package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import java.util.function.DoubleConsumer;

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
}
