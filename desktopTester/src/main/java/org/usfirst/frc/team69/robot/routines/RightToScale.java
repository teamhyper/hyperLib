package org.usfirst.frc.team69.robot.routines;

import org.usfirst.frc.team69.robot.routines.shared.BackOffScale;
import org.usfirst.frc.team69.robot.routines.shared.DriveScaleToSwitch;
import org.usfirst.frc.team69.robot.routines.shared.OffWallNudge;
import org.usfirst.frc.team69.robot.routines.shared.RaiseCubeToScaleManual;
import org.usfirst.frc.team69.robot.routines.shared.UnGrip;
import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutoPref;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.pref.DoublePreference;

public class RightToScale extends AutonomousRoutine {

	@AutoPref
	public DoublePreference diagSpeed, diagTime, fwdSpeed, fwdTime;

	AutonomousRoutine offWallNudge = addSubroutine(new OffWallNudge());
	AutonomousRoutine raiseCubeToScale = addSubroutine(new RaiseCubeToScaleManual());
	AutonomousRoutine unGrip = addSubroutine(new UnGrip());
	AutonomousRoutine backOffScale = addSubroutine(new BackOffScale());
	AutonomousRoutine scaleToSwitch = addSubroutine(new DriveScaleToSwitch());

	@Override
	public void build(CommandBuilder builder) {
	}

}
