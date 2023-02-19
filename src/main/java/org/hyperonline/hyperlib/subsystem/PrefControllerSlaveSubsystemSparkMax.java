package org.hyperonline.hyperlib.subsystem;

import com.revrobotics.CANSparkMaxLowLevel;
import org.hyperonline.hyperlib.controller.HYPER_CANSparkMax;

/**
 * @author Chris McGroarty
 */
public abstract class PrefControllerSlaveSubsystemSparkMax
    extends PrefControllerSlaveSubsystem<HYPER_CANSparkMax, HYPER_CANSparkMax> {
  /**
   * {@inheritDoc}
   */
  protected PrefControllerSlaveSubsystemSparkMax(
      HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor, boolean inverted) {
    this(null, masterMotor, slaveMotor, inverted);
  }
  /**
   * {@inheritDoc}
   */
  protected PrefControllerSlaveSubsystemSparkMax(
      HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor) {
    super(masterMotor, slaveMotor);
  }

  /**
   * {@inheritDoc}
   */
  protected PrefControllerSlaveSubsystemSparkMax(
      String name, HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor, boolean inverted) {
    super(name, masterMotor, slaveMotor, inverted);
  }
  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name
   * {@inheritDoc}
   */
  @Deprecated
  protected PrefControllerSlaveSubsystemSparkMax(
      String name, HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor) {
    this(masterMotor, slaveMotor);
  }

  @Override
  protected void setFollowing(boolean inverted) {
    m_slaveMotor.follow(m_motor, inverted);
  }

  @Override
  protected void configMotor() {
    super.configMotor();

    m_motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
    m_motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 10);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
  }
}
