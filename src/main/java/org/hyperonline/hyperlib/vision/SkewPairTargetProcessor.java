package org.hyperonline.hyperlib.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

/**
 * Target processor class which picks out the pair of targets skewed towards each other.
 *
 * @author James Hagborg
 */
public class SkewPairTargetProcessor extends AbstractTargetProcessor<SkewVisionResult> {

    private final IntSupplier m_xCrosshairs, m_yCrosshairs;

    /**
     *
     * @param connector VisionConnector for retreiving/publishing the result
     * @param xCrosshairs X coordinate for the crosshairs
     * @param yCrosshairs Y coordinate for the crosshairs
     */
    public SkewPairTargetProcessor(SkewVisionConnector connector, int xCrosshairs, int yCrosshairs) {
        this(connector, () -> xCrosshairs, () -> yCrosshairs);
    }

    /**
     *
     * @param connector VisionConnector for retreiving/publishing the result
     * @param xCrosshairs X coordinate for the crosshairs
     * @param yCrosshairs Y coordinate for the crosshairs
     */
    public SkewPairTargetProcessor(SkewVisionConnector connector, IntSupplier xCrosshairs,
                                   IntSupplier yCrosshairs) {
        super(connector);
        m_xCrosshairs = Objects.requireNonNull(xCrosshairs);
        m_yCrosshairs = Objects.requireNonNull(yCrosshairs);
    }

    private double targetDistance(Rect result) {
        Point center = centerOfTarget(result);
        double xError = center.x - m_xCrosshairs.getAsInt();
        double yError = center.y - m_yCrosshairs.getAsInt();
        return xError * xError + yError * yError;
    }

    private Point centerOfTarget(Rect rect) {
        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        return new Point(xCenter, yCenter);
    }

    private Point averagePoints(Point a, Point b) {
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    private SkewVisionResult pairToResult(Rect r1, Rect r2) {
        // Swap order so r1 is on the left
        if (r1.x > r2.x) {
            Rect t = r1;
            r1 = r2;
            r2 = t;
        }
        final double skew = r1.height / r2.height - r2.height / r1.height;
        final Point center = averagePoints(centerOfTarget(r1), centerOfTarget(r2));
        return new SkewVisionResult(center.x - m_xCrosshairs.getAsInt(),
                center.y - m_yCrosshairs.getAsInt(),
                center.x, center.y, skew, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SkewVisionResult computeResult(List<Rect> targets) {
        Rect[] result = targets.stream()
                .sorted(Comparator.comparingDouble(this::targetDistance))
                .limit(2).toArray(Rect[]::new);
        if (result.length < 2) {
            return null;
        } else {
            return pairToResult(result[0], result[1]);
        }
    }

    private static final Scalar MARKER_COLOR = new Scalar(0, 0, 255);
    private static final double MARKER_SCALE = 20;

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        SkewVisionResult result = getLastResult();
        if (result.foundTarget()) {
            Point center = new Point(result.xAbsolute(), result.yAbsolute());
            Point skewMarker = new Point(center.x + result.skew() * MARKER_SCALE, center.y);
            Imgproc.circle(mat, center, 6, MARKER_COLOR);
            Imgproc.line(mat, center, skewMarker, MARKER_COLOR);
        }
    }
}