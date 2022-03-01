package org.hyperonline.hyperlib.pid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.IntPreference;

public abstract class PhoenixPID extends PrefPIDController {
  protected WPI_TalonSRX m_motor;
  protected int m_pidSlot;
  protected DoublePreference m_PeakOutput;
  protected IntPreference m_IZone;
  protected double m_setPoint;

  public PhoenixPID(
      String name,
      WPI_TalonSRX motor,
      int pidSlot,
      double Kp,
      double Ki,
      double Kd,
      double tolerance,
      int kIZone,
      double kPeakOut) {
    super(name, Kp, Ki, Kd, tolerance);
    m_motor = motor;
    m_pidSlot = pidSlot;

    m_IZone = m_prefs.addInt("IZone", kIZone);
    m_PeakOutput = m_prefs.addDouble("Peak Out", kPeakOut);
  }

  public void setSetpoint(double setpoint) {
    m_setPoint = friendlyToNative(setpoint);
  }

  @Override
  public void disable() {
    super.disable();
    m_motor.set(ControlMode.PercentOutput, 0);
  }

  @Override
  protected void setPID(double Kp, double Ki, double Kd) {
    m_motor.config_kP(m_pidSlot, Kp);
    m_motor.config_kI(m_pidSlot, Ki);
    m_motor.config_kD(m_pidSlot, Kd);
  }

  @Override
  public void execute() {}

  /**
   * @deprecated not needed, as the tolerance is a pref that updates itself
   * @param tolerance
   */
  @Deprecated
  @Override
  public void setTolerance(double tolerance) {}

  @Override
  public boolean onTarget() {
    return onTarget(nativeToFriendly(m_setPoint));
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
    m_motor.config_IntegralZone(m_pidSlot, m_IZone.get());
    m_motor.configClosedLoopPeakOutput(m_pidSlot, m_PeakOutput.get());
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Setpoint", () -> nativeToFriendly(m_setPoint), null);
  }
}
