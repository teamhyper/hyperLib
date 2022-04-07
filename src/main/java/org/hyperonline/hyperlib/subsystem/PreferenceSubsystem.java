package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.hyperonline.hyperlib.pref.BooleanPreference;
import org.hyperonline.hyperlib.pref.HasPreferences;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Chris McGroarty
 */
public abstract class PreferenceSubsystem extends SubsystemBase
    implements PreferencesListener, HasPreferences {
  protected PreferencesSet m_prefs;
  protected BooleanPreference m_telemetryEnabled;
  private boolean m_isTelemetryEnabled;

  protected PreferenceSubsystem() {
    super();
    m_prefs = new PreferencesSet(this.getName(), this);
    this.initMyPreferences();
    postConfig();
  }

  /**
   * @deprecated SubsystemBase uses class.getSimpleName as default name
   * @param name string to use as the Subsystem's name
   */
  @Deprecated
  protected PreferenceSubsystem(String name) {
    this();
  }

  private void setTelemetryEnabled(boolean enabled) {
    if (enabled != m_isTelemetryEnabled) {
      m_isTelemetryEnabled = enabled;
      if (m_isTelemetryEnabled) onTelemetryEnable();
      else onTelemetryDisable();
    }
  }

  /**
   * runs when preferences in this PreferencesSet are updated always call
   * super.onPreferencesUpdated() when overriding, so the telemetry pref is correctly configured
   */
  public void onPreferencesUpdated() {
    this.setTelemetryEnabled(m_telemetryEnabled.get());
  }

  private void initMyPreferences() {
    m_telemetryEnabled = m_prefs.addBoolean("LiveWindow Telemetry", false);
    this.setTelemetryEnabled(m_telemetryEnabled.get());
    this.initPreferences();
  }

  protected void onTelemetryEnable() {
    applyToSendables(LiveWindow::enableTelemetry);
  }

  protected void onTelemetryDisable() {
    applyToSendables(LiveWindow::disableTelemetry);
  }

  public abstract void initPreferences();

  protected Stream<Sendable> getSendables() {
    return Stream.of(this);
  }

  protected void applyToSendables(Consumer<Sendable> sendableConsumer) {
    getSendables().forEach(sendableConsumer);
  }

  protected void postConfig() {
    onTelemetryDisable();
  }
}
