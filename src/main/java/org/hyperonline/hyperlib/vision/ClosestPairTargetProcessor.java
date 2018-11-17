package org.hyperonline.hyperlib.vision;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.hyperonline.hyperlib.pid.DisplacementPIDSource;
import org.hyperonline.hyperlib.pref.IntPreference;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.PIDSource;

/**
 * Target processor which finds the closest two targets to the crosshairs, and
 * averages their position. This is the algorithm that was used to track the
 * boiler in the 2017 steamworks game. It can more generally be applied to
 * anything that's two pieces of tape.
 * 
 * This processor uses the height of the targets to measure distance. However,
 * it computes x, y, and height all as simple averages, rather than weighting by
 * depth. This is a good approximation as long as one target is not
 * significantly closer than the other, meaning the camera is not viewing the
 * target from a steep angle.
 * 
 * @author James Hagborg
 *
 */
public class ClosestPairTargetProcessor
        extends AbstractTargetProcessor<TargetWithHeightResult> {

    private final IntSupplier m_xCrosshairs, m_yCrosshairs;

    /**
     * Construct a new target processor with the given fixed crosshairs
     * position.
     * 
     * @param xCrosshairs
     *            X coordinate for the crosshairs
     * @param yCrosshairs
     *            Y coordinate for the crosshairs
     */
    public ClosestPairTargetProcessor(int xCrosshairs, int yCrosshairs) {
        this(() -> xCrosshairs, () -> yCrosshairs);
    }

    /**
     * Construct a new target processor which reads crosshairs position from the
     * given functions. The most common use case would be to pass
     * {@link IntPreference#get}. Whatever you pass must be safe to call from
     * the vision thread, so it should not reference the internals of commands,
     * subsystems, or other robot code. Note that preferences are safe to access
     * from any thread.
     * 
     * @param xCrosshairs
     *            X coordinate for the crosshairs
     * @param yCrosshairs
     *            Y coordinate for the crosshairs
     */
    public ClosestPairTargetProcessor(IntSupplier xCrosshairs,
            IntSupplier yCrosshairs) {
        m_xCrosshairs = Objects.requireNonNull(xCrosshairs);
        m_yCrosshairs = Objects.requireNonNull(yCrosshairs);
    }

    /**
     * Compute the Euclidean distance (actually the distance squared) of a
     * target from the crosshairs.
     * 
     * @param result
     *            The target to check
     * @return The distance from the crosshairs
     */
    private double targetDistance(Point3 result) {
        double xError = result.x - m_xCrosshairs.getAsInt();
        double yError = result.y - m_yCrosshairs.getAsInt();
        return xError * xError + yError * yError;
    }

    /**
     * Find the center of a rectangle.
     * 
     * @param rect
     *            The target rectangle.
     * @return The point at the center of the rectangle.
     */
    private Point3 targetToPoint(Rect rect) {
        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        int height = rect.height;
        return new Point3(xCenter, yCenter, height);
    }

    /**
     * Convert a point to a result, by taking the position to be relative to the
     * crosshairs. Since this method is only called when the point given is
     * actually the chosen target, this also saves the point for drawing a
     * marker later.
     * 
     * @param point
     *            The point at the center of the target, in absolute pixels.
     * @return The new VisionResult
     */
    private TargetWithHeightResult pointToResult(Point3 point) {
        return new TargetWithHeightResult(
                point.x - m_xCrosshairs.getAsInt(),
                point.y - m_yCrosshairs.getAsInt(),
                point.x, point.y, point.z, true);
    }

    /**
     * Given two Points, find the average of their positions.
     * 
     * @param a
     * @param b
     * @return
     */
    private Point3 averagePoints(Point3 a, Point3 b) {
        return new Point3((a.x + b.x) / 2, (a.y + b.y) / 2, (a.z + b.z) / 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetWithHeightResult computeResult(List<Rect> targets) {
        Point3[] result = targets.stream().map(this::targetToPoint)
                .sorted(Comparator.comparingDouble(this::targetDistance))
                .limit(2).toArray(Point3[]::new);
        if (result.length < 2) {
            return getDefaultValue();
        } else {
            return pointToResult(averagePoints(result[0], result[1]));
        }
    }
    
    /**
     * Get a PID source that returns the height of the target.
     * 
     * @return A PID source returning the height of the target.
     */
    public PIDSource heightPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().height();
            }
        };
    }

    private static final Scalar MARKER_COLOR = new Scalar(0, 0, 255);
    private static final double MARKER_WIDTH = 6;

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        TargetWithHeightResult result = getLastResult();
        if (result.foundTarget()) {
            Point center = new Point(result.xAbsolute(), result.yAbsolute());
            double height = result.height();
            Point tl = new Point(center.x - MARKER_WIDTH / 2, center.y - height / 2);
            Point tr = new Point(center.x + MARKER_WIDTH / 2, center.y - height / 2);
            Point bl = new Point(center.x - MARKER_WIDTH / 2, center.y + height / 2);
            Point br = new Point(center.x + MARKER_WIDTH / 2, center.y + height / 2);
            Imgproc.drawMarker(mat, center, MARKER_COLOR);
            Imgproc.line(mat, tl, tr, MARKER_COLOR);
            Imgproc.line(mat, bl, br, MARKER_COLOR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetWithHeightResult getDefaultValue() {
        return new TargetWithHeightResult(0, 0, 0, 0, 0, false);
    }

}
