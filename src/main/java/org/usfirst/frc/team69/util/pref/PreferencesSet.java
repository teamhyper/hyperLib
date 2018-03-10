package org.usfirst.frc.team69.util.pref;

import java.util.ArrayList;
import java.util.HashSet;

import org.usfirst.frc.team69.util.PeriodicScheduler;

/**
 * This class provides a set of related preferences, with some functionality
 * grouped together. Each preference is prefixed by the same name, so that the
 * preferences all show up near each other in the dashboard.
 * 
 * Example use-cases are subsystems, PID controllers, and specific tasks like
 * vision and driving, which may have many related preferences.
 * 
 * It is safe to share this object, as well as the Preference objects it
 * creates, among multiple threads.
 * 
 * @author James Hagborg
 *
 */
public class PreferencesSet {
    public static final String SEPARATOR = " ";

    private final String m_name;
    private final PreferencesListener m_listener;

    private final ArrayList<Preference> m_preferences = new ArrayList<>();
    private final HashSet<String> m_prefNames = new HashSet<>();

    private static final HashSet<String> setNames = new HashSet<>();

    /**
     * Check the set name does not exist and add it if it does. This needs to be
     * synchronized to access the global list.
     * 
     * @param name
     *            The name to add and check
     */
    private synchronized static void registerSetName(String name) {
        if (setNames.contains(name)) {
            throw new IllegalStateException(
                    "A PreferencesSet named " + name + " already exists");
        }

        setNames.add(name);
    }

    /**
     * Construct a new {@link PreferencesSet} object with the given name and
     * {@link PreferencesListener}. Each time a preference in the set is
     * updated, the listener will be called.
     * 
     * @param name
     *            The prefix to use for all preferences in the set. This name
     *            must be unique among all {@link PreferencesSet} objects.
     * @param listener
     *            The listener to call each time an update happens
     */
    public PreferencesSet(String name, PreferencesListener listener) {
        if (name == null) {
            throw new NullPointerException("name == null");
        } else if (listener == null) {
            throw new NullPointerException("listener == null");
        }

        registerSetName(name);

        m_name = name;
        m_listener = listener;

        PeriodicScheduler.getInstance().addEvent(this::checkForUpdates);
    }

    /**
     * Construct a new {@link PreferencesSet} object with the given name.
     * 
     * @param name
     *            The prefix to use for all preferences in the set. This name
     *            must be unique among all {@link PreferencesSet} objects.
     */
    public PreferencesSet(String name) {
        this(name, () -> {
        });
    }

    /**
     * The name of the set is the prefix used for each preference
     * 
     * @return The name of the set
     */
    public String getName() {
        return m_name;
    }

    private synchronized void checkForUpdates() {
        boolean hasChanged = false;

        for (Preference pref : m_preferences) {
            if (pref.hasChanged()) {
                hasChanged = true;
            }
        }

        if (hasChanged) {
            m_listener.onPreferencesUpdated();
        }
    }

    private void checkName(String name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }

        if (m_prefNames.contains(name)) {
            throw new IllegalStateException(
                    "A preference with the name " + name + " already exists");
        }
    }

    private void addPreference(String name, Preference pref) {
        pref.putDefaultIfEmpty();
        m_preferences.add(pref);
        m_prefNames.add(name);
    }

    /**
     * Create a new {@link DoublePreference} and add it to the set
     * 
     * @param name
     *            The name of the preference
     * @param value
     *            The default value
     * @return The newly created preference
     */
    public synchronized DoublePreference addDouble(String name, double value) {
        checkName(name);
        DoublePreference pref = new DoublePreference(m_name + SEPARATOR + name,
                value);
        addPreference(name, pref);
        return pref;
    }

    /**
     * Create a new {@link IntPreference} and add it to the set
     * 
     * @param name
     *            The name of the preference
     * @param value
     *            The default value
     * @return The newly created preference
     */
    public synchronized IntPreference addInt(String name, int value) {
        checkName(name);
        IntPreference pref = new IntPreference(m_name + SEPARATOR + name,
                value);
        addPreference(name, pref);
        return pref;
    }

    /**
     * Create a new {@link BooleanPreference} and add it to the set
     * 
     * @param name
     *            The name of the preference
     * @param value
     *            The default value
     * @return The newly created preference
     */
    public synchronized BooleanPreference addBoolean(String name,
                                                     boolean value) {
        checkName(name);
        BooleanPreference pref = new BooleanPreference(
                m_name + SEPARATOR + name, value);
        addPreference(name, pref);
        return pref;
    }

    /**
     * Create a new {@link StringPreference} and add it to the set
     * 
     * @param name
     *            The name of the preference
     * @param value
     *            The default value
     * @return The newly created preference
     */
    public synchronized StringPreference addString(String name, String value) {
        checkName(name);
        StringPreference pref = new StringPreference(m_name + SEPARATOR + name,
                value);
        addPreference(name, pref);
        return pref;
    }
    
    public synchronized ScalarPreference addScalar(String name, String components, double... value) {
        checkName(name);
        return new ScalarPreference(this, name, components, value);
    }
}
