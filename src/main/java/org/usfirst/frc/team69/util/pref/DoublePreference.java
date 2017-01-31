package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

/**
 * A class which represents a double-valued preference
 * 
 * @author James Hagborg
 *
 */
public class DoublePreference extends Preference {
    private double m_lastValue;
    private double m_default;
    
    /**
     * Create a {@link DoublePreference} object tracking the preference with
     * the given name and default value.  Calling this function does not
     * yet modify the preferences file.
     * 
     * @param name The string id of the preference
     * @param value The default value
     */
    public DoublePreference(String name, double value) {
        super(name);
        m_lastValue = value;
        m_default = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChanged() {
        double newValue = get();
        boolean changed = newValue != m_lastValue;
        m_lastValue = newValue;
        return changed;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void putDefaultValue() {
        Preferences.getInstance().putDouble(getName(), m_default);
    }
    
    /**
     * Get the current value of the preferences file entry, or the
     * default if no entry exists.
     * 
     * @return The value of the preference
     * @see Preferences#getDouble(String, double)
     */
    public double get() {
        return Preferences.getInstance().getDouble(getName(), m_default);
    }
}
