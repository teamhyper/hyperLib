package org.usfirst.frc.team69.util.auto;

import java.util.HashSet;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class AutonomousInfo extends SendableBase {
    private HashSet<AutonomousRoutine> m_allRoutines = new HashSet<>();
    
    public AutonomousInfo() {
        setName("AutonomousInfo");
    }
    
    public void addRoutine(AutonomousRoutine rtn) {
        m_allRoutines.add(rtn);
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("AutonomousInfo");
        for (AutonomousRoutine rtn : m_allRoutines) {
            builder.addStringProperty(rtn.getName() + "/.name", rtn::getName, null);
            builder.addStringProperty(rtn.getName() + "/.type", () -> "AutonomousRoutine", null);
            builder.addStringArrayProperty(rtn.getName() + "/Preferences", rtn::getPreferenceNames, null);
            builder.addStringArrayProperty(rtn.getName() + "/Subroutines", rtn::getSubroutineNames, null);
        }
    }
}
