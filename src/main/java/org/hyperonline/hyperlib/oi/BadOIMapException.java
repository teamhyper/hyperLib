package org.hyperonline.hyperlib.oi;

/**
 * This exception is thrown when the class passed as a map to {@link OI} is
 * invalid. This should only be thrown inside the RobotInspector program. If an
 * invalid configuration is detected at runtime, we report an error to the
 * dashboard and ignore the invalid part.
 * 
 * @author James Hagborg
 *
 */
@SuppressWarnings("serial")
public class BadOIMapException extends Exception {

    /**
     * @see Exception#Exception()
     */
    public BadOIMapException() {
    }

    /**
     * @see Exception#Exception(String)
     * @param msg
     *            A message string describing the exception
     */
    public BadOIMapException(String msg) {
        super(msg);
    }

    /**
     * @see Exception#Exception(Throwable)
     * @param cause
     *            The exception which caused this one
     */
    public BadOIMapException(Throwable cause) {
        super(cause);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     * @param msg
     *            A message string describing the exception
     * @param cause
     *            The exception which caused this one
     */
    public BadOIMapException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
