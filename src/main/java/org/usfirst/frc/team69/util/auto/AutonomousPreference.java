package org.usfirst.frc.team69.util.auto;

import java.util.Objects;

/**
 * {@link AutonomousPreference}
 * @author James
 *
 */
public class AutonomousPreference {
    // Why would we need to support anything else?
    
    public static final String SEPERATOR = "/";
    public static final String PREFERENCES_LOCATION = "$auto_preferences";
    
    /**
     * 
     * @author James
     *
     */
    public enum Type {
        DOUBLE
    }
    
    private final String m_name;
    private final AutonomousRoutine m_routine;
    
    /**
     * 
     * @param routine
     * 			the routine to tie the preference to
     * @param name
     * 			the name of the preference
     */
    public AutonomousPreference(AutonomousRoutine routine, String name) {
        m_name = Objects.requireNonNull(name);
        m_routine = Objects.requireNonNull(routine);
    }
    
    public Type getType() {
        return Type.DOUBLE;
    }
    
    public String getName() {
        return m_name;
    }
    
    public AutonomousRoutine getRoutine() {
        return m_routine;
    }
    
    public String getFullPath() {
        return PREFERENCES_LOCATION + SEPERATOR + m_routine.getName() + SEPERATOR + m_name;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AutonomousPreference)) {
            return false;
        }
        AutonomousPreference pref = (AutonomousPreference) other;
        return m_name.equals(pref.m_name) && m_routine.equals(pref.m_routine); 
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(m_name, m_routine);
    }
    
    /**
     * @return {String}
     */
    public String toString() {
        return m_routine.getName() + SEPERATOR + m_name;
    }
}
