package org.usfirst.frc.team69.util.driving;

import edu.wpi.first.wpilibj.RobotDrive;

public class TankDriveParams implements DriveParameters {

    private final double m_left, m_right;
    private final boolean m_squareInputs;
    
    public TankDriveParams(double left, double right, boolean squareInputs) {
        m_left = left;
        m_right = right;
        m_squareInputs = squareInputs;
    }
    
    @Override
    public void drive(RobotDrive driveTrain, double currentGyro) {
        driveTrain.tankDrive(m_left, m_right, m_squareInputs);
    }
    
    public double left() { return m_left; }
    public double right() { return m_right; }
    public boolean squareInputs() { return m_squareInputs; }
}
