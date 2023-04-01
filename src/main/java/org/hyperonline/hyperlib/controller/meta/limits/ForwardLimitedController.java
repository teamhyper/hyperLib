package org.hyperonline.hyperlib.controller.meta.limits;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.hyperonline.hyperlib.controller.meta.MetaController;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Class that limits a {@link SendableMotorController} to only run until a forward limit is reached.
 * Useful for running a subsystem until a limit switch is tripped.
 *
 * @author Dheeraj Prakash
 */
public class ForwardLimitedController<T extends SendableMotorController> implements MetaController<T> {

    /**
     * The controller being used
     */
    private final T controller;

    /**
     * The limit switch to stop at
     */
    private final DigitalInput limit;

    /**
     * The speed preference for the controller to be set to
     */
    private final DoublePreference forwardSpeed;

    /**
     * The subsystem the controller is being attached to
     */
    private Subsystem subsystem;


    /**
     * Construct a new {@link ForwardLimitedController} from the given information.
     * @param controller the controller to limit
     * @param limit the forward limit switch
     * @param forwardSpeed the speed preference to move at
     */
    public ForwardLimitedController(T controller, DigitalInput limit, DoublePreference forwardSpeed) {
        this.controller = controller;
        this.limit = limit;
        this.forwardSpeed = forwardSpeed;
    }

    public void setSubsystem(Subsystem subsystem) {
        if (this.subsystem != null) throw new UnsupportedOperationException("Cannot replace already set subsystem.");
        this.subsystem = subsystem;
    }

    /**
     * Manually move at a given speed.
     * @param speed the speed to move at
     */
    public void move(double speed) {
        if (this.canMoveForward()) controller.set(speed);
    }

    /**
     * Manually move at a given speed
     * @param speed function returning the speed to move at
     */
    public void move(DoubleSupplier speed) {
        this.move(speed.getAsDouble());
    }

    /**
     * Stop the motor.
     * Sets the power to 0.
     */
    public void stop() {
        this.move(0);
    }

    /**
     * Move forward at a speed proportional to the preference
     * @param multiplier speed multiplier for the preference
     */
    public void forward(double multiplier) {
        this.move(forwardSpeed.get() * multiplier);
    }

    /**
     * Move forward at a speed proportional to the preference
     * @param multiplier function returning speed multiplier for the preference
     */
    public void forward(DoubleSupplier multiplier) {
        this.forward(multiplier.getAsDouble());
    }

    /**
     * Move forward at the speed preference
     */
    public void forward() {
        this.forward(1);
    }

    /**
     * Command wrapping {@link #forward(double)}.
     * Will end when limit is reached.
     * @param multiplier speed multiplier for the preference
     * @return Command
     */
    public Command forwardCmd(double multiplier) {
        return new RunCommand(() -> this.forward(multiplier), subsystem).until(this::atForwardLimit);
    }

    /**
     * Command wrapping {@link #forward(DoubleSupplier)}.
     * Will end when limit is reached.
     * @param multiplier function teturning speed multiplier for the preference
     * @return Command
     */
    public Command forwardCmd(DoubleSupplier multiplier) {
        return this.forwardCmd(multiplier.getAsDouble());
    }

    /**
     * Command wrapping {@link #forward()}.
     * Will end when limit is reached.
     * @return Command
     */
    public Command forwardCmd() {
        return this.forwardCmd(1);
    }

    /**
     * Only move forward when the given predicate returns true.
     * @param condition should move forward?
     * @param multiplier speed multiplier for the preference
     * @return Command
     */
    public Command conditionalForwardCmd(BooleanSupplier condition, double multiplier) {
        return new FunctionalCommand(
                () -> {},
                () -> {
                    if (condition.getAsBoolean()) this.forward(multiplier);
                    else this.stop();
                },
                (interrupted) -> this.stop(),
                this::atForwardLimit,
                subsystem
        );
    }

    /**
     * Only move forward when the given predicate returns true.
     * @param condition should move forward?
     * @param multiplier function returning speed multiplier for the preference
     * @return Command
     */
    public Command conditionalForwardCmd(BooleanSupplier condition, DoubleSupplier multiplier) {
        return this.conditionalForwardCmd(condition, multiplier.getAsDouble());
    }

    /**
     * Only move forward when the given predicate returns true.
     * @param condition should move forward?
     * @return Command
     */
    public Command conditionalForwardCmd(BooleanSupplier condition) {
        return this.conditionalForwardCmd(condition, 1);
    }

    /**
     * Has the limit been reached?
     * @return true if the limit has been reached
     */
    public boolean atForwardLimit() {
        return !limit.get();
    }

    /**
     * Can we move forward?
     * Inversion of {@link #atForwardLimit()}
     * @return true if we can move forward
     */
    public boolean canMoveForward() {
        return !this.atForwardLimit();
    }

    /**
     * Get limit source.
     * @return DigitalInput of the limit switch
     */
    public DigitalInput getLimit() {
        return limit;
    }

    /**
     * Get the forward speed preference
     * @return the preference
     */
    public DoublePreference getForwardSpeed() {
        return forwardSpeed;
    }

    /**
     * Get the subsystem the controller belongs to.
     * @return Subsystem
     */
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

    /**
     * Get motor controller being used.
     * @return the controller
     */
    @Override
    public T getController() {
        return controller;
    }
}
