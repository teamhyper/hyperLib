package org.usfirst.frc.team69.util.pref;

import java.util.ArrayList;
import java.util.HashSet;

import org.usfirst.frc.team69.util.PeriodicScheduler;

/**
 * This class provides a set of preferences, for use in a subsystem.
 * TODO: finish documenting this
 * 
 * @author James Hagborg
 *
 */
public class PreferencesSet {
    public static final String SEPARATOR = " ";
    
    private final String m_name;
    private final PreferencesListener m_listener;
    
    private ArrayList<Preference> m_preferences = new ArrayList<Preference>();
    private HashSet<String> m_prefNames = new HashSet<String>();
    
    private static HashSet<String> setNames = new HashSet<String>();

    public PreferencesSet(String name, PreferencesListener listener) {
        if (name == null) {
            throw new NullPointerException("name == null");
        } else if (listener == null) {
            throw new NullPointerException("listener == null");
        } if (setNames.contains(name)) {
            throw new IllegalStateException("A PreferencesSet named " + name + " already exists");
        }
        
        setNames.add(name);
        
        m_name = name;
        m_listener = listener;
        
        PeriodicScheduler.getInstance().addEvent(this::checkForUpdates);
    }
    
    public PreferencesSet(String name) {
        this(name, () -> {});
    }
    
    public String getName() {
        return m_name;
    }
    
    private void checkForUpdates() {
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
            throw new IllegalStateException("A preference with the name " + name + " already exists");
        }
    }
    
    private void addPreference(String name, Preference pref) {
        pref.putDefaultIfEmpty();
        m_preferences.add(pref);
        m_prefNames.add(name);
    }
    
    public DoublePreference addDouble(String name, double value) {
        checkName(name);
        DoublePreference pref = new DoublePreference(m_name + SEPARATOR + name, value);
        addPreference(name, pref);
        return pref;
    }
    
    public IntPreference addInt(String name, int value) {
        checkName(name);
        IntPreference pref = new IntPreference(m_name + SEPARATOR + name, value);
        addPreference(name, pref);
        return pref;
    }
    
    public BooleanPreference addBoolean(String name, boolean value) {
        checkName(name);
        BooleanPreference pref = new BooleanPreference(m_name + SEPARATOR + name, value);
        addPreference(name, pref);
        return pref;
    }
    
    public StringPreference addString(String name, String value) {
        checkName(name);
        StringPreference pref = new StringPreference(m_name + SEPARATOR + name, value);
        addPreference(name, pref);
        return pref;
    }
}
