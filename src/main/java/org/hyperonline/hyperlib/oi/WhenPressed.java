package org.hyperonline.hyperlib.oi;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.XboxController;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to indicate that a command should run when a button is pressed.
 *
 * @author James Hagborg
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
