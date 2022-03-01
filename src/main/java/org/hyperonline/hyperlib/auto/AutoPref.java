package org.hyperonline.hyperlib.auto;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @author James
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface AutoPref {}
