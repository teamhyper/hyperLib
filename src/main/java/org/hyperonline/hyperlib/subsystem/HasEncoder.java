package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

public interface HasEncoder {
    void resetEncoder();
    Command resetEncoderCmd();
    Command autoResetEncoderAtLimits();
    void configSensors();
}
