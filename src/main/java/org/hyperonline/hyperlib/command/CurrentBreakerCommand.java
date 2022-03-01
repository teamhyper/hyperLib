package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.hyperonline.hyperlib.controller.CANSparkMaxHYPER;

public class CurrentBreakerCommand extends CommandBase {
  CANSparkMaxHYPER m_motor;
  double m_currLimit;

  public CurrentBreakerCommand(CANSparkMaxHYPER motor, double limit) {
    m_motor = motor;
    m_currLimit = limit;
  }

  @Override
  public boolean isFinished() {
    return m_motor.getOutputCurrent() >= m_currLimit;
  }
}
