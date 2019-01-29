package org.hyperonline.hyperlib.vision;

/**
 * Vision result which stores the skew of the target found.
 *
 * @author James Hagoborg
 */
public class SkewVisionResult extends VisionResult {

    private final double m_skew;

    /**
     * @param result base result to add to
     * @param skew   skew of the target
     */
    public SkewVisionResult(VisionResult result, double skew) {
        this(result.xError(), result.yError(), result.xAbsolute(), result.yAbsolute(), skew, result.foundTarget());
    }

    /**
     * @param xError      error on x-coordinate
     * @param yError      error on y-coordinate
     * @param xAbs        absolute x-coordinate
     * @param yAbs        absolute y-coordinate
     * @param skew        skew of the target
     * @param foundTarget whether a target was actually found
     */
    public SkewVisionResult(double xError, double yError, double xAbs, double yAbs, double skew, boolean foundTarget) {
        super(xError, yError, xAbs, yAbs, foundTarget);
        m_skew = skew;
    }

    /**
     * @return the skew of the target
     */
    public double skew() {
        return m_skew;
    }

}
