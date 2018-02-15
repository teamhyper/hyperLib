package org.usfirst.frc.team69.util;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The {@link QuickCommand} class provides helper methods to make short
 * commands. This removes the need to make a new class for every small command,
 * and thus helps quick development and simplifies code.
 * <p>
 * If you want to make a command that runs once and finishes instantly, use
 * {@code oneShot}. If you want to make a command that runs continuously until
 * it is interrupted, use {@code continuous}. If you want to make a command that
 * runs until a specific condition is met, use {@code until} or {@code waitFor}.
 * <p>
 * For example, the following code makes a command that will call
 * {@code exampleSubsystem.doSomething()} once and then end:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	Command myCmd = QuickCommand.oneShot(Robot.exampleSubsystem, () -> Robot.exampleSubsystem.doSomething());
 * }
 * </pre>
 * 
 * For more information on the syntax used, look up Java lambda expressions.
 * <p>
 * Any more advanced features, like custom conditions for {@code isFinished},
 * compound commands, or commands that require multiple or no subsystems,
 * subclass {@link Command} in the usual way.
 * 
 * @author James
 *
 */
public class QuickCommand {
	/**
	 * Create a command that will run once and finish instantly.
	 * 
	 * @param req
	 *            The {@link Subsystem} that this command requires.
	 * @param body
	 *            The {@link Runnable} that will run once.
	 * @return The newly created {@link Command}
	 */
	public static Command oneShot(Subsystem req, Runnable body) {
		return new Command() {
			{
				if (req != null)
					this.requires(req);
			}

			@Override
			protected void initialize() {
				body.run();
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return true;
			}

			@Override
			protected void end() {
			}

			@Override
			protected void interrupted() {
			}
		};
	}

	/**
	 * Create a command that will run until interrupted.
	 * 
	 * @param req
	 *            The {@link Subsystem} that this command requires.
	 * @param body
	 *            The {@link Runnable} that will run repeatedly.
	 * @return The newly created {@link Command}
	 */
	public static Command continuous(Subsystem req, Runnable body) {
		return new Command() {
			{
				if (req != null)
					this.requires(req);
			}

			@Override
			protected void initialize() {
			}

			@Override
			protected void execute() {
				body.run();
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
			}

			@Override
			protected void interrupted() {
			}
		};
	}

	/**
	 * Create a command that will run until the given condition is met.
	 * 
	 * @param req
	 *            The {@link Subsystem} that this command requires.
	 * @param body
	 *            The {@link Runnable} that will run repeatedly.
	 * @param finished
	 *            The condition that signals the end of the command.
	 * @return The newly created {@link Command}
	 */
	public static Command runUntil(Subsystem req, Runnable body, BooleanSupplier finished) {
		return new Command() {
			{
				if (req != null)
					this.requires(req);
			}

			@Override
			protected void initialize() {
			}

			@Override
			protected void execute() {
				body.run();
			}

			@Override
			protected boolean isFinished() {
				return finished.getAsBoolean();
			}

			@Override
			protected void end() {
			}

			@Override
			protected void interrupted() {
			}
		};
	}

	/**
	 * Create a command that will run if the runnable condition is met, and if so
	 * until the finished condition is met.
	 * 
	 * @param req
	 *            The {@link Subsystem} that this command requires.
	 * @param body
	 *            The {@link Runnable} that will run repeatedly.
	 * @param finished
	 *            The condition that signals the end of the command.
	 * @param runnable
	 *            The condition that signals the command should be run
	 * @return The newly created {@link Command}
	 */
	public static Command runIf(Subsystem req, Runnable body, BooleanSupplier finished, BooleanSupplier runnable) {
		return new Command() {
			{
				if (req != null)
					this.requires(req);
			}

			@Override
			protected void initialize() {
			}

			@Override
			protected void execute() {
				if (runnable.getAsBoolean()) {
					body.run();
				}
			}

			@Override
			protected boolean isFinished() {
				return !runnable.getAsBoolean() || finished.getAsBoolean();
			}

			@Override
			protected void end() {
			}

			@Override
			protected void interrupted() {
			}
		};
	}

	/**
	 * A command that does not complete until the given condition is met. This can
	 * be useful when combined with {@link CommandGroup#addSequential(Command)} and
	 * {@link CommandGroup#addParallel(Command)}
	 * 
	 * @param condition
	 *            The condition that indicates the command is over
	 * @return The newly created {@link Command}
	 */
	public static Command waitFor(BooleanSupplier condition) {
		return new Command() {
			@Override
			protected void initialize() {
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return condition.getAsBoolean();
			}

			@Override
			protected void end() {
			}

			@Override
			protected void interrupted() {
			}
		};
	}

