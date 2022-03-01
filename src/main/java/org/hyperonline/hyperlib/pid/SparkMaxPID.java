package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.controller.CANSparkMaxHYPER;
import org.hyperonline.hyperlib.controller.sensor.HYPER_SparkMaxRelativeEncoder;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.IntPreference;

public class SparkMaxPID extends PrefPIDController {

  private CANSparkMaxHYPER m_motor;
  private IntPreference m_IZone;
  private DoublePreference m_maxVelocityUnits, m_minOut_pref, m_maxOut_pref;
  private double m_setPoint;
  private com.revrobotics.CANSparkMax.ControlType m_controlType;
  private HYPER_SparkMaxRelativeEncoder m_encoder;
  private com.revrobotics.SparkMaxPIDController m_pidController;
  private int m_pidSlot;

  public SparkMaxPID(
      String name,
      CANSparkMaxHYPER motor,
      com.revrobotics.CANSparkMax.ControlType controlType,
      double Kp,
      double Ki,
      double Kd,
      int kIZone,
      double tolerance,
      double minOut,
      double maxOut,
      double fNatural,
      boolean useAlternate,
      int slotID) {
    super(name, Kp, Ki, Kd, tolerance);
    m_pidSlot = slotID;
    m_motor = motor;
    m_encoder = useAlternate ? m_motor.getAlternateEncoderSendable() : m_motor.getEncoderSendable();
    m_pidController = m_motor.getPIDController();
    m_controlType = controlType;
    m_IZone = m_prefs.addInt("IZone", kIZone);
    m_maxVelocityUnits = m_prefs.addDouble("F Natural", fNatural);
    m_minOut_pref = m_prefs.addDouble("Min Output", minOut);
    m_maxOut_pref = m_prefs.addDouble("Max Output", maxOut);
  }

  public void setSetpoint(double setpoint) {
    m_setPoint = friendlyToNative(setpoint);
  }

  @Override
  public void execute() {
    m_pidController.setReference(m_setPoint, m_controlType, m_pidSlot);
  }

  @Override
  public void disable() {
    super.disable();
    m_motor.set(0);
  }

  @Override
  public double getFromSource() {
    switch (m_controlType) {
      case kVelocity:
        return m_encoder.encoder.getVelocity();
      case kPosition:
        return m_encoder.encoder.getPosition();
      default:
        return 0.0;
    }
  }

  @Override
  public boolean onTarget() {
    return onTarget(nativeToFriendly(m_setPoint));
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
    m_pidController.setIZone(m_IZone.get(), m_pidSlot);
    m_pidController.setFF(1 / m_maxVelocityUnits.get(), m_pidSlot);
    m_pidController.setOutputRange(m_minOut_pref.get(), m_maxOut_pref.get(), m_pidSlot);
  }

  @Override
  protected void setPID(double Kp, double Ki, double Kd) {
    m_pidController.setP(Kp, m_pidSlot);
    m_pidController.setI(Ki, m_pidSlot);
    m_pidController.setD(Kd, m_pidSlot);
  }

  /**
   * @param tolerance
   * @deprecated not needed, as the tolerance is a pref that updates itself
   */
  @Deprecated
  @Override
  protected void setTolerance(double tolerance) {}

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Setpoint", () -> nativeToFriendly(m_setPoint), null);
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMax PID");
  }
}
