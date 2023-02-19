package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.BooleanSupplier;

public abstract class PreferenceMotorSubsystem extends PreferenceSubsystem {

    public PreferenceMotorSubsystem() {
        super(null);
    }

    public PreferenceMotorSubsystem(String name) {
        super(name);
    }

    public abstract Command forwardCmd();

    public abstract Command backwardCmd();

    public abstract Command stopCmd();

    public abstract Command conditionalForwardCmd(BooleanSupplier condition);

    public abstract Command conditionalBackwardCmd(BooleanSupplier condition);

    public abstract void forward();

    public abstract void backward();

    public abstract void stop();

    protected abstract void configMotor();

    protected abstract void configSensors();

    protected abstract void configPID();
}
