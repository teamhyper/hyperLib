package org.usfirst.frc.team69.util.auto.desktoptester;

import org.usfirst.frc.team69.robot.routines.CenterToLeftSwitchNoVision;
import org.usfirst.frc.team69.robot.routines.CenterToRightSwitchNoVision;
import org.usfirst.frc.team69.robot.routines.LeftToScale;
import org.usfirst.frc.team69.robot.routines.LeftToSwitchNoVision;
import org.usfirst.frc.team69.robot.routines.RightToScale;
import org.usfirst.frc.team69.robot.routines.SideCrossBaseline;
import org.usfirst.frc.team69.robot.routines.StraightToSwitchNoVision;
import org.usfirst.frc.team69.util.auto.AutonomousInfo;
import org.usfirst.frc.team69.util.auto.AutonomousStrategy;

public class ExampleAutoPicker {

    /**
     * Add all possible strategies to the builder object.
     * A strategy is a mapping of FMS data to auto routines.  This is where
     * you declare those mappings.
     * 
     * @param builder The builder for auto info.
     */
    public static void declareStrategies(AutonomousInfo.Builder builder) {
        AutonomousStrategy leftSwitch = new AutonomousStrategy.Builder("Left: Priority SWITCH")
                .addScenario("LLL", new LeftToSwitchNoVision())
                .addScenario("LRL", new LeftToSwitchNoVision())
                .addScenario("RLR", new LeftToScale())
                .addScenario("RRR", new SideCrossBaseline())
                .addDefault(new SideCrossBaseline())
                .build();
        AutonomousStrategy leftScale = new AutonomousStrategy.Builder("Left: Priority SCALE")
                .addScenario("LLL", new LeftToScale())
                .addScenario("LRL", new LeftToSwitchNoVision())
                .addScenario("RLR", new LeftToScale())
                .addScenario("RRR", new SideCrossBaseline())
                .addDefault(new SideCrossBaseline())
                .build();
        AutonomousStrategy rightSwitch = new AutonomousStrategy.Builder("Right: Priority SWITCH")
                .addScenario("RRR", new StraightToSwitchNoVision())
                .addScenario("RLR", new StraightToSwitchNoVision())
                .addScenario("LRL", new RightToScale())
                .addScenario("LLL", new SideCrossBaseline())
                .addDefault(new SideCrossBaseline())
                .build();
        AutonomousStrategy rightScale = new AutonomousStrategy.Builder("Right: Priority SCALE")
                .addScenario("RRR", new RightToScale())
                .addScenario("RLR", new StraightToSwitchNoVision())
                .addScenario("LRL", new RightToScale())
                .addScenario("LLL", new SideCrossBaseline())
                .addDefault(new SideCrossBaseline())
                .build();
        AutonomousStrategy center = new AutonomousStrategy.Builder("Center: Priority SWITCH")
                .addScenario("RRR", new CenterToRightSwitchNoVision())
                .addScenario("RLR", new CenterToRightSwitchNoVision())
                .addScenario("LRL", new CenterToLeftSwitchNoVision())
                .addScenario("LLL", new CenterToLeftSwitchNoVision())
                .build();
        builder.addStrategy(leftSwitch);
        builder.addStrategy(leftScale);
        builder.addStrategy(rightSwitch);
        builder.addStrategy(rightScale);
        builder.addDefault(center);
    }
}
