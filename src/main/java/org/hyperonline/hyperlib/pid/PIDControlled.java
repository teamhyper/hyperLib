package org.hyperonline.hyperlib.pid;

public interface PIDControlled {
  void enable();

  void execute();

  void disable();

  double getFromSource();

  boolean onTarget();

  boolean onTarget(double target);

  void setSetpoint(double setpoint);

  boolean isAbove(double target);

  boolean isBelow(double target);

  boolean isEnabled();

  double getTolerance();

  double getSpeed();
}
