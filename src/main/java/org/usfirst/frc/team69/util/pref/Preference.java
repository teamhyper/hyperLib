package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

public abstract class Preference {
    private String m_name;
    
    public Preference(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        
        m_name = name;
    }
    
    public String getName() {
        return m_name;
    }
    
    public void putDefaultIfEmpty() {
        if (!Preferences.getInstance().containsKey(m_name)) {
            putDefaultValue();
        }
    }
    
    /**
     * Check if the value has changed since last being checked,
     * or if this is the first time this method was called and
     * the value has changed from the default.
     * 
     * @return true if the value has changed, false otherwise
     */
    abstract boolean hasChanged();
    
    /**
     * Put an entry with the default value.
     */
    abstract void putDefaultValue();
}
