package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.SparkMaxAnalogSensor;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

/**
 * wrapper for added behavior on the {@link SparkMaxAnalogSensor}.
 *
 * <strong>added behavior</strong>
 * <ul>
 *     <li>make the SparkMaxAnalogSensor sendable for use with shuffleboard</li>
 *     <li>automatically add datapoints to LiveWindow</li>
 * </ul>
 *
 * @author Chris McGroarty
 */
public class HYPER_SparkMaxAnalogSensor implements Sendable {
  public final com.revrobotics.SparkMaxAnalogSensor analog;

  public HYPER_SparkMaxAnalogSensor(SparkMaxAnalogSensor analog) {
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
