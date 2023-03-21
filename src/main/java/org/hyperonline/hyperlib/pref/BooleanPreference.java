package org.hyperonline.hyperlib.pref;

import edu.wpi.first.wpilibj.Preferences;

import java.util.function.BooleanSupplier;

/**
 * A class which represents a boolean-valued preference
 *
 * @author James Hagborg
 */
public class BooleanPreference extends Preference implements BooleanSupplier {
  private final boolean m_default;
  private boolean m_lastValue;

  /**
   * Create a {@link BooleanPreference} object tracking the preference with the given name and
   * default value. Calling this function does not yet modify the preferences file.
   *
   * @param name The string id of the preference
   * @param value The default value
   */
  public BooleanPreference(String name, boolean value) {
    super(name);
    m_lastValue = value;
    m_default = value;
  }

  /** {@inheritDoc} */
  @Override
  protected synchronized boolean hasChanged() {
    boolean newValue = get();
    boolean changed = newValue != m_lastValue;
    m_lastValue = newValue;
    return changed;
  }

  /** {@inheritDoc} */
  @Override
  protected void putDefaultValue() {
    Preferences.setBoolean(getName(), m_default);
  }

  /**
   * Get the current value of the preferences file entry, or the default if no entry exists.
   *
   * @return The value of the preference
   * @see Preferences#getBoolean(String, boolean)
   */
  public boolean get() {
    return Preferences.getBoolean(getName(), m_default);
  }

  @Override
  public boolean getAsBoolean() {
    return this.get();
  }
}
