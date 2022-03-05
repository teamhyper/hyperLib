package org.hyperonline.hyperlib.controller.sensor;

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
public class HYPER_SparkMaxRelativeEncoder implements Sendable {
  public final RelativeEncoder encoder;

  public HYPER_SparkMaxRelativeEncoder(RelativeEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMaxRelativeEncoder");
    builder.addDoubleProperty("Position", encoder::getPosition, null);
    builder.addDoubleProperty("Velocity", encoder::getVelocity, null);
  }
}
