package org.hyperonline.hyperlib.subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import org.hyperonline.hyperlib.command.ConditionalInterruptCommand;
import org.hyperonline.hyperlib.controller.SendableMotorController;
import org.hyperonline.hyperlib.driving.DriverInput;
import org.hyperonline.hyperlib.pref.DoublePreference;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public abstract class PreferenceMotorSubsystem<MotorType extends SendableMotorController>
    extends PreferenceSubsystem {
  protected MotorType m_motor;
  protected DoublePreference m_forwardSpeed, m_backwardSpeed;

  protected PreferenceMotorSubsystem(String name, MotorType motor) {
    super(name);
    m_motor = motor;
    this.addChild("Motor", m_motor);
    this.setDefaultCommand(this.stopCmd());
  }

  protected void initMotorSpeedPreference(double forward, double backward) {
    m_forwardSpeed = m_prefs.addDouble("Forward Speed", forward);
    m_backwardSpeed = m_prefs.addDouble("Backward Speed", backward);
  }

  /**
   * continuous command to move the motor forward (positive voltage)
   *
   * @return
   */
  public Command forwardCmd() {
    return new RunCommand(this::forward, this);
  }

  /**
   * continuous command to move the motor backward (negative voltage)
   *
   * @return
   */
  public Command backwardCmd() {
    return new RunCommand(this::backward, this);
  }

  /**
   * continuous command stopping the motor (speed 0)
   *
   * @return
   */
  public Command stopCmd() {
    return new RunCommand(this::stop, this);
  }

  /**
   * move forward only if the given condition is satisfied, else stop the motor
   *
   * @param condition
   * @return
   */
  public Command conditionalForwardCmd(BooleanSupplier condition) {
    return new ConditionalInterruptCommand(forwardCmd(), stopCmd(), condition);
  }

  /**
   * move backward only if the given condition is satisfied, else stop the motor
   *
   * @param condition
   * @return
   */
  public Command conditionalBackwardCmd(BooleanSupplier condition) {
    return new ConditionalInterruptCommand(backwardCmd(), stopCmd(), condition);
  }

  /**
   * continuous command that moves the motor at the supplied speed
   *
   * @param speed
   * @return
   */
  public Command moveCmd(DoubleSupplier speed) {
    return new RunCommand(() -> this.move(speed), this);
  }

  /**
   * move the motor at the supplied speed, if the supplied condition is met, else stop
   *
   * @param speed
   * @param canMove
   * @return
   */
  public Command conditionalMoveCmd(DoubleSupplier speed, BooleanSupplier canMove) {
    return new ConditionalInterruptCommand(moveCmd(speed), stopCmd(), canMove);
  }

  /**
   * move the motor at the supplied speed with a peak output, if the supplied condition is met, else
   * stop
   *
   * @param speed
   * @param canMove
   * @return
   */
  public Command conditionalMoveWithSpeedLimitCmd(
      DoubleSupplier speed, double peakOutput, BooleanSupplier canMove) {
    return new ConditionalInterruptCommand(
        moveWithSpeedLimitCmd(speed, peakOutput), stopCmd(), canMove);
  }

  /**
   * cap the peak output of the motor when moving
   *
   * @param speed
   * @param peakOutput
   * @return
   */
  public Command moveWithSpeedLimitCmd(DoubleSupplier speed, double peakOutput) {
    return new RunCommand(
        () -> this.move(() -> DriverInput.governor(speed.getAsDouble(), peakOutput)), this);
  }

  /** move the motor forward (positive voltage) at the speed set in preference */
  public void forward() {
    m_motor.set(m_forwardSpeed.get());
  }

  /** move the motor backward (negative voltage) at the speed set in preference */
  public void backward() {
    m_motor.set(m_backwardSpeed.get());
  }

  /** stop the motor */
  public void stop() {
    m_motor.set(0);
  }
  ;

  /**
   * move the motor at the given speed
   *
   * @param speed
   */
  public void move(double speed) {
    m_motor.set(speed);
  }

  /**
   * move the motor at the supplied speed.
   *
   * @param speed speed that the motor should be moved at
   */
  public void move(DoubleSupplier speed) {
    m_motor.set(speed.getAsDouble());
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
