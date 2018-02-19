package org.hyperonline.autoviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class AutonomousInfo extends ComplexData<AutonomousInfo> {

    private final HashMap<String, AutonomousRoutineData> m_routines;
    private final HashMap<String, AutonomousStrategyData> m_strategies;
    private final String m_selection;
    private final String m_default;
    
    public AutonomousInfo() {
        m_routines = new HashMap<>();
        m_strategies = new HashMap<>();
        m_selection = null;
        m_default = null;
    }
    
    public AutonomousInfo(
            Map<String, AutonomousRoutineData> routines, 
            Map<String, AutonomousStrategyData> strategies, 
            String defaultString, String selection) {
        m_routines = new HashMap<>(routines);
        m_strategies = new HashMap<>(strategies);
        m_selection = selection;
        m_default = defaultString;
    }
    
    public AutonomousInfo(Map<String, Object> map) {
        m_routines = new HashMap<>();
        m_strategies = new HashMap<>();
        for (Entry<String, Object> ent : map.entrySet()) {
            if (ent.getKey().startsWith("Routines/") 
                    && ent.getKey().endsWith("/.type") 
                    && ent.getValue().equals("AutonomousRoutine")) {
                String name = ent.getKey().substring(9, ent.getKey().length() - 6);
                String[] prefs = (String[]) map.getOrDefault("Routines/" + name + "/Preferences", new String[0]);
                String[] subrtns = (String[]) map.getOrDefault("Routines/" + name + "/Subroutines", new String[0]);
                AutonomousRoutineData rtn = new AutonomousRoutineData(name, prefs, subrtns);
                m_routines.put(name, rtn);
            }
            
            if (ent.getKey().startsWith("Strategies/") 
                    && ent.getKey().endsWith("/.type")
                    && ent.getValue().equals("AutonomousStrategy")) {
                String name = ent.getKey().substring(11, ent.getKey().length() - 6);
                System.out.println("!!! Added strategy to data object !!! " + name);
                AutonomousStrategyData.Builder builder = new AutonomousStrategyData.Builder(name);
                for (Entry<String, Object> ent2 : map.entrySet()) {
                    String prefix = "Strategies/" + name + "/Scenarios/";
                    if (ent2.getKey().startsWith(prefix)) {
                        builder.addScenario(
                                ent2.getKey().substring(prefix.length()), 
                                (String) ent2.getValue());
                    }
                }
                builder.addDefault((String) map.getOrDefault("Strategies/" + name + "/Default", ""));
                m_strategies.put(name, builder.build());
            }
        }
        
        Object defaultString = map.get("Default");
        m_default = defaultString instanceof String ? (String) defaultString : null;
        Object selection = map.get("Selection");
        m_selection = selection instanceof String ? (String) selection : m_default;
    }
    
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> result = new HashMap<>();
        for (AutonomousRoutineData ent : m_routines.values()) {
            result.put("Routines/" + ent.getName() + "/.type", "AutonomousRoutine");
            result.put("Routines/" + ent.getName() + "/Preferences", ent.getPreferenceNames());
            result.put("Routines/" + ent.getName() + "/Subroutines", ent.getSubroutineNames());
        }
        for (AutonomousStrategyData ent : m_strategies.values()) {
            result.put("Strategies/" + ent.getName() + "/.type", "AutonomousStrategy");
            for (String routine : ent.getPossibleRoutines()) {
                for (String scenario : ent.scenariosForRoutine(routine)) {
                    result.put("Strategies/" + ent.getName() + "/Scenarios/" + scenario, routine);
                }
            }
        }
        if (m_selection != null) {
            result.put("Selection", m_selection);
        }
        return result;
    }

    public AutonomousInfo withSelection(String newSelection) {
        return new AutonomousInfo(m_routines, m_strategies, m_default, newSelection);
    }
    
    public Set<String> getStrategyNames() {
        return m_strategies.keySet();
    }
    
    public Set<String> getRoutineNames() {
        return m_routines.keySet();
    }
    
    public AutonomousRoutineData getRoutine(String name) {
        return m_routines.get(name);
    }
    
    public AutonomousStrategyData getStrategy(String name) {
        return m_strategies.get(name);
    }
    
    public String getSelection() {
        return m_selection;
    }

    public String getDefault() {
        return m_default;
    }
}
