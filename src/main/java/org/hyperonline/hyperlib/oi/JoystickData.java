package org.hyperonline.hyperlib.oi;

import org.hyperonline.hyperlib.oi.MapJoystick.Role;
import org.hyperonline.hyperlib.oi.MapJoystick.Type;

import java.util.Collections;
import java.util.List;

/**
 * This class is an intermediate data structure used in {@link OI} and in RobotInspector. It holds
 * information parsed out of the OI map for a particular joystick.
 *
 * @author James Hagborg
 */
public class JoystickData {
  private int m_port;
  private Role m_role;
  private Type m_type;
  private Class<?> m_class;
  private List<ButtonData> m_buttons;

  /**
   * Construct a new JoystickData object with the given parameters
   *
   * @param port the port the joystick is plugged into
   * @param role the role the joystick fills
   * @param type the physical type of joystick
   * @param mapClass the class inside the oi map representing the joystick
   * @param buttons a list of buttons which are part of the joystick. This object makes an immutable
   *     reference to the list passed in.
   */
  public JoystickData(int port, Role role, Type type, Class<?> mapClass, List<ButtonData> buttons) {
    m_port = port;
    m_role = role;
    m_type = type;
    m_class = mapClass;
    m_buttons = Collections.unmodifiableList(buttons);
  }

  /**
   * Get the port the joystick is plugged into
   *
   * @return the port the joystick is plugged into
   */
  public int port() {
    return m_port;
  }

  /**
   * Get the role of the joystick
   *
   * @return the role of the joystick
   */
  public Role role() {
    return m_role;
  }

  /**
   * Get the physical type of the joystick
   *
   * @return the type of the joystick
   */
  public Type type() {
    return m_type;
  }

  /**
   * Get a string representation of the name of the joystick. This is the simple name of the class
   * representing the joystick
   *
   * @return The name of the joystick
   */
  public String name() {
    return m_class.getSimpleName();
  }

  /**
   * Get the class used to declare the joystick. This should be a public static class in the oi map
   * which had a {@link MapJoystick} annotation
   *
   * @return The class of the joystick
   */
  public Class<?> mapClass() {
    return m_class;
  }

  /**
   * Get an immutable list of buttons on the joystick. This is actually a list of button/command
   * mappings, so a single button number may be counted twice if there are multiple commands on it.
   *
   * @return A list of button/command mappings
   */
  public List<ButtonData> buttons() {
    return m_buttons;
  }
}
