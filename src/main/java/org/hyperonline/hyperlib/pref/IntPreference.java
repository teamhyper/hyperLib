package org.hyperonline.hyperlib.pref;

import edu.wpi.first.wpilibj.Preferences;

import java.util.function.IntSupplier;

/**
 * A class which represents an integer-valued preference
 *
 * @author James Hagborg
 */
public class IntPreference extends Preference implements IntSupplier {
  private final int m_default;
  private int m_lastValue;

  /**
   * Create a {@link IntPreference} object tracking the preference with the given name and default
   * value. Calling this function does not yet modify the preferences file.
   *
   * @param name The string id of the preference
   * @param value The default value
   */
  public IntPreference(String name, int value) {
    super(name);
    m_lastValue = value;
    m_default = value;
  }

  /** {@inheritDoc} */
  @Override
  protected synchronized boolean hasChanged() {
    int newValue = get();
    boolean changed = newValue != m_lastValue;
    m_lastValue = newValue;
    return changed;
  }

  /** {@inheritDoc} */
  @Override
  protected void putDefaultValue() {
    Preferences.setInt(getName(), m_default);
  }

  /**
   * Get the current value of the preferences file entry, or the default if no entry exists.
   *
   * @return The value of the preference
   * @see Preferences#getInt(String, int)
   */
  public int get() {
    return Preferences.getInt(getName(), m_default);
  }

  @Override
  public int getAsInt() {
    return this.get();
  }
}
