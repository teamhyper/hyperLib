package org.hyperonline.autoviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AutonomousStrategyData {
    private final HashMap<String, ArrayList<String>> m_scenarios;
    private final String m_default;
    private final String m_name;
    
    public static class Builder {
        private HashMap<String, ArrayList<String>> m_scenarios;
        private String m_default;
        private String m_name;
        
        public Builder(String name) {
            m_name = Objects.requireNonNull(name);
            m_scenarios = new HashMap<>();
        }
        
        public Builder addScenario(String input, String routine) {
            Objects.requireNonNull(input);
            Objects.requireNonNull(routine);
            m_scenarios.putIfAbsent(routine, new ArrayList<>());
            m_scenarios.get(routine).add(input);
            return this;
        }
        
        public AutonomousStrategyData build() {
            return new AutonomousStrategyData(m_name, m_scenarios, m_default);
        }

        public Builder addDefault(String def) {
            m_default = def;
            return this;
        }
    }
    
    private AutonomousStrategyData(String name, HashMap<String, ArrayList<String>> scenarios, String def) {
        m_name = name;
        m_scenarios = new HashMap<>(scenarios);
        m_default = def;
        
        // Sort so that things are consistent
        for (List<String> lst : m_scenarios.values()) {
            Collections.sort(lst);
        }
    }
    
    public List<String> scenariosForRoutine(String routine) {
        return m_scenarios.get(routine);
    }
    
    public List<String> getPossibleRoutines() {
        // Sort so that things appear in a consistent order
        ArrayList<String> result = new ArrayList<>(m_scenarios.keySet());
        Collections.sort(result);
        return result;
    }
    
    public String getDefault() {
        return m_default;
    }
    
    public String getName() {
        return m_name;
    }
}
