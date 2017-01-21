package org.usfirst.frc.team69.util.driving;

import edu.wpi.first.wpilibj.RobotDrive;

public class ArcadeDriveParams implements DriveParameters {
    private final double m_move, m_rotate;
    private final boolean m_squareInputs;
    
    public ArcadeDriveParams(double move, double rotate, boolean squareInputs) {
        m_move = move;
        m_rotate = rotate;
        m_squareInputs = squareInputs;
    }
    
    @Override
    public void drive(RobotDrive driveTrain, double currentGyro) {
        driveTrain.arcadeDrive(m_move, m_rotate, m_squareInputs);
    }
    
    public double move() { return m_move; }
    public double rotate() { return m_rotate; }
    public boolean squareInputs() { return m_squareInputs; }
    
}
