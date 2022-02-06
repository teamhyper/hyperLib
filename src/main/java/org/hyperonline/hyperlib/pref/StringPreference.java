package org.hyperonline.hyperlib.pref;

import edu.wpi.first.wpilibj.Preferences;

/**
 * A class which represents a string-valued preference
 * 
 * @author James Hagborg
 *
 */
public class StringPreference extends Preference {
    private String m_lastValue;
    private final String m_default;

    /**
     * Create a {@link StringPreference} object tracking the preference with the
     * given name and default value. Calling this function does not yet modify
     * the preferences file.
     * 
     * @param name
     *            The string id of the preference
     * @param value
     *            The default value
     */
    public StringPreference(String name, String value) {
        super(name);

        if (value == null) {
            throw new NullPointerException("value == null");
        }

        m_lastValue = value;
        m_default = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean hasChanged() {
        String newValue = get();
        boolean changed = !newValue.equals(m_lastValue);
        m_lastValue = newValue;
        return changed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putDefaultValue() {
        Preferences.setString(getName(), m_default);
    }

    /**
     * Get the current value of the preferences file entry, or the default if no
     * entry exists.
     * 
     * @return The value of the preference
     * @see Preferences#getString(String, String)
     */
    public String get() {
        return Preferences.getString(getName(), m_default);
    }
}
