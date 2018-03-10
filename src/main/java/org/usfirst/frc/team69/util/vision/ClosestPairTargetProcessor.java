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
import org.usfirst.frc.team69.robot.vision.VisionResult;
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
public class ClosestPairTargetProcessor implements TargetProcessor<VisionResult> {

    private final IntSupplier m_xCrosshairs, m_yCrosshairs;

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
    private int targetDistance(VisionResult result) {
        int x = result.xError();
        int y = result.yError();
        return x * x + y * y;
    }

    /**
     * Convert a rectangle to a VisionResult. The rectangle is assumed to be
     * specified in pixels, starting from the top-left corner of the screen. The
     * result type stores the difference between the center of the rectangle and
     * the crosshairs.
     * 
     * @param rect
     *            The target to check.
     * @return The corresponding VisionResult.
     */
    private VisionResult targetToResult(Rect rect) {
        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        int xErr = xCenter - m_xCrosshairs.getAsInt();
        int yErr = yCenter - m_yCrosshairs.getAsInt();
        return new VisionResult(xErr, yErr, true);
    }
    
    /**
     * Given two VisionResults, find the average of their positions.
     * 
     * @param a
     * @param b
     * @return
     */
    private VisionResult averageResults(VisionResult a, VisionResult b) {
        int xErr = (a.xError() + b.xError()) / 2;
        int yErr = (a.yError() + b.yError()) / 2;
        return new VisionResult(xErr, yErr, true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public VisionResult process(List<Rect> targets) {
        VisionResult[] result = targets.stream()
                .map(this::targetToResult)
                .sorted(Comparator.comparingInt(this::targetDistance))
                .limit(2)
                .toArray(VisionResult[]::new);
        if (result.length == 0) {
            return getDefaultValue();
        } else if (result.length == 1) {
            return result[0];
        } else {
            return averageResults(result[0], result[1]);
        }
    }

    private static final Scalar MARKER_COLOR = new Scalar(0, 0, 255);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat, VisionResult lastResult) {
        Imgproc.drawMarker(mat, new Point(lastResult.xError(), lastResult.yError()), MARKER_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisionResult getDefaultValue() {
        return new VisionResult(0, 0, false);
    }

}
