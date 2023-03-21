package org.hyperonline.hyperlib.pref;

import edu.wpi.first.wpilibj.Preferences;

import java.util.function.DoubleSupplier;

/**
 * A class which represents a double-valued preference
 *
 * @author James Hagborg
 */
public class DoublePreference extends Preference implements DoubleSupplier {
  private final double m_default;
  private double m_lastValue;

  /**
   * Create a {@link DoublePreference} object tracking the preference with the given name and
   * default value. Calling this function does not yet modify the preferences file.
   *
   * @param name The string id of the preference
   * @param value The default value
   */
  public DoublePreference(String name, double value) {
    super(name);
    m_lastValue = value;
    m_default = value;
  }

  /** {@inheritDoc} */
  @Override
  protected synchronized boolean hasChanged() {
    double newValue = get();
    boolean changed = newValue != m_lastValue;
    m_lastValue = newValue;
    return changed;
  }

  /** {@inheritDoc} */
  @Override
  protected void putDefaultValue() {
    Preferences.setDouble(getName(), m_default);
  }

  /**
   * Get the current value of the preferences file entry, or the default if no entry exists.
   *
   * @return The value of the preference
   * @see Preferences#getDouble(String, double)
   */
  public double get() {
    return Preferences.getDouble(getName(), m_default);
  }

  @Override
  public double getAsDouble() {
    return this.get();
  }
}
