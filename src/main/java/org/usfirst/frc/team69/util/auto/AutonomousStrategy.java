package org.usfirst.frc.team69.util.auto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class AutonomousStrategy {

    public static class Builder {
        private final HashMap<String, AutonomousRoutine> m_map = new HashMap<>();
        private AutonomousRoutine m_default;
        private String m_name;
        
        public Builder(String name) {
            m_name = Objects.requireNonNull(name);
        }
        
        public Builder addScenario(String input, AutonomousRoutine rtn) {
            Objects.requireNonNull(input);
            Objects.requireNonNull(rtn);
            m_map.put(input, rtn);
            return this;
        }
        
        public Builder addDefault(AutonomousRoutine rtn) {
            m_default = rtn;
            return this;
        }
        
        public AutonomousStrategy build() {
            return new AutonomousStrategy(m_name, m_map, m_default);
        }
    }
    
    private final Map<String, AutonomousRoutine> m_map;
    private final AutonomousRoutine m_default;
    private final String m_name;
    
    private AutonomousStrategy(String name, Map<String, AutonomousRoutine> map, AutonomousRoutine def) {
        m_name = name;
        m_map = new HashMap<>(map);
        m_default = def;
    }
    
    public AutonomousRoutine getDefaultRoutine() {
        return m_default;
    }
    
    public AutonomousRoutine getRoutineForScenario(String input) {
        return m_map.get(input);
    }
    
    public String getName() {
        return m_name;
    }
    
    public Collection<AutonomousRoutine> getPossibleRoutines() {
        return m_map.values();
    }
    
    void initSendable(String prefix, SendableBuilder builder) {
        builder.addStringProperty(prefix + ".type", () -> "AutonomousStrategy", null);
        builder.addStringProperty(prefix + "Default", m_default::getName, null);
        for (Entry<String, AutonomousRoutine> ent : m_map.entrySet()) {
            builder.addStringProperty(prefix + "Scenarios/" + ent.getKey(), () -> ent.getValue().getName(), null);
        }
    }
}
