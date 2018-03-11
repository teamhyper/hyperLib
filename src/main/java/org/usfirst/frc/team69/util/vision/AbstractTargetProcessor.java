package org.usfirst.frc.team69.util.vision;

import java.util.List;

import org.opencv.core.Rect;
import org.usfirst.frc.team69.util.pid.DisplacementPIDSource;

import edu.wpi.first.wpilibj.PIDSource;

/**
 * Base class with common functionality used by TargetProcessors. In particular,
 * this provides methods to store a result, and access it via PID sources.
 * 
 * @author James Hagborg
 *
 * @param <T>
 */
public abstract class AbstractTargetProcessor<T extends VisionResult> implements TargetProcessor {

    private volatile T m_lastResult;

    public abstract T computeResult(List<Rect> targets);
    
    public abstract T getDefaultValue();
    
    @Override
    public final void process(List<Rect> targets) {
        m_lastResult = computeResult(targets);
    }

    /**
     * Get the most recent result of this processor. If no result has been
     * produced yet, returns the default value.
     * 
     * @return The most recent result.
     */
    public T getLastResult() {
        T res = m_lastResult;
        return res == null ? getDefaultValue() : res;
    }
    
    /**
     * Get a PID source which tracks the x-coordinate of the target.
     * 
     * @return A PID source tracking the x-coordinate of the target.
     */
    public PIDSource xPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().xError();
            }
        };
    }

    /**
     * Get a PID source which tracks the y-coordinate of the target.
     * 
     * @return A PID source tracking the y-coordinate of the target.
     */
    public PIDSource yPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().yError();
            }
        };
    }
}
