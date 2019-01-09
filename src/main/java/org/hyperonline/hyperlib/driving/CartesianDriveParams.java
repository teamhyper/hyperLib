package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * 
 * @author Chris McGroarty
 *
 */
public class CartesianDriveParams implements DriveParameters {

	private double m_ySpeed, m_xSpeed, m_zRotate, m_gyroAngle;

	/**
	 * get the speed on the y-axis
	 * 
	 * @return
	 */
	public double ySpeed() {
		return m_ySpeed;
	}

	/**
	 * get the speed on the x-axis
	 * 
	 * @return
	 */
	public double xSpeed() {
		return m_xSpeed;
	}

	/**
	 * get the rotation from the z-axis
	 * 
	 * @return
	 */
	public double zRotate() {
		return m_zRotate;
	}

	/**
	 * get the gyro angle
	 * 
	 * @return
	 */
	public double gyroAngle() {
		return m_gyroAngle;
	}

	/**
	 * Construct a new {@link CartesianDriveParams}.
	 * 
	 * @param ySpeed
	 * @param xSpeed
	 * @param zRotate
	 * @param gyroAngle
	 */
	public CartesianDriveParams(double ySpeed, double xSpeed, double zRotate, double gyroAngle) {
		m_ySpeed = ySpeed;
		m_xSpeed = xSpeed;
		m_zRotate = zRotate;
		m_gyroAngle = gyroAngle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drive(DifferentialDrive driveTrain, double currentGyro) throws WrongDriveTypeException {
		throw new WrongDriveTypeException("using Cartesian with DifferentialDrive");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drive(MecanumDrive driveTrain, double currentGyro) {
		driveTrain.driveCartesian(m_ySpeed, m_xSpeed, m_zRotate, m_gyroAngle);

	}

}
