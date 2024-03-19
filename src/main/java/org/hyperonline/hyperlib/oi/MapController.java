package org.hyperonline.hyperlib.oi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The MapJoystick annotation specifies how a joystick should be used in the operator interface.
 * This annotation is read by the runtime as well as by RobotInspector. This should be applied to a
 * public static class inside the OI map class. For example:
 *
 * <pre>
 * public class OIMap {
 *     &#64;MapController(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_2_AXIS)
 *     public static class LeftDriver {
 *         // specify buttons/commands here
 *     }
 * }
 * </pre>
 *
 * @author James Hagborg
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MapController {
  /**
   * The port the controller is connected to.
   *
   * @return The port the controller is connected to.
   */
  int port();

  /**
   * The role the controller serves, e.g. right driver, left operator, etc.
   *
   * @return The role the controller serves.
   * @see OI#leftDriver()
   * @see OI#rightDriver()
   * @see OI#leftOperator()
   * @see OI#rightOperator()
   */
  Role role();

  /**
   * The physical model of controller used. This determines how the diagrams are mapped, and what
   * button numbers are allowed.
   *
   * @return The type of the controller.
   */
  Type type();

  /**
   * The role of a controller determines how it is accessed in {@link OI}. If the controller layout does
   * not conform to left/right driver/operator, then use OTHER and access joysticks using {@link
   * OI#getControllers(int)}.
   *
   * @author James Hagborg
   */
  public enum Role {
    LEFT_DRIVER,
    RIGHT_DRIVER,
    LEFT_OPERATOR,
    RIGHT_OPERATOR,
    OTHER
  }

  /**
   * The physical type of the controller. This determines the picture to use and the number of buttons
   * allowed.
   *
   * @author James Hagborg
   */
  public enum Type {
    LOGITECH_3_AXIS,
    LOGITECH_2_AXIS,
    APEM_HF45S10U,
    VKB_GLADIATOR_NXT_EVO_SCE_LEFT,
    VKB_GLADIATOR_NXT_EVO_SCE_RIGHT,
    XBOX,
    PS4,
    OTHER
  }
}
