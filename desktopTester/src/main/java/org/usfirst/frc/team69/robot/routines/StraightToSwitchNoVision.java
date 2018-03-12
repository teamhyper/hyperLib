package org.usfirst.frc.team69.robot.routines;

import org.usfirst.frc.team69.robot.routines.shared.RaiseCubeToSwitchManual;
import org.usfirst.frc.team69.robot.routines.shared.UnGrip;
import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutoPref;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.pref.DoublePreference;

public class StraightToSwitchNoVision extends AutonomousRoutine {

    @AutoPref public DoublePreference speed, time;
    
    AutonomousRoutine raiseCubeToSwitch = addSubroutine(new RaiseCubeToSwitchManual());
    AutonomousRoutine unGrip = addSubroutine(new UnGrip());
    
    @Override
    public void build(CommandBuilder builder) {
    }

}
