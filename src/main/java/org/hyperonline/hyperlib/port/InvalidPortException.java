package org.hyperonline.hyperlib.port;

/**
 * An exception thrown when you attempt to assign hardware to a port that does not exist in the
 * robot map.
 *
 * @author James Hagborg
 */
@SuppressWarnings("serial")
public class InvalidPortException extends Exception {
  private int number;
  private Port.Type type;
  private String name;

  /**
   * Construct a new {@link InvalidPortException} with the given properties.
   *
   * @param number The number of the port which is invalid
   * @param type The type of port which is invalid
   * @param name The name of the use of the invalid port
   */
  public InvalidPortException(int number, Port.Type type, String name) {
    this.number = number;
    this.type = type;
    this.name = name;
  }

  public int getNumber() {
    return number;
  }

  public Port.Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public String getMessage() {
    return String.format(
        "%s cannot be assigned to %s #%d because it is not a valid port", name, type, number);
  }
}
