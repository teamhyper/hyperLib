package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class PolarDriveParams implements DriveParameters {
	
	private double m_magnitude, m_angle, m_zRotation;
	
	public double magnitude() {
		return m_magnitude;
	}

	public double angle() {
		return m_angle;
	}

	public double rotate() {
		return m_zRotation;
	}

	public PolarDriveParams(double magnitude, double angle, double rotate) {
		m_magnitude = magnitude;
		m_angle = angle;
		m_zRotation = rotate;
	}

	@Override
	public void drive(DifferentialDrive driveTrain, double currentGyro) throws WrongDriveTypeException {
		throw new WrongDriveTypeException("using Polar with DifferentialDrive");

	}

	@Override
	public void drive(MecanumDrive driveTrain, double currentGyro) {
		driveTrain.drivePolar(m_magnitude, m_angle, m_zRotation);
	}

}
