package org.hyperonline.hyperlib.pref;

import edu.wpi.first.wpilibj.Preferences;

/**
 * The {@link Preference} class is an abstract class representing a single
 * preference entry in the preferences file. This is used to perform common
 * operations, like checking if the preference is set, and putting the default
 * value.
 * 
 * Because {@link Preferences} in WPILib is not generic, we must use an abstract
 * class with subclasses for each type of preference.
 * 
 * @author James Hagborg
 * 
 * @see Preferences
 *
 */
public abstract class Preference {
    private String m_name;

    /**
     * Create a new {@link Preference} with the given name. No modification is
     * made yet to the preferences file.
     * 
     * @param name
     *            The string id of the preference
     */
    public Preference(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }

        m_name = name;
    }

    /**
     * The name is the string id of the preference, as it appears on the
     * dashboard and in the preferences file.
     * 
     * @return The name of the preference
     */
    public String getName() {
        return m_name;
    }

    /**
     * Check if a preference with the same name as this one already exists in
     * the preferences file. If not, then create the preference using the
     * default value.
     */
    public synchronized void putDefaultIfEmpty() {
        if (!Preferences.containsKey(m_name)) {
            putDefaultValue();
        }
    }

    /**
     * Check if the value has changed since last being checked, or if this is
     * the first time this method was called and the value has changed from the
     * default.
     * 
     * @return true if the value has changed, false otherwise
     */
    protected abstract boolean hasChanged();

    /**
     * Put an entry with the default value.
     */
    protected abstract void putDefaultValue();
}
