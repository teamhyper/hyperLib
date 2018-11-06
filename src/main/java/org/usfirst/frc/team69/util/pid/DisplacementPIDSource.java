package org.usfirst.frc.team69.util.pid;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
/**
 * {@link DisplacementPIDSource}
 * @author James
 *
 */
public abstract class DisplacementPIDSource implements PIDSource{
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