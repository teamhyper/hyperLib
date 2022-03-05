package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

/**
 * wrapper for added behavior on the {@link SparkMaxLimitSwitch}.
 *
 * <strong>added behavior</strong>
 * <ul>
 *     <li>make the SparkMaxLimitSwitch sendable for use with shuffleboard</li>
 *     <li>automatically add datapoints to LiveWindow</li>
 * </ul>
 *
 * @author Chris McGroarty
 */
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
