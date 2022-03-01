package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.util.sendable.SendableBuilder;

public class HYPER_TalonSRX extends WPI_TalonSRX implements SendableMotorController {

  private final boolean m_useSensor;

  /**
   * Constructor for motor controller
   *
   * @param deviceNumber device ID of motor controller
   */
  public HYPER_TalonSRX(int deviceNumber, boolean useSensor) {
    super(deviceNumber);
    m_useSensor = useSensor;
  }

  public HYPER_TalonSRX(int deviceNumber) {
    this(deviceNumber, false);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    if (m_useSensor) {
      builder.addDoubleProperty("Position", this::getSelectedSensorPosition, null);
      builder.addDoubleProperty("Velocity", this::getSelectedSensorVelocity, null);
    }
  }
}
