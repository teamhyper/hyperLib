package org.usfirst.frc.team69.util.vision;

/**
 * This class stores the result of running the vision pipeline. This contains
 * the location of the target. If we want to do more advanced stuff (like find
 * many targets, or different types) add some data here.
 * 
 * This class is immutable. That way it can be safely passed between threads.
 * 
 * @author James Hagborg
 *
 */
public class VisionResult {
    private final double m_xError;
    private final double m_yError;
    private final double m_xAbs;
    private final double m_yAbs;
    private final boolean m_foundTarget;

    /**
     * 
     * @param xError
     *            error on the x-axis
     * @param yError
     *            error on the y-axis
     * @param xAbs
     *            absolute x-coordinate
     * @param yAbs
     *            absolute y-coordinate
     * @param foundTarget
     *            is the target found
     */
    public VisionResult(double xError, double yError, double xAbs, double yAbs,
            boolean foundTarget) {
        m_xError = xError;
        m_yError = yError;
        m_xAbs = xAbs;
        m_yAbs = yAbs;
        m_foundTarget = foundTarget;
    }

    public double xError() { return m_xError; }
    public double yError() { return m_yError; }
    public boolean foundTarget() { return m_foundTarget; }
    public double xAbsolute() { return m_xAbs; }
    public double yAbsolute() { return m_yAbs; }
}