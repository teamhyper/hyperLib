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
     * Construct a new vision result with the given values.
     * 
     * @param xError The x co-ordinate of the error
     * @param yError The y co-ordinate of the error
     * @param foundTarget Whether or not a target was actually found (if false, then x,y are garbage)
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
