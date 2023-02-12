package org.hyperonline.hyperlib.controller;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface SendableMotorController extends MotorController, Sendable {
  void setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode mode);

  void resetMotorConfig();
}
