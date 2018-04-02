package org.usfirst.frc.team69.util;

import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.GetRequirements;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.command.WaitForChildren;
import edu.wpi.first.wpilibj.command.WaitUntilCommand;
import edu.wpi.first.wpilibj.command.WhileCommand;

/**
 * A builder class to create commands in autonomous. Currently, it wraps a
 * {@link CommandGroup}, but the implementation may change in future versions.
 * 
 * To build a command, add child commands using {@link #sequential(Command)} and
 * {@link #parallel(Command)}, then get a command using {@link #build()}.
 * 
 * @author James Hagborg
 *
 */
public class CommandBuilder {
    private final CommandGroup m_cmdGroup;

    /**
     * Construct a new CommandBuilder
     */
    public CommandBuilder() {
        m_cmdGroup = new CommandGroup();
    }

    /**
     * Construct a new CommandBuilder with a given name
     * 
     * @param name
     *            The name of the command to build
     */
    public CommandBuilder(String name) {
        m_cmdGroup = new CommandGroup(name);
    }

    /**
     * Add a command in sequence. Execution of the next command will not happen
     * until this one finishes.
     * 
     * @param command
     *            The command to add in sequence
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command) {
        m_cmdGroup.addSequential(command);
        return this;
    }

    /**
     * Add a command in sequence. Execution of the next command will not happen
     * until this one finishes, or until the timeout expires
     * 
     * @param command
     *            The command to add in sequence
     * @param timeout
     *            The timeout, in seconds. if negative, no timeout is used.
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command, double timeout) {
    	if(timeout >= 0) {
    		m_cmdGroup.addSequential(command, timeout);
    	} else {
    		m_cmdGroup.addSequential(command);
    	}
        
        return this;
    }

    /**
     * Add a command in parallel. Execution of the next command will begin
     * immediately, while this one continues to execute in parallel.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command
     *            The command to add in parallel
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command) {
        m_cmdGroup.addParallel(command);
        return this;
    }

    /**
     * Add a command in parallel. Execution of the next command will begin
     * immediately, while this one continues to execute in parallel. The command
     * will be killed if the timeout expires.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command
     *            The command to add in parallel
     * @param timeout
     *            The timeout, in seconds. if negative, no timeout is used.
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command, double timeout) {
    	if(timeout >= 0) {
    		m_cmdGroup.addParallel(command, timeout);
    	} else {
    		m_cmdGroup.addParallel(command);
    	}        
        return this;
    }

    /**
     * Add a sequence of commands in parallel. Execution of the next command
     * will begin immediately, while this one continues to execute in parallel.
     * The command will be killed if the timeout expires.
     * 
     * @param command
     *            A function taking a CommandBuilder specifying the sequence of
     *            commands
     * @return {CommandBuilder}
     */
    public CommandBuilder parallel(Consumer<CommandBuilder> command) {
        CommandBuilder child = new CommandBuilder();
        command.accept(child);
        m_cmdGroup.addParallel(child.build());
        return this;
    }

    /**
     * Wait for a given duration.
     * 
     * @param seconds
     *            The time to wait, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForDuration(double seconds) {
        m_cmdGroup.addSequential(new WaitCommand(seconds));
        return this;
    }

    /**
     * Wait until a given match time.
     * 
     * @param seconds
     *            The match time to wait for, in seconds from the start
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForMatchTime(double seconds) {
        m_cmdGroup.addSequential(new WaitUntilCommand(seconds));
        return this;
    }

    /**
     * Wait until an arbitrary condition is met.
     * 
     * @param condition
     *            The condition to wait for.
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForCondition(BooleanSupplier condition) {
        m_cmdGroup.addSequential(QuickCommand.waitFor(condition));
        return this;
    }

    /**
     * Wait until an arbitrary condition is met, or until a timeout expires.
     * 
     * @param condition
     *            The condition to wait for
     * @param timeout
     *            The timeout in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForCondition(BooleanSupplier condition, double timeout) {
        m_cmdGroup.addSequential(QuickCommand.waitFor(condition), timeout);
        return this;
    }

    /**
     * Run a command if and only if a condition is met at runtime.
     * 
     * @param condition
     *            The condition to check
     * @param ifTrue
     *            The command to run if the condition is true
     * @return This CommandBuilder object
     */
    public CommandBuilder ifThen(BooleanSupplier condition, Command ifTrue) {
        return ifThenElse(condition, ifTrue, new InstantCommand());
    }

