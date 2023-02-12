package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import java.util.function.DoubleConsumer;

/**
 * wrapper for added behavior on the {@link WPI_VictorSPX}
 *
 * <strong>added behavior</strong>
 * <ul>
 *   <li>automatically add datapoints to LiveWindow</li>
 * </ul>
 *
 * @author Chris McGroarty
 */
public class HYPER_VictorSPX extends WPI_VictorSPX implements SendableMotorController {
  public DoubleConsumer consumeSpeed = speed -> this.set(speed);

  /**
   * Constructor for motor controller
   *
   * @param deviceNumber device ID of motor controller
   */
  public HYPER_VictorSPX(int deviceNumber) {
    super(deviceNumber);
  }

  @Override
  public void resetMotorConfig() {
    this.configFactoryDefault();
  }
}
