package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

import java.util.function.BooleanSupplier;

public class ConditionalInterruptCommand extends ConditionalCommand {

  /**
   * Creates a new ConditionalCommand.
   *
   * @param onTrue the command to run if the condition is true
   * @param onFalse the command to run if the condition is false
   * @param condition the condition to determine which command to run
   */
  public ConditionalInterruptCommand(Command onTrue, Command onFalse, BooleanSupplier condition) {
    super(
        onTrue.withInterrupt(() -> !condition.getAsBoolean()),
        onFalse.withInterrupt(condition),
        condition);
  }
}
