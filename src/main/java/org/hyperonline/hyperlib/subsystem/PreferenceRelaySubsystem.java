package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj.Relay;

public abstract class PreferenceRelaySubsystem extends PreferenceMotorSubsystem<Relay> {
    protected Relay m_motor;

    public PreferenceRelaySubsystem(Relay motor) {
        this(null, motor);
    }

    public PreferenceRelaySubsystem(String name, Relay motor) {
        super(name, motor);
    }

    @Override
    public void forward() {
        m_motor.set(Relay.Value.kForward);
    }

    @Override
    public void reverse() {
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
}
