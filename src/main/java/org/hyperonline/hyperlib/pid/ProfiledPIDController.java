package org.hyperonline.hyperlib.pid;

import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class ProfiledPIDController extends edu.wpi.first.math.controller.ProfiledPIDController implements IPIDController{
    public ProfiledPIDController(double Kp, double Ki, double Kd, TrapezoidProfile.Constraints constraints) {
        super(Kp, Ki, Kd, constraints);
    }

    public ProfiledPIDController(double Kp, double Ki, double Kd, TrapezoidProfile.Constraints constraints, double period) {
        super(Kp, Ki, Kd, constraints, period);
    }
}
