package org.hyperonline.hyperlib.controller.limits;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Class that limits a {@link SendableMotorController} to only run until a reverse limit is reached.
 * Useful for running a subsystem until a limit switch is tripped.
 *
 * @author Dheeraj Prakash
 *
 * TODO: write docs for methods (copy from ForwardLimitedController)
 */
public class ReverseLimitedController<T extends SendableMotorController> implements SendableMotorController {
    private final T controller;
    private final DigitalInput limit;
    private final DoublePreference reverseSpeed;
    private Subsystem subsystem;

    public ReverseLimitedController(T controller, DigitalInput limit, DoublePreference forwardSpeed) {
        this.controller = controller;
        this.limit = limit;
        this.reverseSpeed = forwardSpeed;
    }

    public void setSubsystem(Subsystem subsystem) {
        if (this.subsystem != null) throw new UnsupportedOperationException("Cannot replace already set subsystem.");
        this.subsystem = subsystem;
    }

    public void move(double speed) {
        if (this.canMoveReverse()) controller.set(speed);
    }

    public void move(DoubleSupplier speed) {
        this.move(speed.getAsDouble());
    }

    public void stop() {
        this.move(0);
    }

    public void reverse(double multiplier) {
        this.move(reverseSpeed.get() * multiplier);
    }

    public void reverse(DoubleSupplier multiplier) {
        this.reverse(multiplier.getAsDouble());
    }

    public void reverse() {
        this.reverse(1);
    }

    public Command reverseCmd(double multiplier) {
        return new RunCommand(() -> this.reverse(multiplier), subsystem).until(this::atReverseLimit);
    }

    public Command reverseCmd(DoubleSupplier multiplier) {
        return this.reverseCmd(multiplier.getAsDouble());
    }

    public Command reverseCmd() {
        return this.reverseCmd(1);
    }

    public Command conditionalReverseCmd(BooleanSupplier condition, double multiplier) {
        return new FunctionalCommand(
                () -> {},
                () -> {
                    if (condition.getAsBoolean()) this.reverse(multiplier);
                    else this.stop();
                },
                (interrupted) -> this.stop(),
                this::atReverseLimit,
                subsystem
        );
    }

    public Command conditionalReverseCmd(BooleanSupplier condition, DoubleSupplier multiplier) {
        return this.conditionalReverseCmd(condition, multiplier.getAsDouble());
    }

    public Command conditionalReverseCmd(BooleanSupplier condition) {
        return this.conditionalReverseCmd(condition, 1);
    }

    public boolean atReverseLimit() {
        return !limit.get();
    }

    public boolean canMoveReverse() {
        return !this.atReverseLimit();
    }

    public T getController() {
        return controller;
    }

    public DigitalInput getLimit() {
        return limit;
    }

    public DoublePreference getReverseSpeed() {
        return reverseSpeed;
    }

    public Subsystem getSubsystem() {
        return subsystem;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        controller.setNeutralMode(mode);
    }

    @Override
    public void resetMotorConfig() {
        controller.resetMotorConfig();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        controller.initSendable(builder);
    }

    @Override
    public void set(double speed) {
        controller.set(speed);
    }

    @Override
    public double get() {
        return controller.get();
    }

    @Override
    public void setInverted(boolean isInverted) {
        controller.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return controller.getInverted();
    }

    @Override
    public void disable() {
        controller.disable();
    }

    @Override
    public void stopMotor() {
        controller.stopMotor();
    }
}
