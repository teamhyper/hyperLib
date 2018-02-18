package org.usfirst.frc.team69.util.auto;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class AutonomousInfo extends SendableBase {
    private HashMap<String, AutonomousRoutine> m_allRoutines = new HashMap<>();
    private String m_defaultSelection;
    private NetworkTableEntry m_currentSelection;
    
    public AutonomousInfo() {
        setName("AutonomousInfo");
    }
    
    public void addRoutine(AutonomousRoutine rtn) {
        m_allRoutines.put(rtn.getName(), rtn);
    }
    
    public void addDefault(AutonomousRoutine rtn) {
        m_defaultSelection = rtn.getName();
        m_allRoutines.put(rtn.getName(), rtn);
    }
    
    public AutonomousRoutine getSelection() {
        String selection = m_defaultSelection;
        if (m_currentSelection != null) {
            selection = m_currentSelection.getString(m_defaultSelection);
        }
        return m_allRoutines.get(selection);
    }
    
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("AutonomousInfo");
        for (AutonomousRoutine rtn : m_allRoutines.values()) {
            builder.addStringProperty("Routines/" + rtn.getName() + "/.name", rtn::getName, null);
            builder.addStringProperty("Routines/" + rtn.getName() + "/.type", () -> "AutonomousRoutine", null);
            builder.addStringArrayProperty("Routines/" + rtn.getName() + "/Preferences", rtn::getPreferenceNames, null);
            builder.addStringArrayProperty("Routines/" + rtn.getName() + "/Subroutines", rtn::getSubroutineNames, null);
        }
        builder.addStringProperty("Default", () -> m_defaultSelection, null);
        m_currentSelection = builder.getEntry("Selection");
    }
}
