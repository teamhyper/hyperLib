package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.driving.DriverInput;
import org.hyperonline.hyperlib.pref.DoublePreference;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * @param <MotorType> {@link org.hyperonline.hyperlib.controller.SendableMotorController} type to
 *     use in this {@link edu.wpi.first.wpilibj2.command.Subsystem}
 * @author Chris McGroarty
 */
public abstract class PreferenceMotorSubsystem<MotorType extends SendableMotorController>
    extends PreferenceSubsystem {
  protected MotorType m_motor;
  protected DoublePreference m_forwardSpeed, m_backwardSpeed, m_rampRate;
  protected SlewRateLimiter m_rateLimiter;
  protected boolean useRampRate = false;

  /**
   * @param motor the motor to use in the subsystem
   */
  protected PreferenceMotorSubsystem(MotorType motor) {
    super();
    m_motor = motor;
    this.addChild("Motor", m_motor);
    this.setDefaultCommand(this.stopCmd());
  }

  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name {@inheritDoc}
   */
  @Deprecated
  protected PreferenceMotorSubsystem(String name, MotorType motor) {
    this(motor);
  }

  /**
   * initialize the subsystem's forward/backward speed preferences
   *
   * @param forward default positive speed for motor
   * @param backward default negative speed for motor
   */
  protected void initMotorSpeedPreference(double forward, double backward) {
    m_forwardSpeed = m_prefs.addDouble("Forward Speed", forward);
    m_backwardSpeed = m_prefs.addDouble("Backward Speed", backward);
  }

  /**
   * set if the subsystem should use a ramp rate limiter or not
   *
   * @param rampRate ramp rate (in number of seconds) to limit the motor to go from 0 to full
   *     throttle
   */
  protected void initRampRatePreference(double rampRate) {
    this.useRampRate = true;
    m_rampRate = m_prefs.addDouble("Seconds to Full Throttle", rampRate);
    m_rateLimiter = new SlewRateLimiter(1.0 / m_rampRate.get());
  }

  @Override
  public void initPreferences() {}

  /**
   * @return continuous {@link Command} to move the motor forward (positive voltage)
   */
  public Command forwardCmd() {
    return new RunCommand(this::forward, this);
  }
  /**
   * @param multiplier how to modify the preference speed
   * @return continuous {@link Command} to move the motor forward (positive voltage) modified by the
   *     given multiplier
   */
  public Command forwardCmd(double multiplier) {
    return new RunCommand(() -> this.forward(multiplier), this);
  }

  /**
   * @return continuous {@link Command} to move the motor backward (negative voltage)
   */
  public Command backwardCmd() {
    return new RunCommand(this::backward, this);
  }

  /**
   * @param multiplier how to modify the preference speed
   * @return continuous {@link Command} to move the motor backward (negative voltage) modified by
   *     the given multiplier
   */
  public Command backwardCmd(double multiplier) {
    return new RunCommand(() -> this.backward(multiplier), this);
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
   *     the condition is true
   */
  public Command conditionalForwardCmd(BooleanSupplier condition) {
    return conditionalForwardCmd(0.0, condition);
  }

  /**
   * move forward only if the given condition is satisfied, else stop the motor
   *
   * @param multipler how to modify the preference speed
   * @param condition should the motor be driven forwards at pref speed or modified speed
   * @return {@link Command} that moves the motor at its configured (preference) forward speed if
   *     the condition is true
   */
  public Command conditionalForwardCmd(double multipler, BooleanSupplier condition) {
    return new FunctionalCommand(
        () -> {},
        () -> {
          if (condition.getAsBoolean()) {
            forward();
          } else {
            forward(multipler);
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
   *     the condition is true
   */
  public Command conditionalBackwardCmd(BooleanSupplier condition) {
    return conditionalBackwardCmd(0.0, condition);
  }

  /**
   *
   * @param multipler how to modify the preference speed
   * @param condition should the motor be driven backwards at pref speed or modified speed
   * @return
   */
  public Command conditionalBackwardCmd(double multipler, BooleanSupplier condition) {
    return new FunctionalCommand(
        () -> {},
        () -> {
          if (condition.getAsBoolean()) {
            backward();
          } else {
            backward(multipler);
          }
        },
        interrupted -> stop(),
        () -> false,
        this);
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
   * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
   * @param canMove should the motor be allowed to move (at hardstop/sensor)
   * @return {@link Command} that moves the motor at the given speed if the condition is true
   */
  public Command conditionalMoveCmd(DoubleSupplier speed, BooleanSupplier canMove) {
    return new FunctionalCommand(
        () -> {},
        () -> {
          if (canMove.getAsBoolean()) {
            move(speed);
          } else {
            stop();
          }
        },
        interrupted -> stop(),
        () -> false,
        this);
  }

  /**
   * move the motor at the supplied speed with a peak output, if the supplied condition is met, else
   * stop
   *
   * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
   * @param peakOutput maxmimum absolute value speed to allow
   * @param canMove should the motor be allowed to move (at hardstop/sensor)
   * @return {@link Command} that moves the motor at the given or max speed if the condition is true
   */
  public Command conditionalMoveWithSpeedLimitCmd(
      DoubleSupplier speed, double peakOutput, BooleanSupplier canMove) {
    return new FunctionalCommand(
        () -> {},
        () -> {
          if (canMove.getAsBoolean()) {
            move(() -> DriverInput.governor(speed.getAsDouble(), peakOutput));
          } else {
            stop();
          }
        },
        interrupted -> stop(),
        () -> false,
        this);
  }

  /**
   * cap the peak output of the motor when moving
   *
   * @param speed -1.0 to 1.0 speed the motor should move at (0-100% forward and reverse)
   * @param peakOutput maxmimum absolute value speed to allow
   * @return {@link Command} that moves the motor at the given speed or max allowed speed if it is
   *     over
   */
  public Command moveWithSpeedLimitCmd(DoubleSupplier speed, double peakOutput) {
    return new RunCommand(
        () -> this.move(() -> DriverInput.governor(speed.getAsDouble(), peakOutput)), this);
  }

  public void forward() {
    forward(1);
  }

  /**
   * move the motor forward (positive voltage) at the speed set in preference
   *
   * @param multiplier how to modify the preference speed
   */
  public void forward(double multiplier) {
    m_motor.set(calculateSpeed(m_forwardSpeed.get() * multiplier));
  }

  public void backward() {
    backward(1);
  }
  /**
   * move the motor backward (negative voltage) at the speed set in preference
   *
   * @param multiplier how to modify the preference speed
   */
  public void backward(double multiplier) {
    m_motor.set(calculateSpeed(m_backwardSpeed.get() * multiplier));
  }

  /** stop the motor */
  public void stop() {
    m_motor.set(0);
    m_rateLimiter.reset(0);
  }

  /**
   * calculate the speed the motor should move at, based on if we're rate limiting or not
   *
   * @param speed the motor speed to rate limit or use
   * @return the calculated motor speed for this subsystem
   */
  protected double calculateSpeed(double speed) {
    if (useRampRate) {
      return DriverInput.filterAllowZero(speed, m_rateLimiter, speed == 0);
    }
    return speed;
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

  /**
   * @param mode coast or brake mode for the motor when set to 0
   */
  public void setNeutralMode(NeutralMode mode) {
    m_motor.setNeutralMode(mode);
  }

  /** helper for organizing motor configuration. */
  protected void configMotor() {}

  /** helper for organizing sensor configuration */
  protected void configSensors() {}

  /** helper for organizing PID configuration */
  protected void configPID() {}
}
