package org.hyperonline.hyperlib.port;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The {@link Port} annotation is used to mark ports in the robot map. This allows RobotInspector to
 * parse the robot map, and do validation as well as draw diagrams.
 *
 * <p>This annotation is meant to be applied to static constants which represent port numbers.
 *
 * @author James Hagborg
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Port {
  /**
   * The type of port used on the RIO
   *
   * @return the type of the port
   */
  public Type type();

  /**
   * The {@link Type} enum specifies what type of port on the RIO is used.
   *
   * @author James Hagborg
   */
  public enum Type {
    PWM,
    DIO,
    ANALOG,
    RELAY,
    PCM,
    CAN,
    USB
  }
}
