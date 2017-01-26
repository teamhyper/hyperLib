package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

public class StringPreference extends Preference {
    private String m_lastValue;
    private String m_default;
    
    public StringPreference(String name, String value) {
        super(name);
        
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        
        m_lastValue = value;
        m_default = value;
    }
    
    public boolean hasChanged() {
        String newValue = get();
        boolean changed = !newValue.equals(m_lastValue);
        m_lastValue = newValue;
        return changed;
    }
    
    public void putDefaultValue() {
        Preferences.getInstance().putString(getName(), m_default);
    }
    
    public String get() {
        return Preferences.getInstance().getString(getName(), m_default);
    }
}
