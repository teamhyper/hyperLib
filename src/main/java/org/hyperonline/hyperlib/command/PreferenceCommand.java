package org.hyperonline.hyperlib.command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

public abstract class PreferenceCommand extends CommandBase implements PreferencesListener {
  protected PreferencesSet m_prefs;

  public PreferenceCommand(String name) {
    super();
    this.setName(name);
    m_prefs = new PreferencesSet(this.getName(), this);
    this.initPreferences();
  }

  @Override
  public void onPreferencesUpdated() {}

  protected abstract void initPreferences();
}
