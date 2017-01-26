package org.usfirst.frc.team69.util.pid;

import edu.wpi.first.wpilibj.PIDSource;

public class PassivePIDController extends PrefPIDController {

    public PassivePIDController(String prefString, double Kp, double Ki, double Kd, PIDSource source) {
        super(prefString, Kp, Ki, Kd, source, (x) -> {});
    }

    public PassivePIDController(String prefString, double Kp, double Ki, double Kd, PIDSource source, double period) {
        super(prefString, Kp, Ki, Kd, source, (x) -> {}, period);
    }

    public PassivePIDController(String prefString, double Kp, double Ki, double Kd, double Kf, PIDSource source) {
        super(prefString, Kp, Ki, Kd, Kf, source, (x) -> {});
    }

    public PassivePIDController(String prefString, double Kp, double Ki, double Kd, double Kf, PIDSource source,
            double period) {
        super(prefString, Kp, Ki, Kd, Kf, source, (x) -> {}, period);
    }

}
