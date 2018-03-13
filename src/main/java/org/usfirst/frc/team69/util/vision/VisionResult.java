package org.usfirst.frc.team69.util.vision;

/**
 * This class stores the result of running the vision pipeline.
 * This contains the location of the target.  If we want to
 * do more advanced stuff (like find many targets, or different
 * types) add some data here.
 * 
 * This class is immutable.  That way it can be safely passed
 * between threads.
 * 
 * @author James Hagborg
 *
 */
public class VisionResult {
    private final int m_xError;
    private final int m_yError;
    private final boolean m_foundTarget;
    
    /**
     * 
     * @param xError
     * 			error on the x-axis
     * @param yError
     * 			error on the y-axis
     * @param foundTarget
     * 			is the target found
     */
    public VisionResult(int xError, int yError, boolean foundTarget) {
        m_xError = xError;
        m_yError = yError;
        m_foundTarget = foundTarget;
    }
    
    public int xError() { return m_xError; }
    public int yError() { return m_yError; }
    public boolean foundTarget() { return m_foundTarget; }
}