	/**
	 * Constructs a command that uses a {@link PIDController} to control a
	 * subsystem. The pid controller is enabled when the command starts and disabled
	 * when it ends or is interrupted.
	 * 
	 * @param req
	 *            The subsystem to require
	 * @param pid
	 *            The PID controller to use
	 * @param setPoint
	 *            The point to move the PID controller to
	 * @param hold
	 *            Whether or not to continue running the PID loop after the target
	 *            setpoint is reached
	 * @return The newly created {@link Command}
	 */
	public static Command pidMove(Subsystem req, PIDController pid, double setPoint, boolean hold) {
		return new Command() {
			{
				if (req != null) // not sure when we're going to use pid outside
									// a subsystem lol
					requires(req);
			}

			@Override
			protected void initialize() {
				pid.setSetpoint(setPoint);
				pid.enable();
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return !hold && pid.onTarget();
			}

			@Override
			protected void interrupted() {
				pid.disable();
			}

			@Override
			protected void end() {
				pid.disable();
			}
		};
	}
	/**
	 * Constructs a command that uses a {@link PIDController} to control a
	 * subsystem. The pid controller is enabled when the command starts and disabled
	 * when it ends or is interrupted.
	 * 
	 * @param req
	 *            The subsystem to require
	 * @param pid
	 *            The PID controller to use
	 * @param setPoint
	 *            The point to move the PID controller to
	 * @param hold
	 *            Whether or not to continue running the PID loop after the target
	 *            setpoint is reached
	 * @return The newly created {@link Command}
	 */
	public static Command pidMove(Subsystem req, PIDController pid, DoubleSupplier setPoint, boolean hold) {
		return new Command() {
			{
				if (req != null) // not sure when we're going to use pid outside
					// a subsystem lol
					requires(req);
			}

			@Override
			protected void initialize() {
				pid.enable();
			}

			@Override
			protected void execute() {
				pid.setSetpoint(setPoint.getAsDouble());
			}

			@Override
			protected boolean isFinished() {
				return !hold && pid.onTarget();
			}

			@Override
			protected void interrupted() {
				pid.disable();
			}

			@Override
			protected void end() {
				pid.disable();
			}
		};
	}

	/**
	 * Constructs a Command that holds a {@link PIDController} at whatever the
	 * current input is. This is useful in combination with a manual control system,
	 * where you want to move the system manually but hold it in place using PID.
	 * 
	 * @param req
	 *            The Subsystem to require
	 * @param pid
	 *            The PIDController to use
	 * @param source
	 *            The PIDSource (encoder, potentiometer, etc.) from which to take
	 *            the input
	 * @return The newly created command
	 */
	public static Command pidHold(Subsystem req, PIDController pid, PIDSource source) {
		return new Command() {
			{
				if (req != null)
					requires(req);
			}

			@Override
			protected void initialize() {
				pid.setSetpoint(source.pidGet());
				pid.enable();
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected void end() {
				pid.disable();
			}

			@Override
			protected void interrupted() {
				pid.disable();
			}
		};
	}

	/**
	 * Constructs a command that moves a {@link PIDController} to be under a given
	 * target, only if the current position is over that target.
	 * 
	 * @param req
	 *            The subsystem to require
	 * @param pid
	 *            The PID controller to use
	 * @param source
	 *            The PIDSource (encoder, potentiometer, etc.) from which to take
	 *            the input
	 * @param target
	 *            The point to move the PID controller under
	 * @param hold
	 *            Whether or not to continue running the PID loop after the target
	 *            is reached
	 * @return The newly created {@link Command}
	 */
	public static Command pidMoveUnder(Subsystem req, PIDController pid, PIDSource source, double target,
			boolean hold) {
		return new Command() {
			{
				if (req != null) // not sure when we're going to use pid outside
									// a subsystem lol
					requires(req);
			}

			@Override
			protected void initialize() {

				if (source.pidGet() > target) {
					pid.setSetpoint(target);
				}
				pid.enable();
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return !hold && pid.onTarget();
			}

			@Override
			protected void interrupted() {
				pid.disable();
			}

			@Override
			protected void end() {
				pid.disable();
			}
		};
	}

	/**
	 * Constructs a command that moves a {@link PIDController} to be over a given
	 * target, only if the current position is under that target.
	 * 
	 * @param req
	 *            The subsystem to require
	 * @param pid
	 *            The PID controller to use
	 * @param source
	 *            The PIDSource (encoder, potentiometer, etc.) from which to take
	 *            the input
	 * @param target
	 *            The point to move the PID controller over
	 * @param hold
	 *            Whether or not to continue running the PID loop after the target
	 *            is reached
	 * @return The newly created {@link Command}
	 */
	public static Command pidMoveOver(Subsystem req, PIDController pid, PIDSource source, double target, boolean hold) {
		return new Command() {
			{
				if (req != null) // not sure when we're going to use pid outside
									// a subsystem lol
					requires(req);
			}

			@Override
			protected void initialize() {

				if (source.pidGet() < target) {
					pid.setSetpoint(target);
				}
				pid.enable();
			}

			@Override
			protected void execute() {
			}

			@Override
			protected boolean isFinished() {
				return !hold && pid.onTarget();
			}

			@Override
			protected void interrupted() {
				pid.disable();
			}

			@Override
			protected void end() {
				pid.disable();
			}
		};
	}

	/**
	 * Create a command which releases control of a subsystem.
	 * 
	 * The {@link Command} returned by this method will require the given subsystem,
	 * and end instantly. This has the effect of stopping any command using the
	 * given subsystem, and of starting the default command, if one exists.
	 * 
	 * @param subsystem
	 *            The subsystem to release
	 * @return A command which releases the subsystem
	 */
	public static Command release(Subsystem subsystem) {
		if (subsystem == null) {
			throw new NullPointerException("subsystem == null");
		}

		return new Command() {
			{
				requires(subsystem);
			}

			@Override
			protected boolean isFinished() {
				return true;
			}
		};
	}
}
