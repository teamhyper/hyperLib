package org.usfirst.frc.team69.util.pid;

import java.util.function.DoubleSupplier;

import org.usfirst.frc.team69.robot.hypercode2016.Robot;
import org.usfirst.frc.team69.util.driving.ArcadeDriveParams;
import org.usfirst.frc.team69.util.driving.DriveParameters;

import edu.wpi.first.wpilibj.PIDSource;

public class DrivePIDController extends PrefPIDController {
    
    private final DoubleSupplier m_moveCallback;
    
    public DrivePIDController(String prefName, double p, double i, double d,
            PIDSource source, boolean directDrive) {
        this(prefName, p, i, d, source, directDrive, () -> 0);
    }
    
    public DrivePIDController(String prefName, double p, double i, double d, 
            PIDSource source, boolean directDrive, DoubleSupplier moveCallback) {
        super(prefName, p, i, d, source, (output) -> {
            if (directDrive) {
                Robot.driveTrain.arcadeDriveDirect(moveCallback.getAsDouble(), output, false); 
            }
        });
        m_moveCallback = moveCallback;
    }
    
    public DriveParameters getDriveParams() {
        return new ArcadeDriveParams(m_moveCallback.getAsDouble(), get(), false);
    }
}
