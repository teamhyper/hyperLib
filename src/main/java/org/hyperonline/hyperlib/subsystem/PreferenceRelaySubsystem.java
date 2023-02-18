package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public abstract class PreferenceRelaySubsystem extends PreferenceMotorSubsystem {
    protected Relay m_motor;

    public PreferenceRelaySubsystem(Relay motor) {
        super();
        m_motor = motor;
        this.addChild("Motor", m_motor);
        this.setDefaultCommand(this.stopCmd());
    }

    /**
     * @return continuous {@link Command} to move the motor forward (positive voltage)
     */
    @Override
    public Command forwardCmd() {
        return new RunCommand(this::forward, this);
    }

    /**
     * @return continuous {@link Command} to move the motor backward (negative voltage)
     */
    @Override
    public Command backwardCmd() {
        return new RunCommand(this::backward, this);
    }

    /**
     * @return continuous {@link Command} stopping the motor (speed 0)
     */
    @Override
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
    @Override
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
     * move backward only if the given condition is satisfied, else stop the motor
     *
     * @param condition should the motor be driven backwards or stopped
     * @return {@link Command} that moves the motor at its configured (preference) backward speed if
     * the condition is true
     */
    @Override
    public Command conditionalBackwardCmd(BooleanSupplier condition) {
        return new FunctionalCommand(
                () -> {
                },
                () -> {
                    if (condition.getAsBoolean()) {
                        backward();
                    } else {
                        stop();
                    }
                },
                interrupted -> stop(),
                () -> false,
                this);
    }


    @Override
    public void forward() {
        m_motor.set(Relay.Value.kForward);
    }

    @Override
    public void backward() {
        m_motor.set(Relay.Value.kReverse);
    }

    @Override
    public void stop() {
        m_motor.set(Relay.Value.kOff);
    }

    @Override
    protected void configMotor() {

    }

    @Override
    protected void configSensors() {

    }

    @Override
    protected void configPID() {

    }

    @Override
    public void initPreferences() {

    }

    @Override
    public Stream<Sendable> getSendables() {
        return Stream.concat(
                super.getSendables(), Stream.of(m_motor));
    }
}
