package org.usfirst.frc.team69.util.port;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Port {
    public enum Type {
        PWM,
        DIO,
        ANALOG,
        RELAY,
        PCM,
        CAN
    }

    public Type type();
}