    /**
     * Run one of two commands, depending on if a condition is true.
     * 
     * @param condition
     *            The condition to check
     * @param ifTrue
     *            The command to run if the condition is true
     * @param ifFalse
     *            The command to run if the condition is false
     * @return This CommandBuilder object
     */
    public CommandBuilder ifThenElse(BooleanSupplier condition, Command ifTrue, Command ifFalse) {
        m_cmdGroup.addSequential(new ConditionalCommand(ifTrue, ifFalse) {
            @Override
            protected boolean condition() {
                return condition.getAsBoolean();
            }
        });
        return this;
    }

    /**
     * Loop repeatedly while checking a condition.
     * 
     * Given a command as a body and a condition to check, this command will
     * repeatedly check the condition, and execute the command if it is met.
     * 
     * The requirements of the body command are removed when this method is
     * called. This means you cannot re-use the command object you pass to this
     * method. This allows CommandGroups and cancelling to work properly.
     * 
     * Because HyperLib is currently in pre-release, these semantics may change
     * based on what we consider "reasonable defaults".
     * 
     * @param condition
     *            The condition to check
     * @param body
     *            The command to run as the loop body
     * @return This CommandBuilder object
     * @see WhileCommand
     */
    public CommandBuilder whileLoop(BooleanSupplier condition, Command body) {
        m_cmdGroup.addSequential(new WhileCommand(condition, body));
        return this;
    }

    // This needs to be a class, so we can pass-by-reference to the loop command
    private static class Counter {
        public int value = 0;
    }

    /**
     * Run a command a given number of times. This is effectively a for loop,
     * although the command cannot make use of the value of the counter.
     * 
     * This uses {@link #whileLoop(BooleanSupplier, Command)} under the hood, so
     * the semantics are the same as that.
     * 
     * @param count
     *            The number of times to run the command.
     * @param body
     *            The body of the command to execute.
     * @return This CommandBuilder object
     */
    public CommandBuilder forLoop(int count, Command body) {
        Counter counter = new Counter();
        m_cmdGroup.addSequential(QuickCommand.oneShot(null, () -> counter.value = 0));
        m_cmdGroup.addSequential(new WhileCommand(() -> counter.value++ < count, body));
        return this;
    }

    /**
     * End any command currently running on the subsystem. This is accomplished
     * by running a command which ends instantly, which requires the given
     * subsystem. Afterwards, the default command will run, if there is one.
     * 
     * @param subsystem
     *            The subsystem to release
     * @return This CommandBuilder object
     */
    public CommandBuilder release(Subsystem subsystem) {
        m_cmdGroup.addSequential(QuickCommand.release(subsystem));
        return this;
    }

    /**
     * End all commands currently running in parallel. This is accomplished by
     * starting an command which requires all the subsystems used so far, which
     * ends instantly. This will have the effect of starting the default
     * commands for all these subsystems, if they have any.
     * 
     * @return This CommandBuilder object
     */
    @SuppressWarnings("unchecked")
    public CommandBuilder releaseAll() {
        m_cmdGroup.addSequential(new InstantCommand() {
            {
                for (Object req : Collections.list(GetRequirements.getRequirements(m_cmdGroup))) {
                    requires((Subsystem) req);
                }
            }
        });
        return this;
    }

    /**
     * Wait for all running parallel commands to finish before executing the
     * next command.
     * 
     * @return This CommandBuilder object
     */
    public CommandBuilder waitForChildren() {
        m_cmdGroup.addSequential(new WaitForChildren());
        return this;
    }

    /**
     * Build a command. Note that for simplicity, calling this method multiple
     * times will return the same {@link Command} object. This may change in the
     * future, to match the behavior of other objects following the builder
     * pattern.
     * 
     * @return A command created from this builder
     */
    public Command build() {
        return m_cmdGroup;
    }
}
