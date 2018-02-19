package org.usfirst.frc.team69.util.auto;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class AutonomousInfo extends SendableBase {
    private final HashMap<String, AutonomousRoutine> m_allRoutines;
    private final HashMap<String, AutonomousStrategy> m_allStrategies;
    private final String m_defaultSelection;
    private NetworkTableEntry m_currentSelection;

    public static class Builder {
        private HashMap<String, AutonomousRoutine> m_allRoutines = new HashMap<>();
        private HashMap<String, AutonomousStrategy> m_allStrategies = new HashMap<>();
        private String m_defaultSelection;
        
        public Builder addStrategy(AutonomousStrategy strat) {
            m_allStrategies.put(strat.getName(), strat);
            for (AutonomousRoutine rtn : strat.getPossibleRoutines()) {
                addWithSubroutines(rtn);
            }
            return this;
        }

        private void addWithSubroutines(AutonomousRoutine rtn) {
            if (!m_allRoutines.containsKey(rtn.getName())) {
                m_allRoutines.put(rtn.getName(), rtn);
                for (AutonomousRoutine sub : rtn.getSubroutines()) {
                    addWithSubroutines(sub);
                }
            }
        }

        public Builder addDefault(AutonomousStrategy strat) {
            m_defaultSelection = strat.getName();
            addStrategy(strat);
            return this;
        }
        
        public AutonomousInfo build() {
            return new AutonomousInfo(m_allRoutines, m_allStrategies, m_defaultSelection);
        }
    }
    
    private AutonomousInfo(HashMap<String, AutonomousRoutine> routines,
            HashMap<String, AutonomousStrategy> strats, 
            String defaultSelection) {
        setName("AutonomousInfo");
        m_allRoutines = new HashMap<>(routines);
        m_allStrategies = new HashMap<>(strats);
        m_defaultSelection = defaultSelection;
    }

    public AutonomousStrategy getSelection() {
        String selection = m_defaultSelection;
        if (m_currentSelection != null) {
            selection = m_currentSelection.getString(m_defaultSelection);
        }
        return m_allStrategies.get(selection);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("AutonomousInfo");
        for (AutonomousRoutine rtn : m_allRoutines.values()) {
            rtn.initSendable("Routines/" + rtn.getName() + "/", builder);
        }
        for (AutonomousStrategy strat : m_allStrategies.values()) {
            strat.initSendable("Strategies/" + strat.getName() + "/", builder);
        }
        builder.addStringProperty("Default", () -> m_defaultSelection, null);
        m_currentSelection = builder.getEntry("Selection");
    }
}
