package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

/**
 * wrapper for added behavior on {@link RelativeEncoder}.
 *
 * <strong>added behavior</strong>
 * <ul>
 *     <li>make a {@link com.revrobotics.SparkMaxRelativeEncoder} or {@link com.revrobotics.SparkMaxAlternateEncoder} sendable for use with shuffleboard</li>
 *     <li>automatically add datapoints to LiveWindow</li>
 * </ul>
 *
 * @author Chris McGroarty
 */
public class HYPER_SparkMaxRelativeEncoder implements HYPER_CANSensorSendable {
  public final RelativeEncoder encoder;

  public HYPER_SparkMaxRelativeEncoder(RelativeEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMaxRelativeEncoder");
    builder.addDoubleProperty("Position", this::getPosition, null);
    builder.addDoubleProperty("Velocity", this::getVelocity, null);
  }

  public MotorFeedbackSensor getSensor() {
    return encoder;
  }

  @Override
  public double getPosition() {
    return encoder.getPosition();
  }

  @Override
  public double getVelocity() {
    return encoder.getVelocity();
  }

  @Override
  public REVLibError setPositionConversionFactor(double factor) {
    return encoder.setPositionConversionFactor(factor);
  }

  @Override
  public REVLibError setVelocityConversionFactor(double factor) {
    return encoder.setVelocityConversionFactor(factor);
  }

  @Override
  public double getPositionConversionFactor() {
    return encoder.getPositionConversionFactor();
  }

  @Override
  public double getVelocityConversionFactor() {
    return encoder.getVelocityConversionFactor();
  }

  @Override
  public REVLibError setInverted(boolean inverted) {
    return encoder.setInverted(inverted);
  }

  @Override
  public boolean getInverted() {
    return encoder.getInverted();
  }
}
