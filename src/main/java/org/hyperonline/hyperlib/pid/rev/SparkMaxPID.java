package org.hyperonline.hyperlib.pid.rev;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.controller.HYPER_CANSparkMax;
import org.hyperonline.hyperlib.controller.sensor.HYPER_CANSensorSendable;
import org.hyperonline.hyperlib.pid.PrefPIDController;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.IntPreference;

public class SparkMaxPID extends PrefPIDController {

  private HYPER_CANSparkMax m_motor;
  private IntPreference m_IZone;
  private DoublePreference m_maxVelocityUnits;
  private double m_setPoint;
  private CANSparkMax.ControlType m_controlType;
  private HYPER_CANSensorSendable m_sensor;
  private SparkMaxPIDController m_pidController;
  private int m_pidSlot;

  public SparkMaxPID(
      String name,
      HYPER_CANSparkMax motor,
      CANSparkMax.ControlType controlType,
      double Kp,
      double Ki,
      double Kd,
      int kIZone,
      double tolerance,
      double fNatural,
      HYPER_CANSensorSendable sensorToUse,
      int slotID) {
    super(name, Kp, Ki, Kd, tolerance);
    m_pidSlot = slotID;
    m_motor = motor;
    m_sensor = sensorToUse;
    m_pidController = m_motor.getPIDController();
    m_pidController.setFeedbackDevice(m_sensor.getSensor());
    m_controlType = controlType;
    m_IZone = m_prefs.addInt("IZone", kIZone);
    m_maxVelocityUnits = m_prefs.addDouble("F Natural", fNatural);

    // TODO: determine if we need this or if the PreferencesUpdater can trigger this and have it
    // grab the data
    this.onPreferencesUpdated();
  }

  /**
   * @param maxVel limit the velocity in RPM of the pid controller
   * @param minVel lower bound in RPM of the pid controller
   * @param maxAcc will limit the acceleration in RPM^2
   * @param accelStrategy acceleration strategy to use for the generated motion profile
   */
  public void initSmartMotionProfile(
      double maxVel,
      double minVel,
      double maxAcc,
      SparkMaxPIDController.AccelStrategy accelStrategy) {
    if (m_controlType == CANSparkMax.ControlType.kSmartMotion) {
      if (accelStrategy == SparkMaxPIDController.AccelStrategy.kTrapezoidal) {
        m_pidController.setSmartMotionAccelStrategy(accelStrategy, m_pidSlot);
        m_pidController.setSmartMotionMaxVelocity(maxVel, m_pidSlot);
        m_pidController.setSmartMotionMinOutputVelocity(minVel, m_pidSlot);
        m_pidController.setSmartMotionMaxAccel(maxAcc, m_pidSlot);
        m_pidController.setSmartMotionAllowedClosedLoopError(m_tolerance_pref.get(), m_pidSlot);
      } else {
        throw new IllegalArgumentException(
            "As of the 2022 FRC season, SparkMax firmware only supports the trapezoidal motion profiling acceleration strategy");
      }
    } else {
      throw new IllegalStateException(
          "SparkMaxPID is not using SmartMotion ControlType, cannot set Smart Motion Profile");
    }
  }

  /** {@inheritDoc} */
  public void initSmartMotionProfile(
      double maxVel, double minVel, double maxAcc) {
    initSmartMotionProfile(
        maxVel, minVel, maxAcc, SparkMaxPIDController.AccelStrategy.kTrapezoidal);
  }

  public void setSetpoint(double setpoint) {
    m_setPoint = setpoint;
  }

  @Override
  public void execute() {}

  @Override
  public void enable() {
    super.enable();
    m_pidController.setReference(friendlyToNative(m_setPoint), m_controlType, m_pidSlot);
  }

  @Override
  public void disable() {
    super.disable();
    m_motor.stopMotor();
  }

  @Override
  public double getFromSource() {
    switch (m_controlType) {
      case kVelocity:
      case kSmartVelocity:
        return nativeToFriendly(m_sensor.getVelocity());
      case kPosition:
      case kSmartMotion:
        return nativeToFriendly(m_sensor.getPosition());
      default:
        return 0.0;
    }
  }

  @Override
  public boolean onTarget() {
    return onTarget(m_setPoint);
  }

  @Override
  public void onPreferencesUpdated() {
    super.onPreferencesUpdated();
    m_pidController.setIZone(m_IZone.get(), m_pidSlot);
    m_pidController.setFF(1 / m_maxVelocityUnits.get(), m_pidSlot);
  }

  @Override
  protected void updateOutputRange() {
    super.updateOutputRange();
    m_pidController.setOutputRange(m_minOut, m_maxOut, m_pidSlot);
  }

  @Override
  protected void setPID(double Kp, double Ki, double Kd) {
    m_pidController.setP(Kp, m_pidSlot);
    m_pidController.setI(Ki, m_pidSlot);
    m_pidController.setD(Kd, m_pidSlot);
  }

  @Override
  protected void setTolerance(double tolerance) {
    if(m_controlType == CANSparkMax.ControlType.kSmartMotion) {
      m_pidController.setSmartMotionAllowedClosedLoopError(tolerance, m_pidSlot);
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Setpoint", () -> m_setPoint, null);
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMax PID");
  }
}
