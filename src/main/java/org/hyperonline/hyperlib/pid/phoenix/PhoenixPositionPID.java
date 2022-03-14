package org.hyperonline.hyperlib.pid.phoenix;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.util.sendable.SendableBuilder;

public class PhoenixPositionPID extends PhoenixPID {

  private boolean m_forwardLimitEnabled = false;
  private boolean m_reverseLimitEnabled = false;

  public PhoenixPositionPID(
      String name,
      WPI_TalonSRX motor,
      int pidSlot,
      double Kp,
      double Ki,
      double Kd,
      double tolerance,
      int kIZone,
      double kPeakOut) {
    super(name, motor, pidSlot, Kp, Ki, Kd, tolerance, kIZone, kPeakOut);
  }

  @Override
  public void enable() {
    super.enable();
    m_motor.set(ControlMode.Position, m_setPoint);
  }

  @Override
  public double getFromSource() {
    return m_motor.getSelectedSensorPosition(m_pidSlot);
  }

  @Override
  public boolean onTarget(double target) {
    // TODO: try using the TalonSRX error here instead of calculating ourselves
    // other teams had issues with the error being zero when the command first ran, ending the
    // command before the PID could be used
    return super.onTarget(target)
        ||
        // if we're going to zero, consider the PID done when we trip the revSwitch, if the error
        // accumulated
        (m_reverseLimitEnabled && target <= 0 && m_motor.isRevLimitSwitchClosed() > 0)
        ||
        // if we're going forward (to our max angle), consider the PID Done when we trip the
        // fwdSwitch
        (m_forwardLimitEnabled && target > 0 && m_motor.isFwdLimitSwitchClosed() > 0);

    // The TalonSRX will stop driving when these limits are hit, but the PID command would not know
    // to quit
    // if we were not on target (within tolerance) which if we cannot drive further (ie, limit trips
    // at 0.5 instead of 0 because of error)
    // then the command would never finish, as the TalonSRX would be sending 0, even though the PID
    // is sending a value
  }

  public void setLimitSwitches(boolean forward, boolean reverse) {
    m_forwardLimitEnabled = forward;
    m_reverseLimitEnabled = reverse;
  }

  @Override
  protected void setSmartDashboardType(SendableBuilder builder) {
    builder.setSmartDashboardType("Phoenix Position PID");
  }
}
