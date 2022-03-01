package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

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
