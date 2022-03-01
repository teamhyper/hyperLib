package org.hyperonline.hyperlib.subsystem;

import com.revrobotics.CANSparkMaxLowLevel;
import org.hyperonline.hyperlib.controller.CANSparkMaxHYPER;

public abstract class PrefMotorSlaveSubsystemSparkMax
    extends PrefMotorSlaveSubsystem<CANSparkMaxHYPER, CANSparkMaxHYPER> {
  protected PrefMotorSlaveSubsystemSparkMax(
      String name, CANSparkMaxHYPER masterMotor, CANSparkMaxHYPER slaveMotor, boolean inverted) {
    super(name, masterMotor, slaveMotor, inverted);
  }

  protected PrefMotorSlaveSubsystemSparkMax(
      String name, CANSparkMaxHYPER masterMotor, CANSparkMaxHYPER slaveMotor) {
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
