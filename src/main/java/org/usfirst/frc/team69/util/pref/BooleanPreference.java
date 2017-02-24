package org.usfirst.frc.team69.util.pref;

import edu.wpi.first.wpilibj.Preferences;

/**
 * A class which represents a boolean-valued preference
 * 
 * @author James Hagborg
 *
 */
public class BooleanPreference extends Preference {
    private boolean m_lastValue;
    private final boolean m_default;
    
    /**
     * Create a {@link BooleanPreference} object tracking the preference with
     * the given name and default value.  Calling this function does not
     * yet modify the preferences file.
     * 
     * @param name The string id of the preference
     * @param value The default value
     */
    public BooleanPreference(String name, boolean value) {
        super(name);
        m_lastValue = value;
        m_default = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean hasChanged() {
        boolean newValue = get();
        boolean changed = newValue != m_lastValue;
        m_lastValue = newValue;
        return changed;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void putDefaultValue() {
        Preferences.getInstance().putBoolean(getName(), m_default);
    }
    
    /**
     * Get the current value of the preferences file entry, or the
     * default if no entry exists.
     * 
     * @return The value of the preference
     * @see Preferences#getBoolean(String, boolean)
     */
    public boolean get() {
        return Preferences.getInstance().getBoolean(getName(), m_default);
    }
}
