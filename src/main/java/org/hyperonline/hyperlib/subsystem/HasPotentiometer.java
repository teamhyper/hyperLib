package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

public interface HasPotentiometer {
    void resetPot();
    Command resetPotCmd();
    void configSensors();
}
