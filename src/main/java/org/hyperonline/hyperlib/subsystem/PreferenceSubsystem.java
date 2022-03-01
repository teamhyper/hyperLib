package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.hyperonline.hyperlib.pref.HasPreferences;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

public abstract class PreferenceSubsystem extends SubsystemBase
    implements PreferencesListener, HasPreferences {
  protected PreferencesSet m_prefs;

  protected PreferenceSubsystem(String name) {
    super();
    this.setName(name);
    m_prefs = new PreferencesSet(this.getName(), this);
    this.initPreferences();
  }

  public void onPreferencesUpdated() {}

  public abstract void initPreferences();
}
