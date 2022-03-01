package org.hyperonline.hyperlib.controller.sensor;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class HYPER_SparkMaxAnalogSensor implements Sendable {
  public final com.revrobotics.SparkMaxAnalogSensor analog;

  public HYPER_SparkMaxAnalogSensor(com.revrobotics.SparkMaxAnalogSensor analog) {
    this.analog = analog;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMaxAnalogSensor");
    builder.addDoubleProperty("Position", analog::getPosition, null);
    builder.addDoubleProperty("Velocity", analog::getVelocity, null);
    builder.addDoubleProperty("Voltage", analog::getVoltage, null);
  }
}
