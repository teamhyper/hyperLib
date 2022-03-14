package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * @deprecated use {@link RioPID} with an output instead
 */
@Deprecated
public class RioActivePID extends RioPID {

  public RioActivePID(
      String name,
      DoubleSupplier source,
      DoubleConsumer output,
      double Kp,
      double Ki,
      double Kd,
      double tolerance) {
    super(name, source, output, Kp, Ki, Kd, tolerance);
  }
}
