package org.usfirst.frc.team69.util;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Base class for a PID Source that just supports displacement.
 * 
 * @author James Hagborg
 */
public abstract class DisplacementOnlyPIDSource implements PIDSource {

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        if (pidSource != PIDSourceType.kDisplacement) {
            throw new UnsupportedOperationException("Only displacement is supported");
        }
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }
}
