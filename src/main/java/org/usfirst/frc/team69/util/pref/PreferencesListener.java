package org.usfirst.frc.team69.util.pref;

/**
 * A {@link PreferencesListener} is something which needs to run code each
 * time the preferences file changes.  This is useful for telling hardware
 * and PID controllers to adjust their settings accordingly.
 * 
 * @author James Hagborg
 *
 */
@FunctionalInterface
public interface PreferencesListener {
    /**
     * Runs once each time a preference is changed.
     */
    void onPreferencesUpdated();
}
