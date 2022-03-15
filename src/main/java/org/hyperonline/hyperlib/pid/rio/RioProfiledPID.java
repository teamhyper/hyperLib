package org.hyperonline.hyperlib.pid.rio;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import org.hyperonline.hyperlib.pid.ProfiledPIDController;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class RioProfiledPID extends BaseRioPID<ProfiledPIDController> {

  private final DoublePreference m_maxVelocity, m_maxAcceleration;

  public RioProfiledPID(
      String name,
      DoubleSupplier source,
      double Kp,
      double Ki,
      double Kd,
      double tolerance,
      double maxVelocity,
      double maxAcceleration) {
    this(name, source, null, Kp, Ki, Kd, tolerance, maxVelocity, maxAcceleration);
  }

  public RioProfiledPID(
      String name,
      DoubleSupplier source,
      DoubleConsumer output,
      double Kp,
      double Ki,
      double Kd,
      double tolerance,
      double maxVelocity,
      double maxAcceleration) {
    super(name, source, output, Kp, Ki, Kd, tolerance);
    m_maxVelocity = m_prefs.addDouble("Max Velocity", maxVelocity);
    m_maxAcceleration = m_prefs.addDouble("Max Acceleration", maxAcceleration);
    m_pid =
        new ProfiledPIDController(
            m_P_pref.get(),
            m_I_pref.get(),
            m_D_pref.get(),
            new TrapezoidProfile.Constraints(m_maxVelocity.get(), m_maxAcceleration.get()));
    // the parent is added, so we don't need this floating in LW
    SendableRegistry.remove(m_pid);
    onPreferencesUpdated();
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("RIO Profiled PID");
  }

  @Override
  public boolean onTarget() {
    return m_pid.atGoal();
  }

  @Override
  public void setSetpoint(double setpoint) {
    setGoal(setpoint);
  }

  public double calculate(double measurement, TrapezoidProfile.State goal) {
    return m_pid.calculate(measurement, goal);
  }

  public double calculate(double measurement, double goal) {
    return m_pid.calculate(measurement, goal);
  }

  public double calculate(
      double measurement, TrapezoidProfile.State goal, TrapezoidProfile.Constraints constraints) {
    return m_pid.calculate(measurement, goal, constraints);
  }

  public void setGoal(TrapezoidProfile.State goal) {
    m_pid.setGoal(goal);
  }

  public void setGoal(double goal) {
    setGoal(new TrapezoidProfile.State(goal, 0));
  }

  public boolean atGoal() {
    return m_pid.atGoal();
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
    m_pid.setConstraints(
        new TrapezoidProfile.Constraints(m_maxVelocity.get(), m_maxAcceleration.get()));
  }

  @Override
  public void enable() {
    super.enable();
    m_pid.reset(getFromSource());
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Setpoint", () -> m_pid.getGoal().position, null);
  }
}
