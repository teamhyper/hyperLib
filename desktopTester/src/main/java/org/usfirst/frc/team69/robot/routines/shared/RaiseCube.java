package org.usfirst.frc.team69.robot.routines.shared;

import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutoPref;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.pref.DoublePreference;

public class RaiseCube extends AutonomousRoutine {

    @AutoPref public DoublePreference 
            waitAfterGrip,
            waitAfterLift,
            waitAfterShoulder;
    
    @Override
    public void build(CommandBuilder builder) {
    }

}
