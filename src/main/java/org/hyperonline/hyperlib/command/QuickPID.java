package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import org.hyperonline.hyperlib.pid.PIDControlled;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

public class QuickPID {
    /**
     * Constructs a command that uses a {@link PIDControlled} to control a subsystem. The pid
     * controller is enabled when the command starts and disabled when it ends or is interrupted.
     *
     * @param req      The subsystem to require
     * @param pid      The PID controller to use
     * @param setPoint The point to move the PID controller to
     * @param hold     Whether to continue running the PID loop after the target setpoint is
     *                 reached
     * @return The newly created {@link Command}
     */
    public static Command pidMove(Subsystem req, PIDControlled pid, double setPoint, boolean hold) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(setPoint);
                pid.enable();
            }

            @Override
            public void execute() {
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return !hold && pid.onTarget();
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that uses a {@link PIDControlled} to control a subsystem. The pid
     * controller is enabled when the command starts and disabled when it ends or is interrupted.
     *
     * @param req      The subsystem to require
     * @param pid      The PID controller to use
     * @param setPoint The point to move the PID controller to
     * @param stop     Whether to continue running the PID loop after the target setpoint is
     *                 reached
     * @return The newly created {@link Command}
     */
    public static Command pidMoveWithStop(
            Subsystem req, PIDControlled pid, double setPoint, BooleanSupplier stop) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(setPoint);
                pid.enable();
            }

            @Override
            public void execute() {
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return stop.getAsBoolean() && pid.onTarget();
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that uses a {@link PIDControlled} to control a subsystem. The pid
     * controller is enabled when the command starts and disabled when it ends or is interrupted.
     *
     * @param req      The subsystem to require
     * @param pid      The PID controller to use
     * @param setPoint The point to move the PID controller to
     * @param pause    Whether to continue running the PID loop after the target setpoint is
     *                 reached
     * @return The newly created {@link Command}
     */
    public static Command pidMoveWithPause(
            Subsystem req, PIDControlled pid, double setPoint, BooleanSupplier pause) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(setPoint);
                pid.enable();
            }

            @Override
            public void execute() {
                if (pause.getAsBoolean()) {
                    pid.disable();
                } else if (!pid.isEnabled()) {
                    pid.enable();
                }

                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return pid.onTarget(setPoint);
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that uses a {@link PIDControlled} to control a subsystem. The pid
     * controller is enabled when the command starts and disabled when it ends or is interrupted.
     *
     * @param req      The subsystem to require
     * @param pid      The PID controller to use
     * @param setPoint The point to move the PID controller to
     * @param hold     Whether to continue running the PID loop after the target setpoint is
     *                 reached
     * @return The newly created {@link Command}
     */
    public static Command pidMove(
            Subsystem req, PIDControlled pid, DoubleSupplier setPoint, boolean hold) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(setPoint.getAsDouble());
                pid.enable();
            }

            @Override
            public void execute() {
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return !hold && pid.onTarget();
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that uses a {@link PIDControlled} to control a subsystem. The pid
     * controller is enabled when the command starts and disabled when it ends or is interrupted.
     *
     * @param req      The subsystem to require
     * @param pid      The PID controller to use
     * @param setPoint The point to move the PID controller to
     * @param hold     Whether to continue running the PID loop after the target setpoint is
     *                 reached
     * @return The newly created {@link Command}
     */
    public static Command pidMoveContinuous(
            Subsystem req, PIDControlled pid, DoubleSupplier setPoint, boolean hold) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(setPoint.getAsDouble());
                pid.enable();
            }

            @Override
            public void execute() {
                pid.setSetpoint(setPoint.getAsDouble());
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return !hold && pid.onTarget();
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a Command that holds a {@link PIDControlled} at whatever the current input is. This
     * is useful in combination with a manual control system, where you want to move the system
     * manually but hold it in place using PID.
     *
     * @param req The Subsystem to require
     * @param pid The PIDController to use
     * @return The newly created command
     */
    public static Command pidHold(Subsystem req, PIDControlled pid) {
        return QuickPID.pidHold(req, pid, () -> false);
    }

    /**
     * Constructs a Command that holds a {@link PIDControlled} at whatever the current input is. This
     * is useful in combination with a manual control system, where you want to move the system
     * manually but hold it in place using PID.
     *
     * @param req       The Subsystem to require
     * @param pid       The PIDController to use
     * @param interrupt BooleanSupplier that can interrupt the pidHold command (like a limit switch)
     * @return The newly created command
     */
    public static Command pidHold(Subsystem req, PIDControlled pid, BooleanSupplier interrupt) {
        return new Command() {
            {
                if (req != null) addRequirements(req);
            }

            @Override
            public void initialize() {
                pid.setSetpoint(pid.getFromSource());
                pid.enable();
            }

            @Override
            public void execute() {
                if (interrupt.getAsBoolean()) {
                    pid.disable();
                }
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that moves a {@link PIDControlled} to be under a given target, only if the
     * current position is over that target.
     *
     * @param req    The subsystem to require
     * @param pid    The PID controller to use
     * @param target The point to move the PID controller under
     * @param hold   Whether to continue running the PID loop after the target is reached
     * @return The newly created {@link Command}
     */
    public static Command pidMoveUnder(
            Subsystem req, PIDControlled pid, double target, boolean hold) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {

                if (pid.isAbove(target)) {
                    pid.setSetpoint(target - pid.getTolerance());
                    pid.enable();
                }
            }

            @Override
            public void execute() {
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return !hold && (pid.isBelow(target) || pid.onTarget());
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that moves a {@link PIDControlled} to be over a given target, only if the
     * current position is under that target.
     *
     * @param req    The subsystem to require
     * @param pid    The PID controller to use
     * @param target The point to move the PID controller over
     * @param hold   Whether to continue running the PID loop after the target is reached
     * @return The newly created {@link Command}
     */
    public static Command pidMoveOver(Subsystem req, PIDControlled pid, double target, boolean hold) {
        return new Command() {
            {
                if (req != null) // not sure when we're going to use pid outside
                    // a subsystem lol
                    addRequirements(req);
            }

            @Override
            public void initialize() {

                if (pid.isBelow(target)) {
                    pid.setSetpoint(target + pid.getTolerance());
                    pid.enable();
                }
            }

            @Override
            public void execute() {
                pid.execute();
            }

            @Override
            public boolean isFinished() {
                return !hold && (pid.isAbove(target) || pid.onTarget());
            }

            @Override
            public void end(boolean interrupted) {
                pid.disable();
            }
        };
    }

    /**
     * Constructs a command that moves a {@link PIDControlled} to a given setpoint,
     * finishing when it is ontarget, or if the speed is positive and the forwardLimit is tripped,
     * or if the speed is negative and the reverseLimit is tripped
     *
     * @param req            The subsystem to require
     * @param pid            The PID controller to use
     * @param setPoint       The point to move the PID controller to
     * @param hold           Whether to continue running the PID loop after the target setpoint is reached
     * @param canMoveForward Can the PID move the motor forward (positive speed) with the given speed or end command
     * @param canMoveReverse Can the PID move the motor reverse (negative speed) with the given speed or end command
     * @return
     */
    public static Command pidMoveWithLimits(Subsystem req, PIDControlled pid, double setPoint, boolean hold, Predicate<DoubleSupplier> canMoveForward, Predicate<DoubleSupplier> canMoveReverse) {
        return new ParallelRaceGroup(
                QuickPID.pidMove(req, pid, setPoint, hold),
                new WaitUntilCommand(() -> !canMoveForward.test(pid::getSpeed) || !canMoveReverse.test(pid::getSpeed))
        );
    }

    /**
     * Constructs a command that moves a {@link PIDControlled} to a given setpoint,
     * finishing when it is ontarget, or if the speed is positive and the forwardLimit is tripped,
     * or if the speed is negative and the reverseLimit is tripped
     *
     * @param req            The subsystem to require
     * @param pid            The PID controller to use
     * @param setPoint       The point to move the PID controller to
     * @param hold           Whether to continue running the PID loop after the target setpoint is reached
     * @param canMoveForward Can the PID move the motor forward (positive speed) with the given speed or end command
     * @param canMoveReverse Can the PID move the motor reverse (negative speed) with the given speed or end command
     * @return
     */
    public static Command pidMoveWithLimits(Subsystem req, PIDControlled pid, DoubleSupplier setPoint, boolean hold, Predicate<DoubleSupplier> canMoveForward, Predicate<DoubleSupplier> canMoveReverse) {
        return QuickPID.pidMoveWithLimits(req, pid, setPoint.getAsDouble(), hold, canMoveForward, canMoveReverse);
    }
}
