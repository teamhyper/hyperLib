package org.hyperonline.hyperlib.vision;

/**
 * Vision result which stores the height of the target found.
 *
 * @author James Hagoborg
 */
public class TargetWithHeightResult extends VisionResult {

    private final double m_height;

    /**
     *
     * @param result    base result to add to
     * @param height    height of the target
     */
    public TargetWithHeightResult(VisionResult result, double height) {
        this(result.xError(), result.yError(), result.xAbsolute(), result.yAbsolute(), height, result.foundTarget());
    }

    /**
     * Construct a result with the given parameters
     *
     * @param xError      error on x-coordinate
     * @param yError      error on y-coordinate
     * @param xAbs        absolute x-coordinate
     * @param yAbs        absolute y-coordinate
     * @param height      height of the target
     * @param foundTarget whether a target was actually found
     */
    public TargetWithHeightResult(double xError, double yError, double xAbs,
                                  double yAbs, double height, boolean foundTarget) {
        super(xError, yError, xAbs, yAbs, foundTarget);
        m_height = height;
    }

    /**
     *
     * @return  height of the target
     */
    public double height() {
        return m_height;
    }

}
