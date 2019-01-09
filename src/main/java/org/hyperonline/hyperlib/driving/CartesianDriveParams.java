package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class CartesianDriveParams implements DriveParameters {
	
	private double m_ySpeed, m_xSpeed, m_zRotate, m_gyroAngle;
	
	public double ySpeed() {
		return m_ySpeed;
	}

	public double xSpeed() {
		return m_xSpeed;
	}

	public double zRotate() {
		return m_zRotate;
	}

	public double gyroAngle() {
		return m_gyroAngle;
	}

	public CartesianDriveParams(double ySpeed, double xSpeed, double zRotate, double gyroAngle) {
		m_ySpeed = ySpeed;
		m_xSpeed = xSpeed;
		m_zRotate = zRotate;
		m_gyroAngle = gyroAngle;
	}

	@Override
	public void drive(DifferentialDrive driveTrain, double currentGyro) throws WrongDriveTypeException {
		throw new WrongDriveTypeException("using Cartesian with DifferentialDrive");

	}

	@Override
	public void drive(MecanumDrive driveTrain, double currentGyro) {
		driveTrain.driveCartesian(m_ySpeed, m_xSpeed, m_zRotate, m_gyroAngle);

	}

}
