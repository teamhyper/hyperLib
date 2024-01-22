package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.REVLibError;
import com.revrobotics.SparkAnalogSensor;
import com.revrobotics.SparkMaxAnalogSensor;
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
public record HYPER_SparkMaxAnalogSensor(SparkAnalogSensor analog) implements HYPER_CANSensorSendable {

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMaxAnalogSensor");
    builder.addDoubleProperty("Position", this::getPosition, null);
    builder.addDoubleProperty("Velocity", this::getVelocity, null);
    builder.addDoubleProperty("Voltage", this::getVoltage, null);
  }

  public MotorFeedbackSensor getSensor() {
    return analog;
  }

  @Override
  public double getPosition() {
    return analog.getPosition();
  }

  @Override
  public double getVelocity() {
    return analog.getVelocity();
  }

  public double getVoltage() {
    return analog.getVoltage();
  }

  @Override
  public REVLibError setPositionConversionFactor(double factor) {
    return analog.setPositionConversionFactor(factor);
  }

  @Override
  public REVLibError setVelocityConversionFactor(double factor) {
    return analog.setVelocityConversionFactor(factor);
  }

  @Override
  public double getPositionConversionFactor() {
    return analog.getPositionConversionFactor();
  }

  @Override
  public double getVelocityConversionFactor() {
    return analog.getVelocityConversionFactor();
  }

  @Override
  public REVLibError setInverted(boolean inverted) {
    return analog.setInverted(inverted);
  }

  @Override
  public boolean getInverted() {
    return analog.getInverted();
  }
}
