package org.hyperonline.hyperlib.pid.rio;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pid.IPIDController;
import org.hyperonline.hyperlib.pid.PrefPIDController;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public abstract class BaseRioPID<ControllerType extends IPIDController> extends PrefPIDController {

    protected ControllerType m_pid;
    protected DoubleSupplier m_source;
    protected DoubleConsumer m_output;
    protected boolean isExecutablePID = false;

    public BaseRioPID(String name, DoubleSupplier source, DoubleConsumer output, double Kp, double Ki, double Kd, double tolerance) {
        super(name, Kp, Ki, Kd, tolerance);

        m_source = source;
        m_output = output;
        isExecutablePID = output != null;
    }

    /**
     * enable and set continuous input for this pid/sensor. for example, a mechanism that rotates
     * 360deg would have an input range of 0 to 360, so that going under 0 instead reads as 359
     * instead of -1
     *
     * @param minIn input to switch to maximum input when going under
     * @param maxIn input to switch to minimum input when going over
     */
    public void enableContinuousInput(double minIn, double maxIn) {
        m_pid.enableContinuousInput(minIn, maxIn);
    }

    /**
     * disable continuous input for this pid/sensor. the sensor will count in either direction
     * infinitely (or until the sensor has a hardstop like a pot)
     */
    public void disableContinuousInput() {
        m_pid.disableContinuousInput();
    }

    public void setIntegratorRange(double minimumIntegral, double maximumIntegral){
        m_pid.setIntegratorRange(minimumIntegral, maximumIntegral);
    }

    @Override
    protected void setPID(double Kp, double Ki, double Kd) {
        m_pid.setPID(Kp, Ki, Kd);
    }

    @Override
    protected void setTolerance(double tolerance) {
        m_pid.setTolerance(tolerance);
    }

    @Override
    public void execute() {
        if (isExecutablePID) {
            if (m_enabled) {
                m_output.accept(calculate(getFromSource()));
            } else {
                m_output.accept(0);
            }
        }
    }

    public double calculate(double measurement) {
        return MathUtil.clamp(m_pid.calculate(measurement), m_minOut, m_maxOut);
    }

    public double calculate() {
        return calculate(getFromSource());
    }

    public double calculate(double measurement, double setpoint) {
        // Set setpoint to provided value
        setSetpoint(setpoint);
        return calculate(measurement);
    }

    @Override
    public void disable() {
        super.disable();
    }

    public ControllerType getController() {
        return m_pid;
    }

    @Override
    public double getFromSource() {
        return nativeToFriendly(m_source.getAsDouble());
    }

    @Override
    public boolean onTarget() {
        return m_pid.atSetpoint();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addBooleanProperty("Executable PID", () -> isExecutablePID, null);
    }
}
