package org.hyperonline.hyperlib.pid;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.DoubleSupplier;

public class RioPassivePID extends PrefPIDController {
  protected DoubleSupplier m_source;
  protected PIDController m_pid;

  public RioPassivePID(
      String name, DoubleSupplier source, double Kp, double Ki, double Kd, double tolerance) {
    super(name, Kp, Ki, Kd, tolerance);
    m_source = source;
    m_pid = new PIDController(m_P_pref.get(), m_I_pref.get(), m_D_pref.get());
    onPreferencesUpdated();
  }

  @Override
  protected void setPID(double Kp, double Ki, double Kd) {
    m_pid.setPID(Kp, Ki, Kd);
  }

  @Override
  protected void setTolerance(double tolerance) {
    m_pid.setTolerance(friendlyToNative(tolerance));
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("RIO Passive PID");
  }

  @Override
  public void execute() {
    // passive pid doesn't do anything
  }

  public double calculate(double next) {
    return MathUtil.clamp(m_pid.calculate(next), m_minOut, m_maxOut);
  }

  public double calculate() {
    return calculate(getFromSource());
  }

  @Override
  public void disable() {
    super.disable();
  }

  public PIDController getController() {
    return m_pid;
  }

  @Override
  public double getFromSource() {
    return m_source.getAsDouble();
  }

  @Override
  public boolean onTarget() {
    return m_pid.atSetpoint();
  }

  @Override
  public void setSetpoint(double setpoint) {
    m_pid.setSetpoint(friendlyToNative(setpoint));
  }

  public void setInputRange(double minIn, double maxIn) {
    m_pid.enableContinuousInput(minIn, maxIn);
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
  }


}
