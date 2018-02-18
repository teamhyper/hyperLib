package org.hyperonline.autoviewer;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.wpi.first.shuffleboard.api.data.ComplexData;

public class AutonomousRoutineData extends ComplexData<AutonomousRoutineData> {

    private final String m_name;
    private final String[] m_preferences, m_subroutines;
    
    public static final String UNNAMED = "<unnamed routine>";
    
    public AutonomousRoutineData(String name, String[] preferences, String[] subroutines) {
        m_name = name;
        m_preferences = preferences.clone();
        m_subroutines = subroutines.clone();
    }
    
    public AutonomousRoutineData(Map<String, Object> map) {
        this((String) map.getOrDefault(".name", UNNAMED),
                (String[]) map.getOrDefault("Preferences", new String[0]),
                (String[]) map.getOrDefault("Subroutines", new String[0]));
    }
    
    public AutonomousRoutineData() {
        this(UNNAMED, new String[0], new String[0]);
    }
    
    @Override
    public Map<String, Object> asMap() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(".name", m_name);
        builder.put("Preferences", m_preferences);
        builder.put("Subroutines", m_subroutines);
        return builder.build();
    }
    
    public String getName() {
        return m_name;
    }
    
    public String[] getPreferenceNames() {
        return m_preferences.clone();
    }
    
    public String[] getSubroutineNames() {
        return m_subroutines.clone();
    }
    
    @Override
    public String toString() {
        return getName();
    }

}
