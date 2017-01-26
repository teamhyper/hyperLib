package org.usfirst.frc.team69.util.oi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The MapJoystick annotation specifies how a joystick should be used in the
 * operator interface.  This annotation is read by the runtime as well as by
 * RobotInspector.  This should be applied to a public static class inside the
 * OI map class.  For example:
 * <pre>
 * public class OIMap {
 *      &#64;MapJoystick(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_DUAL_ACTION)
 *      public static class LeftDriver {
 *          // specify buttons/commands here
 *      }
 * }
 * </pre>
 * 
 * @author James Hagborg
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MapJoystick {
    int port();
    Role role();
    Type type();
    
    public enum Role {
        LEFT_DRIVER,
        RIGHT_DRIVER,
        LEFT_OPERATOR,
        RIGHT_OPERATOR,
        OTHER
    }
    
    public enum Type {
        LOGITECH_3_AXIS,
        LOGITECH_2_AXIS
    }
}
