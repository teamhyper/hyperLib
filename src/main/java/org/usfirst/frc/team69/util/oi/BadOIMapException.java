package org.usfirst.frc.team69.util.oi;

/**
 * This exception is thrown when the class passed as a map to {@link OI} is
 * invalid.  This should only be thrown inside the RobotInspector program.
 * If an invalid configuration is detected at runtime, we report an error
 * to the dashboard and ignore the invalid part.
 * 
 * @author James Hagborg
 *
 */
@SuppressWarnings("serial")
public class BadOIMapException extends Exception {

    public BadOIMapException() {
        // TODO Auto-generated constructor stub
    }

    public BadOIMapException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public BadOIMapException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public BadOIMapException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public BadOIMapException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
        // TODO Auto-generated constructor stub
    }

}
