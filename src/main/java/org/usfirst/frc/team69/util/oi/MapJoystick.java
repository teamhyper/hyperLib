package org.usfirst.frc.team69.util.oi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The MapJoystick annotation specifies how a joystick should be used in the
 * operator interface. This annotation is read by the runtime as well as by
 * RobotInspector. This should be applied to a public static class inside the OI
 * map class. For example:
 * 
 * <pre>
 * public class OIMap {
 *     &#64;MapJoystick(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_DUAL_ACTION)
 *     public static class LeftDriver {
 *         // specify buttons/commands here
 *     }
 * }
 * </pre>
 * 
 * @author James Hagborg
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MapJoystick {
    /**
     * The port the joystick is connected to.
     * 
     * @return The port the joystick is connected to.
     */
    int port();

    /**
     * The role the joystick serves, e.g. right driver, left operator, etc.
     * 
     * @return The role the joystick serves.
     * @see OI#leftDriver()
     * @see OI#rightDriver()
     * @see OI#leftOperator()
     * @see OI#rightOperator()
     */
    Role role();

    /**
     * The physical model of joystick used. This determines how the diagrams are
     * mapped, and what button numbers are allowed.
     * 
     * @return The type of the joystick.
     */
    Type type();

    /**
     * The role of a joystick determines how it is accessed in {@link OI}. If
     * the joystick layout does not conform to left/right driver/operator, then
     * use OTHER and access joysticks using {@link OI#getJoystick(int)}.
     * 
     * @author James Hagborg
     *
     */
    public enum Role {
        LEFT_DRIVER, RIGHT_DRIVER, LEFT_OPERATOR, RIGHT_OPERATOR, OTHER
    }

    /**
     * The physical type of a joystick. This determines the picture to use and
     * the number of buttons allowed.
     * 
     * @author James Hagborg
     *
     */
    public enum Type {
        LOGITECH_3_AXIS, LOGITECH_2_AXIS
    }
}
