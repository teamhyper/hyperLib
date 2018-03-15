package org.usfirst.frc.team69.util.vision;

/**
 * Vision result which stores the height of the target found.
 * 
 * @author James Hagoborg
 *
 */
public class TargetWithHeightResult extends VisionResult {

    private final double m_height;

    /**
     * Construct a result with the given parameters
     * 
     * @param xError
     *            error on x-coordinate
     * @param yError
     *            error on y-coordinate
     * @param xAbs
     *            absolute x-coordinate
     * @param yAbs
     *            absolute y-coordinate
     * @param height
     *            height of the target
     * @param foundTarget
     *            whether a target was actually found
     */
    public TargetWithHeightResult(double xError, double yError, double xAbs,
            double yAbs, double height, boolean foundTarget) {
        super(xError, yError, xAbs, yAbs, foundTarget);
        m_height = height;
    }

    public double height() { return m_height; }

}
