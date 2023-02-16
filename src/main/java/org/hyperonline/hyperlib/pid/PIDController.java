package org.hyperonline.hyperlib.pid;

public class PIDController extends edu.wpi.first.math.controller.PIDController implements IPIDController {
    public PIDController(double kp, double ki, double kd) {
        super(kp, ki, kd);
    }

    public PIDController(double kp, double ki, double kd, double period) {
        super(kp, ki, kd, period);
    }


}
