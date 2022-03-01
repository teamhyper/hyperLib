package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

public abstract class PrefPIDController implements PIDControlled, PreferencesListener, Sendable {
  protected PreferencesSet m_prefs;
  protected DoublePreference m_P_pref, m_I_pref, m_D_pref, m_tolerance_pref;
  protected boolean m_enabled = false;
  protected double m_nativeRatio = 1;

  public PrefPIDController(String name, double Kp, double Ki, double Kd, double tolerance) {
    m_prefs = new PreferencesSet(name + " PID", this);
    m_P_pref = m_prefs.addDouble("P", Kp);
    m_I_pref = m_prefs.addDouble("I", Ki);
    m_D_pref = m_prefs.addDouble("D", Kd);
    m_tolerance_pref = m_prefs.addDouble("Tolerance", tolerance);
  }

  @Override
  public void enable() {
    m_enabled = true;
  }

  public void setRatio(double ratio) {
    // we cannot divide by zero, so if we are given 0, use 1 instead
    m_nativeRatio = ratio == 0 ? 1 : ratio;
  }

  public double nativeToFriendly(double nativeUnits) {
    return nativeUnits / m_nativeRatio;
  }

  public double friendlyToNative(double friendlyUnits) {
    return friendlyUnits * m_nativeRatio;
  }

  @Override
  public void disable() {
    m_enabled = false;
  }

  @Override
  public boolean isEnabled() {
    return m_enabled;
  }

  @Override
  public void onPreferencesUpdated() {
    this.setPID(m_P_pref.get(), m_I_pref.get(), m_D_pref.get());
    this.setTolerance(m_tolerance_pref.get());
  }

  protected abstract void setPID(double Kp, double Ki, double Kd);

  @Override
  public boolean onTarget(double target) {
    return Math.abs(nativeToFriendly(getFromSource()) - target) <= m_tolerance_pref.get();
  }

  @Override
  public boolean isAbove(double target) {
    return nativeToFriendly(getFromSource()) >= target;
  }

  @Override
  public boolean isBelow(double target) {
    return nativeToFriendly(getFromSource()) <= target;
  }

  public double getTolerance() {
    return m_tolerance_pref.get();
  }

  protected abstract void setTolerance(double tolerance);

  @Override
  public void initSendable(SendableBuilder builder) {
    setSmartDashboardType(builder);
    builder.addBooleanProperty("Enabled", this::isEnabled, null);
    builder.addBooleanProperty("On Target", this::onTarget, null);
    builder.addDoubleProperty("Friendly Value", () -> nativeToFriendly(getFromSource()), null);
  }

  protected abstract void setSmartDashboardType(SendableBuilder builder);
}
