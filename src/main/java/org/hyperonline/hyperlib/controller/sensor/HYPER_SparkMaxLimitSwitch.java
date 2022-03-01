package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class HYPER_SparkMaxLimitSwitch implements Sendable {
  public final SparkMaxLimitSwitch limitSwitch;

  public HYPER_SparkMaxLimitSwitch(SparkMaxLimitSwitch limitSwitch) {
    this.limitSwitch = limitSwitch;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("SparkMaxLimitSwitch");
    builder.addBooleanProperty("Is Pressed", limitSwitch::isPressed, null);
  }
}
