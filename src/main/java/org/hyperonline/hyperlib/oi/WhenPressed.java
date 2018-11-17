package org.hyperonline.hyperlib.oi;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a command should run when a button is pressed.
 * 
 * @author James Hagborg
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface WhenPressed {
    /**
     * The number of the button (starting at 1) to bind to
     * 
     * @return The number of the button.
     */
    int value();
}
