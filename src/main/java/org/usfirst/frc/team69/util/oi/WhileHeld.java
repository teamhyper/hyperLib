package org.usfirst.frc.team69.util.oi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a command should run while a button is held.
 * 
 * @author James Hagborg
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface WhileHeld {
    /**
     * The number of the button (starting at 1) to bind to
     */
    int value();
}
