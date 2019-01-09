package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * 
 * @author Chris McGroarty
 *
 */
public class PolarDriveParams implements DriveParameters {

	private double m_magnitude, m_angle, m_zRotation;

	/**
	 * get the magnitude
	 * 
	 * @return the magnitude
	 */
	public double magnitude() {
		return m_magnitude;
	}

	/**
	 * get the angle
	 * 
	 * @return the angle
	 */
	public double angle() {
		return m_angle;
	}

	/**
	 * get the rotation
	 * 
	 * @return the z-axis rotation
	 */
	public double rotate() {
		return m_zRotation;
	}

	/**
	 * Construct a new {@link PolarDriveParams}.
	 * 
	 * @param magnitude 	the amount to move
	 * @param angle 		the angle to move at
	 * @param rotate 		the rotation to apply
	 */
	public PolarDriveParams(double magnitude, double angle, double rotate) {
		m_magnitude = magnitude;
		m_angle = angle;
		m_zRotation = rotate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drive(DifferentialDrive driveTrain, double currentGyro) throws WrongDriveTypeException {
		throw new WrongDriveTypeException("using Polar with DifferentialDrive");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drive(MecanumDrive driveTrain, double currentGyro) {
		driveTrain.drivePolar(m_magnitude, m_angle, m_zRotation);
	}

}
