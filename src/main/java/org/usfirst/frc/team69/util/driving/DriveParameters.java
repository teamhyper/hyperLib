package org.usfirst.frc.team69.util.driving;

import edu.wpi.first.wpilibj.RobotDrive;

public interface DriveParameters {
    void drive(RobotDrive driveTrain, double currentGyro);
}
