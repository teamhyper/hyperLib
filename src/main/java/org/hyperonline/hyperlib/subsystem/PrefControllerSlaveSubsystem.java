package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.util.sendable.Sendable;
import org.hyperonline.hyperlib.controller.SendableMotorController;

import java.util.stream.Stream;

/**
 * @author Chris McGroarty
 */
public abstract class PrefControllerSlaveSubsystem<
        MasterMotorType extends SendableMotorController,
        SlaveMotorType extends SendableMotorController>
    extends PreferenceControllerSubsystem<MasterMotorType> {
  protected SlaveMotorType m_slaveMotor;
  protected boolean m_invertedSlave;

  /**
   *
   * @param masterMotor the motor to use in the subsystem
   * @param slaveMotor the motor to follow the master motor in the subsystem
   * @param inverted is the slaveMotor inverted compared to the masterMotor
   */
  protected PrefControllerSlaveSubsystem(
          MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    super(masterMotor);
    m_slaveMotor = slaveMotor;
    this.addChild("Slave Motor", m_slaveMotor);
    m_invertedSlave = inverted;
  }

  /**
   * {@inheritDoc}
   */
  protected PrefControllerSlaveSubsystem(MasterMotorType masterMotor, SlaveMotorType slaveMotor) {
    this(masterMotor, slaveMotor, false);
  }



  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name
   * @param name string to use as the Subsystem's name
   * @param masterMotor the motor to use in the subsystem
   * @param slaveMotor the motor to follow the master motor in the subsystem
   * @param inverted is the slaveMotor inverted compared to the masterMotor
   *
   */
  @Deprecated
  protected PrefControllerSlaveSubsystem(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor, boolean inverted) {
    this(masterMotor, slaveMotor, inverted);
  }

  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name
   * @param name string to use as the Subsystem's name
   * @param masterMotor the motor to use in the subsystem
   * @param slaveMotor the motor to follow the master motor in the subsystem
   */
  @Deprecated
  protected PrefControllerSlaveSubsystem(
      String name, MasterMotorType masterMotor, SlaveMotorType slaveMotor) {
    this(masterMotor, slaveMotor);
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
    m_slaveMotor.resetMotorConfig();
    this.setFollowing(m_invertedSlave);
  }

  @Override
  protected Stream<Sendable> getSendables() {
    return Stream.concat(
            super.getSendables(), Stream.of(m_slaveMotor));
  }
}
