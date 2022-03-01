package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class RioActivePID extends RioPassivePID {
  private DoubleConsumer m_output;

  public RioActivePID(
      String name,
      DoubleSupplier source,
      DoubleConsumer output,
      double Kp,
      double Ki,
      double Kd,
      double tolerance) {
    super(name, source, Kp, Ki, Kd, tolerance);
    m_output = output;
  }

  @Override
  public void execute() {
    if (m_enabled) {
      m_output.accept(calculate(getFromSource()));
    } else {
      m_output.accept(0);
    }
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("RIO Active PID");
  }
}
