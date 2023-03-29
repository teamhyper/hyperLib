package org.hyperonline.hyperlib.controller.limits;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Simple wrapper over {@link ForwardLimitedController} and {@link ReverseLimitedController} in one class.
 * Offers marginal functionality over having both individually.
 *
 * @author Dheeraj Prakash
 */
public class LimitedController {
    private final ForwardLimitedController forwardController;
    private final ReverseLimitedController reverseController;

    public LimitedController(SendableMotorController controller, DigitalInput forwardLimit, DigitalInput reverseLimit, DoublePreference forwardSpeed, DoublePreference reverseSpeed, Subsystem subsystem) {

        this.forwardController = new ForwardLimitedController(controller, forwardLimit, forwardSpeed, subsystem);
        this.reverseController = new ReverseLimitedController(controller, reverseLimit, reverseSpeed, subsystem);
    }

    public boolean canMove(double speed) {
        if (speed > 0) return canMoveForward();
        if (speed < 0) return canMoveReverse();
        return true;
    }

    public boolean canMove(DoubleSupplier speed) {
        return this.canMove(speed.getAsDouble());
    }

    // delegate methods to controller classes

    public void move(double speed) {
        forwardController.move(speed);
    }

    public void move(DoubleSupplier speed) {
        forwardController.move(speed);
    }

    public void stop() {
        forwardController.stop();
    }

    public void forward(double multiplier) {
        forwardController.forward(multiplier);
    }

    public void forward(DoubleSupplier multiplier) {
        forwardController.forward(multiplier);
    }

    public void forward() {
        forwardController.forward();
    }

    public Command forwardCmd(double multiplier) {
        return forwardController.forwardCmd(multiplier);
    }

    public Command forwardCmd(DoubleSupplier multiplier) {
        return forwardController.forwardCmd(multiplier);
    }

    public Command forwardCmd() {
        return forwardController.forwardCmd();
    }

    public Command conditionalForwardCmd(BooleanSupplier condition, double multiplier) {
        return forwardController.conditionalForwardCmd(condition, multiplier);
    }

    public Command conditionalForwardCmd(BooleanSupplier condition, DoubleSupplier multiplier) {
        return forwardController.conditionalForwardCmd(condition, multiplier);
    }

    public Command conditionalForwardCmd(BooleanSupplier condition) {
        return forwardController.conditionalForwardCmd(condition);
    }

    public boolean atForwardLimit() {
        return forwardController.atForwardLimit();
    }

    public boolean canMoveForward() {
        return forwardController.canMoveForward();
    }

    public void reverse(double multiplier) {
        reverseController.reverse(multiplier);
    }

    public void reverse(DoubleSupplier multiplier) {
        reverseController.reverse(multiplier);
    }

    public void reverse() {
        reverseController.reverse();
    }

    public Command reverseCmd(double multiplier) {
        return reverseController.reverseCmd(multiplier);
    }

    public Command reverseCmd(DoubleSupplier multiplier) {
        return reverseController.reverseCmd(multiplier);
    }

    public Command reverseCmd() {
        return reverseController.reverseCmd();
    }

    public Command conditionalReverseCmd(BooleanSupplier condition, double multiplier) {
        return reverseController.conditionalReverseCmd(condition, multiplier);
    }

    public Command conditionalReverseCmd(BooleanSupplier condition, DoubleSupplier multiplier) {
        return reverseController.conditionalReverseCmd(condition, multiplier);
    }

    public Command conditionalReverseCmd(BooleanSupplier condition) {
        return reverseController.conditionalReverseCmd(condition);
    }

    public boolean atReverseLimit() {
        return reverseController.atReverseLimit();
    }

    public boolean canMoveReverse() {
        return reverseController.canMoveReverse();
    }

    public SendableMotorController getController() {
        return forwardController.getController();
    }

    public DigitalInput getForwardLimit() {
        return forwardController.getLimit();
    }

    public DigitalInput getReverseLimit() {
        return reverseController.getLimit();
    }

    public DoublePreference getForwardSpeed() {
        return forwardController.getForwardSpeed();
    }

    public DoublePreference getReverseSpeed() {
        return reverseController.getReverseSpeed();
    }

    public Subsystem getSubsystem() {
        return forwardController.getSubsystem();
    }
}
