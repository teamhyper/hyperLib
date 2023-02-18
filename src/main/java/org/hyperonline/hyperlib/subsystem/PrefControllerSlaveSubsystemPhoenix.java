package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import org.hyperonline.hyperlib.controller.SendableMotorController;
/**
 * @author Chris McGroarty
 */
public abstract class PrefControllerSlaveSubsystemPhoenix<
        MasterMotorType extends SendableMotorController & IMotorController,
        SlaveMotorType extends SendableMotorController & IMotorController>
    extends PrefControllerSlaveSubsystem<MasterMotorType, SlaveMotorType> {
  /** {@inheritDoc} */
  protected PrefControllerSlaveSubsystemPhoenix(
      MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    super(masterMotor, slaveMotor, inverted);
  }
  /** {@inheritDoc} */
  protected PrefControllerSlaveSubsystemPhoenix(MasterMotorType masterMotor, SlaveMotorType slaveMotor) {
    super(masterMotor, slaveMotor);
  }

  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name {@inheritDoc}
   */
  @Deprecated
  protected PrefControllerSlaveSubsystemPhoenix(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    this(masterMotor, slaveMotor, inverted);
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
