package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

public class IntPreference extends Preference {
    private int m_lastValue;
    private int m_default;
    
    public IntPreference(String name, int value) {
        super(name);
        m_lastValue = value;
        m_default = value;
    }
    
    public boolean hasChanged() {
        int newValue = get();
        boolean changed = newValue != m_lastValue;
        m_lastValue = newValue;
        return changed;
    }
    
    public void putDefaultValue() {
        Preferences.getInstance().putInt(getName(), m_default);
    }
    
    public int get() {
        return Preferences.getInstance().getInt(getName(), m_default);
    }
}
