package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.function.DoubleConsumer;

/**
 * wrapper for added behavior on the {@link WPI_TalonSRX}
 *
 * <strong>added behavior</strong> *
 * <ul>
 *   <li>automatically add datapoints to LiveWindow</li>
 * </ul>
 *
 * @author Chris McGroarty
 */
public class HYPER_TalonSRX extends WPI_TalonSRX implements RawController {

  private final boolean m_useSensor;

  public DoubleConsumer consumeSpeed = speed -> this.set(speed);

  /**
   * Constructor for motor controller
   *
   * @param deviceNumber device ID of motor controller
   * @param useSensor should the sensor values be pushed to LiveWindow
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

  @Override
  public void resetMotorConfig() {
    this.configFactoryDefault();
    this.clearStickyFaults();
  }
}
