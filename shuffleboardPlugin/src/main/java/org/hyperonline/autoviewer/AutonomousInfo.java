package org.hyperonline.autoviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class AutonomousInfo extends ComplexData<AutonomousInfo> {

    private final HashMap<String, AutonomousRoutineData> m_routines;
    private final String m_selection;
    private final String m_default;
    
    public AutonomousInfo() {
        m_routines = new HashMap<>();
        m_selection = null;
        m_default = null;
    }
    
    public AutonomousInfo(Map<String, AutonomousRoutineData> routines, String defaultString, String selection) {
        m_routines = new HashMap<>(routines);
        m_selection = selection;
        m_default = defaultString;
    }
    
    public AutonomousInfo(Map<String, Object> map) {
        m_routines = new HashMap<>();
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
        if (m_selection != null) {
            result.put("Selection", m_selection);
        }
        return result;
    }

    public AutonomousInfo withSelection(String newSelection) {
        return new AutonomousInfo(m_routines, m_default, newSelection);
    }
    
    public Set<String> getRoutineNames() {
        return m_routines.keySet();
    }
    
    public AutonomousRoutineData getRoutine(String name) {
        return m_routines.get(name);
    }
    
    public String getSelection() {
        return m_selection;
    }

    public String getDefault() {
        return m_default;
    }
}
