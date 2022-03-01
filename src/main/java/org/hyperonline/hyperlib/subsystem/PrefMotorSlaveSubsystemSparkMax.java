package org.hyperonline.hyperlib.subsystem;

import com.revrobotics.CANSparkMaxLowLevel;
import org.hyperonline.hyperlib.controller.HYPER_CANSparkMax;

public abstract class PrefMotorSlaveSubsystemSparkMax
    extends PrefMotorSlaveSubsystem<HYPER_CANSparkMax, HYPER_CANSparkMax> {
  protected PrefMotorSlaveSubsystemSparkMax(
          String name, HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor, boolean inverted) {
    super(name, masterMotor, slaveMotor, inverted);
  }

  protected PrefMotorSlaveSubsystemSparkMax(
          String name, HYPER_CANSparkMax masterMotor, HYPER_CANSparkMax slaveMotor) {
    super(name, masterMotor, slaveMotor);
  }

  @Override
  protected void setFollowing(boolean inverted) {
    m_slaveMotor.follow(m_motor, inverted);
  }

  @Override
  protected void configMotor() {
    m_motor.restoreFactoryDefaults();
    m_slaveMotor.restoreFactoryDefaults();
    super.configMotor();

    m_motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
    m_motor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 10);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
    m_slaveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 500);
  }
}
