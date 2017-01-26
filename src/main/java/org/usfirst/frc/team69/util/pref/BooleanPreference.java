package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

public class BooleanPreference extends Preference {
    private boolean m_lastValue;
    private boolean m_default;
    
    public BooleanPreference(String name, boolean value) {
        super(name);
        m_lastValue = value;
        m_default = value;
    }
    
    public boolean hasChanged() {
        boolean newValue = get();
        boolean changed = newValue != m_lastValue;
        m_lastValue = newValue;
        return changed;
    }
    
    public void putDefaultValue() {
        Preferences.getInstance().putBoolean(getName(), m_default);
    }
    
    public boolean get() {
        return Preferences.getInstance().getBoolean(getName(), m_default);
    }
}
