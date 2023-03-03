package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;
import org.hyperonline.hyperlib.pref.DoublePreference;

public interface MovesWithPID {
    Command moveTo(DoublePreference positionPreference);

    Command moveTo(double setpoint);

    void initPositionPreferences();
}
