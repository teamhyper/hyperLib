package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

public class DoublePreference extends Preference {
    private double m_lastValue;
    private double m_default;
    
    public DoublePreference(String name, double value) {
        super(name);
        m_lastValue = value;
        m_default = value;
    }
    
    public boolean hasChanged() {
        double newValue = get();
        boolean changed = newValue != m_lastValue;
        m_lastValue = newValue;
        return changed;
    }
    
    public void putDefaultValue() {
        Preferences.getInstance().putDouble(getName(), m_default);
    }
    
    public double get() {
        return Preferences.getInstance().getDouble(getName(), m_default);
    }
}
