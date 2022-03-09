package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.hyperonline.hyperlib.pref.HasPreferences;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

/**
 * @author Chris McGroarty
 */
public abstract class PreferenceSubsystem extends SubsystemBase
    implements PreferencesListener, HasPreferences {
  protected PreferencesSet m_prefs;

  protected PreferenceSubsystem() {
    super();
    m_prefs = new PreferencesSet(this.getName(), this);
    this.initPreferences();
  }


  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name
   * @param name string to use as the Subsystem's name
   */
  @Deprecated
  protected PreferenceSubsystem(String name) {
    this();
  }

  public void onPreferencesUpdated() {}

  public abstract void initPreferences();
}
