package org.usfirst.frc.team69.util.vision;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team69.util.pref.IntPreference;

/**
 * Target processor which finds the closest two targets to the crosshairs, and
 * averages their position. This is the algorithm that was used to track the
 * boiler in the 2017 steamworks game. It can more generally be applied to
 * anything that's two pieces of tape.
 * 
 * @author James Hagborg
 *
 */
public class ClosestPairTargetProcessor extends AbstractTargetProcessor<VisionResult> {

    private final IntSupplier m_xCrosshairs, m_yCrosshairs;
    
    /*
     * The most recent point where a target was found. This is stored in
     * absolute pixels, rather than relative to the crosshairs, like the result
     * type stores. This is only used for drawing indicators.
     */
    private Point m_lastPoint;

    /**
     * Construct a new target processor with the given fixed crosshairs
     * position.
     * 
     * @param xCrosshairs
     * @param yCrosshairs
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
     * @param yCrosshairs
     */
    public ClosestPairTargetProcessor(IntSupplier xCrosshairs, IntSupplier yCrosshairs) {
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
    private double targetDistance(Point result) {
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
    private Point targetToPoint(Rect rect) {
        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        return new Point(xCenter, yCenter);
    }

    /**
     * Convert a point to a result, by taking the position to be relative to the
     * crosshairs. Since this method is only called when the point given is
     * actually the chosen target, this also saves the point for drawing a marker
     * later.
     * 
     * @param point The point at the center of the target, in absolute pixels.
     * @return The new VisionResult
     */
    private VisionResult pointToResult(Point point) {
        m_lastPoint = point;
        return new VisionResult(
                (int) point.x - m_xCrosshairs.getAsInt(),
                (int) point.y - m_yCrosshairs.getAsInt(), true);
    }
    
    /**
     * Given two Points, find the average of their positions.
     * 
     * @param a
     * @param b
     * @return
     */
    private Point averagePoints(Point a, Point b) {
        return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VisionResult computeResult(List<Rect> targets) {
        Point[] result = targets.stream()
                .map(this::targetToPoint)
                .sorted(Comparator.comparingDouble(this::targetDistance))
                .limit(2)
                .toArray(Point[]::new);
        if (result.length == 0) {
            return getDefaultValue();
        } else if (result.length == 1) {
            return pointToResult(result[0]);
        } else {
            return pointToResult(averagePoints(result[0], result[1]));
        }
    }

    private static final Scalar MARKER_COLOR = new Scalar(0, 0, 255);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        if (m_lastPoint != null && getLastResult().foundTarget()) {
            Imgproc.drawMarker(mat, m_lastPoint, MARKER_COLOR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisionResult getDefaultValue() {
        return new VisionResult(0, 0, false);
    }

}
