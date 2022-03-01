package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.hyperonline.hyperlib.controller.SendableMotorController;

public abstract class PrefMotorSlaveSubsystem<
        MasterMotorType extends SendableMotorController,
        SlaveMotorType extends SendableMotorController>
    extends PreferenceMotorSubsystem<MasterMotorType> {
  protected SlaveMotorType m_slaveMotor;
  protected boolean m_invertedSlave;

  protected PrefMotorSlaveSubsystem(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor) {
    this(name, masterMotor, slaveMotor, false);
  }

  protected PrefMotorSlaveSubsystem(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    super(name, masterMotor);
    m_slaveMotor = slaveMotor;
    this.addChild("Slave Motor", m_slaveMotor);
    m_invertedSlave = inverted;
  }

  protected abstract void setFollowing(boolean inverted);

  @Override
  public void setNeutralMode(NeutralMode mode) {
    super.setNeutralMode(mode);
    m_slaveMotor.setNeutralMode(mode);
  }

  @Override
  protected void configMotor() {
    super.configMotor();
    this.setFollowing(m_invertedSlave);
  }
}
