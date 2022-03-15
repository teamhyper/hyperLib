package org.hyperonline.hyperlib.pid.rio;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import org.hyperonline.hyperlib.pid.PIDController;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class RioPID extends BaseRioPID<PIDController> {

  public RioPID(
      String name, DoubleSupplier source, double Kp, double Ki, double Kd, double tolerance) {
    this(name, source, null, Kp, Ki, Kd, tolerance);
  }

  public RioPID(
      String name,
      DoubleSupplier source,
      DoubleConsumer output,
      double Kp,
      double Ki,
      double Kd,
      double tolerance) {
    super(name, source, output, Kp, Ki, Kd, tolerance);

    m_pid = new PIDController(m_P_pref.get(), m_I_pref.get(), m_D_pref.get());
    // the parent is added, so we don't need this floating in LW
    SendableRegistry.remove(m_pid);
    onPreferencesUpdated();
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("RIO PID");
  }

  @Override
  public void setSetpoint(double setpoint) {
    m_pid.setSetpoint(setpoint);
  }

  @Override
  public void enable() {
    super.enable();
    m_pid.reset();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Setpoint", m_pid::getSetpoint, null);
  }
}
