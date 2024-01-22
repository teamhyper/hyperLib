package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A command which constructs another command when it is initialized, and runs
 * that command. The wrapped command is run directly; that is, each method of
 * this command runs the corresponding method of the wrapped command. The
 * requirements of the wrapped command must be known statically, and passed in
 * the constructor.
 * 
 * @author James Hagborg
 *
 */
public class JustInTimeCommand extends Command {

	private final Supplier<Command> m_getCommand;
	private final Set<Subsystem> m_requirements;

	private Command m_command;

	public JustInTimeCommand(Supplier<Command> getCommand, Subsystem... requirements) {
		m_getCommand = Objects.requireNonNull(getCommand);
		m_requirements = Set.of(requirements);
	}

	public void initialize() {
		m_command = m_getCommand.get();
		m_command.initialize();
	}

	@Override
	public void execute() {
		m_command.execute();
	}

	@Override
	public void end(boolean interrupted) {
		m_command.end(interrupted);
	}

	@Override
	public boolean isFinished() {
		return m_command.isFinished();
	}

	@Override
	public Set<Subsystem> getRequirements() {
		return m_requirements;
	}

}
