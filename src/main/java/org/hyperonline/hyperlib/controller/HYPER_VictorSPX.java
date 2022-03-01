package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class HYPER_VictorSPX extends WPI_VictorSPX implements SendableMotorController {
  /**
   * Constructor for motor controller
   *
   * @param deviceNumber device ID of motor controller
   */
  public HYPER_VictorSPX(int deviceNumber) {
    super(deviceNumber);
  }
}
