package org.hyperonline.hyperlib.pid;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.PreferencesListener;
import org.hyperonline.hyperlib.pref.PreferencesSet;

import java.util.function.DoubleSupplier;

public abstract class PrefPIDController implements PIDControlled, PreferencesListener, Sendable {
  protected PreferencesSet m_prefs;
  protected final DoublePreference m_P_pref, m_I_pref, m_D_pref, m_tolerance_pref;
  protected DoublePreference m_minOut_pref, m_maxOut_pref;

  protected DoubleSupplier m_pidOffsetBase = () -> 0;
  protected double m_minOut = -1, m_maxOut = 1;
  protected boolean m_enabled = false;
  protected double m_conversionFactor = 1;

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

  public double getMaxOutput() {
    return m_maxOut;
  }

  public double getMinOutput() {
    return m_minOut;
  }

  protected void updateOutputRange() {
    if (m_minOut_pref != null && m_minOut_pref.get() >= -1) {
      m_minOut = m_minOut_pref.get();
    } else {
      m_minOut = -1;
    }

    if (m_maxOut_pref != null && m_maxOut_pref.get() <= 1) {
      m_maxOut = m_maxOut_pref.get();
    } else {
      m_maxOut = 1;
    }
  }

  public void setOffset(DoubleSupplier offset) {
    this.m_pidOffsetBase = offset;
  }

  public void setConversionFactor(double factor) {
    if (factor != 0) {
      m_conversionFactor = factor;
    } else {
      throw new IllegalArgumentException("factor is used as a divisor and cannot be zero");
    }
  }

  public void setOutputRange(double minOut, double maxOut) {
    m_minOut_pref = m_prefs.addDouble("MinOut", minOut);
    m_maxOut_pref = m_prefs.addDouble("MaxOut", maxOut);
    this.updateOutputRange();
  }

  public double nativeToFriendly(double nativeUnits) {
    return nativeUnits * m_conversionFactor;
  }

  public double friendlyToNative(double friendlyUnits) {
    return friendlyUnits / m_conversionFactor;
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
    this.updateOutputRange();
  }

  protected abstract void setPID(double Kp, double Ki, double Kd);

  @Override
  public boolean onTarget(double target) {
    return Math.abs(getFromSource() - target) <= m_tolerance_pref.get();
  }

  @Override
  public boolean isAbove(double target) {
    return getFromSource() >= target;
  }

  @Override
  public boolean isBelow(double target) {
    return getFromSource() <= target;
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
    builder.addDoubleProperty("Friendly Value", this::getFromSource, null);
    builder.addDoubleProperty("Native Value", () -> friendlyToNative(this.getFromSource()), null);
    builder.addDoubleProperty("Value Minus Offset", () -> getFromSource() - m_pidOffsetBase.getAsDouble(), null);
  }

  protected abstract void setSmartDashboardType(SendableBuilder builder);

  public abstract void disableContinuousInput();
  public abstract void enableContinuousInput(double minIn, double maxIn);
}
