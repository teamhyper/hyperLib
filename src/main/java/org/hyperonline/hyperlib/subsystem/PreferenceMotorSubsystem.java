package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

abstract class PreferenceMotorSubsystem<MotorType extends Sendable> extends PreferenceSubsystem {

    protected MotorType m_motor;

    public PreferenceMotorSubsystem() {
        super(null);
    }

    @Override
    public void initPreferences() {
    }

    public PreferenceMotorSubsystem(String name, MotorType motor) {
        super(name);
        m_motor = motor;
        this.addChild("Motor", m_motor);
        this.setDefaultCommand(this.stopCmd());
    }

    /**
     * @return continuous {@link Command} to move the motor forward (positive voltage)
     */
    public Command forwardCmd() {
        return new RunCommand(this::forward, this);
    }

    /**
     * @return continuous {@link Command} to move the motor reverse (negative voltage)
     */
    public Command reverseCmd() {
        return new RunCommand(this::reverse, this);
    }

    /**
     * @return continuous {@link Command} stopping the motor (speed 0)
     */

    public Command stopCmd() {
        return new RunCommand(this::stop, this);
    }

    /**
     * move forward only if the given condition is satisfied, else stop the motor
     *
     * @param condition should the motor be driven forwards or stopped
     * @return {@link Command} that moves the motor at its configured (preference) forward speed if
     * the condition is true
     */
    public Command conditionalForwardCmd(BooleanSupplier condition) {
        return new FunctionalCommand(
                () -> {
                },
                () -> {
                    if (condition.getAsBoolean()) {
                        forward();
                    } else {
                        stop();
                    }
                },
                interrupted -> stop(),
                () -> false,
                this);
    }


    /**
     * move reverse only if the given condition is satisfied, else stop the motor
     *
     * @param condition should the motor be driven reverse or stopped
     * @return {@link Command} that moves the motor at its configured (preference) reverse speed if
     * the condition is true
     */
    public Command conditionalReverseCmd(BooleanSupplier condition) {
        return new FunctionalCommand(
                () -> {
                },
                () -> {
                    if (condition.getAsBoolean()) {
                        reverse();
                    } else {
                        stop();
                    }
                },
                interrupted -> stop(),
                () -> false,
                this);
    }

    public abstract void forward();

    public abstract void reverse();

    public abstract void stop();

    protected abstract void configMotor();

    /**
     * helper for organizing sensor configuration
     */
    protected void configSensors() {
    }

    /**
     * helper for organizing PID configuration
     */
    protected void configPID() {
    }

    protected Stream<Sendable> getSendables() {
        return Stream.concat(
                super.getSendables(), Stream.of(m_motor));
    }

    public void resetPositionSensor() {
    }

    public Command resetPositionSensorCmd() {
        return new InstantCommand(this::resetPositionSensor);
    }

    public boolean isAtForwardLimit() {
        return false;
    }


    public boolean canMoveForward() {
        return !isAtForwardLimit();
    }

    public boolean canMoveReverse() {
        return !isAtReverseLimit();
    }

    public boolean isAtReverseLimit() {
        return false;
    }

    public Command resetPositionSensorAtReverseLimitCmd() {
        return new RunCommand(() -> {
            if (isAtReverseLimit()) {
                this.resetPositionSensor();
            }
        });
    }

    public Command resetPositionSensorAtForwardLimitCmd() {
        return new RunCommand(() -> {
            if (isAtForwardLimit()) {
                this.resetPositionSensor();
            }
        });
    }

    public Command moveAllWayReverseCmd() {
        return this.reverseCmd().until(this::isAtReverseLimit);
    }

    public Command moveAllWayForwardCmd() {
        return this.forwardCmd().until(this::isAtForwardLimit);
    }
}
