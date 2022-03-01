package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import org.hyperonline.hyperlib.controller.SendableMotorController;

public abstract class PrefMotorSlaveSubsystemPhoenix<
        MasterMotorType extends SendableMotorController & IMotorController,
        SlaveMotorType extends SendableMotorController & IMotorController>
    extends PrefMotorSlaveSubsystem<MasterMotorType, SlaveMotorType> {
  protected PrefMotorSlaveSubsystemPhoenix(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    super(name, masterMotor, slaveMotor, inverted);
  }

  protected PrefMotorSlaveSubsystemPhoenix(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor) {
    super(name, masterMotor, slaveMotor);
  }

  @Override
  protected void setFollowing(boolean inverted) {
    m_slaveMotor.follow(m_motor);
    m_slaveMotor.setInverted(inverted);
  }

  @Override
  protected void configMotor() {
    super.configMotor();
  }
}
