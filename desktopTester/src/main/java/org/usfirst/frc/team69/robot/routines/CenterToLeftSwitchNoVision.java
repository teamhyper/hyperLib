package org.usfirst.frc.team69.robot.routines;

import org.usfirst.frc.team69.robot.routines.shared.OffWallNudge;
import org.usfirst.frc.team69.robot.routines.shared.RaiseCube;
import org.usfirst.frc.team69.robot.routines.shared.UnGrip;
import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutoPref;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.pref.DoublePreference;

public class CenterToLeftSwitchNoVision extends AutonomousRoutine {
    @AutoPref public DoublePreference diagSpeed, diagTime, fwdSpeed, fwdTime;
    
    AutonomousRoutine offWallNudge = addSubroutine(new OffWallNudge());
    AutonomousRoutine raiseCube = addSubroutine(new RaiseCube());
    AutonomousRoutine unGrip = addSubroutine(new UnGrip());
    
    @Override
    public void build(CommandBuilder builder) {
    }
}
