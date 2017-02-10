package org.usfirst.frc.team69.util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A builder class to create commands in autonomous.  Currently, it wraps a
 * {@link CommandGroup}, but the implementation may change in future versions.
 * 
 * To build a command, add child commands using {@link #sequential(Command)}
 * and {@link #parallel(Command)}, then get a command using {@link #build()}.
 * 
 * @author James
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
     * @param name The name of the command to build
     */
    public CommandBuilder(String name) {
        m_cmdGroup = new CommandGroup(name);
    }
    
    /**
     * Add a command in sequence.  Execution of the next command will not
     * happen until this one finishes.
     * @param command The command to add in sequence
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command) {
        m_cmdGroup.addSequential(command);
        return this;
    }
    
    /**
     * Add a command in sequence.  Execution of the next command will not
     * happen until this one finishes, or until the timeout expires
     * @param command The command to add in sequence
     * @param timeout The timeout, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder sequential(Command command, double timeout) {
        m_cmdGroup.addSequential(command, timeout);
        return this;
    }
    
    /**
     * Add a command in parallel.  Execution of the next command will begin
     * immediately, while this one continues to execute in parallel.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command The command to add in parallel
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command) {
        m_cmdGroup.addParallel(command);
        return this;
    }
    
    /**
     * Add a command in parallel.  Execution of the next command will begin
     * immediately, while this one continues to execute in parallel.  The
     * command will be killed if the timeout expires.
     * 
     * Note that the command created will not end until all parallel commands
     * finish.
     * 
     * @param command The command to add in parallel
     * @param timeout The timeout, in seconds
     * @return This CommandBuilder object
     */
    public CommandBuilder parallel(Command command, double timeout) {
        m_cmdGroup.addParallel(command, timeout);
        return this;
    }
    
    /**
     * Build a command.  Note that for simplicity, calling this method 
     * multiple times will return the same {@link Command} object.  This
     * may change in the future, to match the behavior of other objects
     * following the builder pattern.
     * @return A command created from this builder
     */
    public Command build() {
        return m_cmdGroup;
    }
}
