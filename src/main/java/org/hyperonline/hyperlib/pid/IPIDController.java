package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.Sendable;

/**
 * work-around to give {@link edu.wpi.first.math.controller.PIDController} and {@link
 * edu.wpi.first.math.controller.ProfiledPIDController} a common ancestor, as they share a lot of
 * common functionality. So we can have an genericized {@link org.hyperonline.hyperlib.pid.rio.BaseRioPID}
 */
public interface IPIDController extends Sendable {
  void setPID(double kp, double ki, double kd);

  double getP();

  void setP(double kp);

  double getI();

  void setI(double ki);

  double getD();

  void setD(double kd);

  double getPeriod();

  boolean atSetpoint();

  void enableContinuousInput(double minimumInput, double maximumInput);

  void disableContinuousInput();

  void setIntegratorRange(double minimumIntegral, double maximumIntegral);

  void setTolerance(double positionTolerance);

  void setTolerance(double positionTolerance, double velocityTolerance);

  double getPositionError();

  double getVelocityError();

  double calculate(double measurement);

  double calculate(double measurement, double setpoint);
}
