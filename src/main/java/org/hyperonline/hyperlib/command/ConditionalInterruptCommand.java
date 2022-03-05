package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

import java.util.function.BooleanSupplier;


/**
 * extend {@link ConditionalCommand} so it interrupts its running command when the boolean value changes
 * a normal ConditionalCommand provided with continuous running commands does not re-revaluate the BooleanSupplier when it changes
 * so an active running ConditionalCommand will continue executing the chosen command (based on the initial BooleanSupplier value)
 * this allows us to automatically switch running command when the BooleanSupplier value changes
 *
 * @author Chris McGroarty
 */
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
