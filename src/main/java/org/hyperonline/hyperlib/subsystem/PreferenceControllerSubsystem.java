package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.driving.DriverInput;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;

/**
 * @param <MotorType> {@link org.hyperonline.hyperlib.controller.SendableMotorController} type to
 *                    use in this {@link edu.wpi.first.wpilibj2.command.Subsystem}
 * @author Chris McGroarty
 */
public abstract class PreferenceControllerSubsystem<MotorType extends SendableMotorController> extends PreferenceMotorSubsystem<MotorType> {
    protected DoublePreference m_forwardSpeed, m_reverseSpeed, m_rampRate;
    protected SlewRateLimiter m_rateLimiter;
    protected boolean useRampRate = false;

    /**
     * @param motor the motor to use in the subsystem
     */
    protected PreferenceControllerSubsystem(MotorType motor) {
        this(null, motor);
    }

    /**
     * @param name
     * @param motor
     */
    protected PreferenceControllerSubsystem(String name, MotorType motor) {
        super(name, motor);
    }

    /**
     * initialize the subsystem's forward/reverse speed preferences
     *
     * @param forward default positive speed for motor
     * @param reverse default negative speed for motor
     */
    protected void initMotorSpeedPreference(double forward, double reverse) {
        m_forwardSpeed = m_prefs.addDouble("Forward Speed", forward);
        m_reverseSpeed = m_prefs.addDouble("Backward Speed", reverse);
    }

    /**
     * set if the subsystem should use a ramp rate limiter or not
     *
     * @param rampRate ramp rate (in number of seconds) to limit the motor to go from 0 to full
     *                 throttle
     */
    protected void initRampRatePreference(double rampRate) {
        this.useRampRate = true;
        m_rampRate = m_prefs.addDouble("Seconds to Full Throttle", rampRate);
        m_rateLimiter = new SlewRateLimiter(1.0 / m_rampRate.get());
    }

    /**
     * continuous command that moves the motor at the supplied speed
     *
     * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     * @return {@link Command} that moves the motor at the given speed
     */
    public Command moveCmd(DoubleSupplier speed) {
        return new RunCommand(() -> this.move(speed), this);
    }

    /**
     * move the motor at the supplied speed, if the supplied condition is met, else stop
     *
     * @param speed   -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     * @param canMove should the motor be allowed to move (at hardstop/sensor)
     * @return {@link Command} that moves the motor at the given speed if the condition is true
     */
    public Command conditionalMoveCmd(DoubleSupplier speed, Predicate<DoubleSupplier> canMove) {
        return new FunctionalCommand(() -> {
        }, () -> {
            if (canMove.test(speed)) {
                move(speed);
            } else {
                stop();
            }
        }, interrupted -> stop(), () -> false, this);
    }

    /**
     * move the motor at the supplied speed with a peak output, if the supplied condition is met, else
     * stop
     *
     * @param speed      -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     * @param peakOutput maxmimum absolute value speed to allow
     * @param canMove    should the motor be allowed to move (at hardstop/sensor)
     * @return {@link Command} that moves the motor at the given or max speed if the condition is true
     */
    public Command conditionalMoveWithSpeedLimitCmd(DoubleSupplier speed, double peakOutput, Predicate<DoubleSupplier> canMove) {
        return new FunctionalCommand(() -> {
        }, () -> {
            if (canMove.test(speed)) {
                move(() -> DriverInput.governor(speed.getAsDouble(), peakOutput));
            } else {
                stop();
            }
        }, interrupted -> stop(), () -> false, this);
    }

    /**
     * cap the peak output of the motor when moving
     *
     * @param speed      -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     * @param peakOutput maxmimum absolute value speed to allow
     * @return {@link Command} that moves the motor at the given speed or max allowed speed if it is
     * over
     */
    public Command moveWithSpeedLimitCmd(DoubleSupplier speed, double peakOutput) {
        return new RunCommand(() -> this.move(() -> DriverInput.governor(speed.getAsDouble(), peakOutput)), this);
    }

    @Override
    public void forward() {
        forward(1);
    }

    /**
     * move the motor forward (positive voltage) at the speed set in preference
     *
     * @param multiplier how to modify the preference speed
     */
    public void forward(double multiplier) {
        move(m_forwardSpeed::get, multiplier);
    }

    /**
     * @param multiplier how to modify the preference speed
     * @return continuous {@link Command} to move the motor forward (positive voltage) modified by the
     * given multiplier
     */
    public Command forwardCmd(double multiplier) {
        return new RunCommand(() -> this.forward(multiplier), this);
    }

