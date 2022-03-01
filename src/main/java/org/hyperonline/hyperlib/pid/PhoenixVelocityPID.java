package org.hyperonline.hyperlib.pid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pref.DoublePreference;

public class PhoenixVelocityPID extends PhoenixPID {
  private DoublePreference m_maxVelocityUnits;

  public PhoenixVelocityPID(
      String name,
      WPI_TalonSRX motor,
      int pidSlot,
      double Kp,
      double Ki,
      double Kd,
      double fNatural,
      double tolerance,
      int kIZone,
      double kPeakOut) {
    super(name, motor, pidSlot, Kp, Ki, Kd, tolerance, kIZone, kPeakOut);
    m_maxVelocityUnits = m_prefs.addDouble("F Natural", fNatural);
  }

  @Override
  public void execute() {
    m_motor.set(ControlMode.Velocity, m_setPoint);
  }

  public void setP(double Kp) {
    m_motor.config_kP(m_pidSlot, Kp);
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
    // F: 1023 / ### natural units for max output
    // max natural output was 314
    m_motor.config_kF(m_pidSlot, 1023.0 / this.m_maxVelocityUnits.get());
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("Phoenix Velocity PID");
  }

  @Override
  public double getFromSource() {
    return m_motor.getSelectedSensorVelocity(m_pidSlot);
  }
}
