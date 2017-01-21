package org.usfirst.frc.team69.util.oi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