    /**
     * * move forward at the full preference speed only if the given condition is satisfied, else
     * move at the speed times the multiplier
     *
     * @param multipler how to modify the preference speed
     * @param condition should the motor be driven forwards at pref speed or modified speed
     * @return {@link Command} that moves the motor at its configured (preference) forward speed if
     * the condition is true
     */
    public Command conditionalForwardCmd(double multipler, BooleanSupplier condition) {
        return new FunctionalCommand(() -> {
        }, () -> {
            if (condition.getAsBoolean()) {
                forward();
            } else {
                forward(multipler);
            }
        }, interrupted -> stop(), () -> false, this);
    }

    /**
     * @param multiplier how to modify the preference speed
     * @return continuous {@link Command} to move the motor reverse (negative voltage) modified by
     * the given multiplier
     */
    public Command reverseCmd(double multiplier) {
        return new RunCommand(() -> this.reverse(multiplier), this);
    }

    /**
     * move backward at the full preference speed only if the given condition is satisfied, else move at the speed times the multiplier
     *
     * @param multipler how to modify the preference speed
     * @param condition should the motor be driven backwards at pref speed or modified speed
     * @return {@link Command} that moves the motor at its configured (preference) backward speed if *
     * the condition is true
     */
    public Command conditionalReverseCmd(double multipler, BooleanSupplier condition) {
        return new FunctionalCommand(() -> {
        }, () -> {
            if (condition.getAsBoolean()) {
                reverse();
            } else {
                reverse(multipler);
            }
        }, interrupted -> stop(), () -> false, this);
    }


    @Override
    public void reverse() {
        reverse(1);
    }

    /**
     * move the motor reverse (negative voltage) at the speed set in preference
     *
     * @param multiplier how to modify the preference speed
     */
    public void reverse(double multiplier) {
        move(m_reverseSpeed::get, multiplier);
    }

    /**
     * stop the motor
     */
    @Override
    public void stop() {
        m_motor.set(0);
        if (useRampRate) {
            m_rateLimiter.reset(0);
        }
    }

    /**
     * calculate the speed the motor should move at, based on if we're rate limiting or not
     *
     * @param speed the motor speed to rate limit or use
     * @return the calculated motor speed for this subsystem
     */
    protected double calculateSpeed(double speed) {
        double calculatedSpeed = speed;

        if (useRampRate) {
            calculatedSpeed = DriverInput.filterAllowZero(speed, m_rateLimiter, speed == 0);
        }
        return MathUtil.clamp(calculatedSpeed, -1, 1);
    }

    /**
     * move the motor at the given speed
     *
     * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     */
    public void move(double speed) {
        m_motor.set(calculateSpeed(speed));
    }

    /**
     * move the motor at the supplied speed.
     *
     * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
     */
    public void move(DoubleSupplier speed) {
        move(speed.getAsDouble());
    }

    public void move(DoubleSupplier speed, double multiplier) {
        move(speed.getAsDouble() * multiplier);
    }

    /**
     * @param mode coast or brake mode for the motor when set to 0
     */
    public void setNeutralMode(NeutralMode mode) {
        m_motor.setNeutralMode(mode);
    }

    @Override
    protected void configMotor() {
        m_motor.resetMotorConfig();
    }

    @Override
    protected void configSensors() {
    }

    @Override
    protected void configPID() {
    }

    public boolean canMoveForward(DoubleSupplier speed) {
        return speed.getAsDouble() >= 0 && canMoveForward();
    }

    public boolean canMoveReverse(DoubleSupplier speed) {
        return speed.getAsDouble() <= 0 && canMoveReverse();
    }


    public boolean canMove(DoubleSupplier speed) {
        if (speed.getAsDouble() > 0) {
            return canMoveForward();
        }

        if (speed.getAsDouble() < 0) {
            return canMoveReverse();
        }

        return true;

    }

    @Override
    public Command resetPositionSensorAtReverseLimitCmd() {
        return new RunCommand(() -> {
            if (isAtReverseLimit() && m_motor.get() <= 0) {
                this.resetPositionSensor();
            }
        });
    }

    @Override
    public Command resetPositionSensorAtForwardLimitCmd() {
        return new RunCommand(() -> {
            if (isAtForwardLimit() && m_motor.get() >= 0) {
                this.resetPositionSensor();
            }
        });
    }
}
