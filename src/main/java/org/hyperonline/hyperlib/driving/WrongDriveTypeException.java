package org.hyperonline.hyperlib.driving;


/**
 * This exception is thrown when a class implementing DriveParameters is called 
 * being passed an incompatible Drive type (i.e. PolarDriveParams given a DifferentialDrive)
 * 
 * @author Chris McGroarty
 *
 */
@SuppressWarnings("serial")
public class WrongDriveTypeException extends Exception {
	 /**
     * @see Exception#Exception()
     */
    public WrongDriveTypeException() {
    }
    /**
     * @see Exception#Exception(String)
     * @param msg
     *            A message string describing the exception
     */
    public WrongDriveTypeException(String msg) {
        super(msg);
    }

    /**
     * @see Exception#Exception(Throwable)
     * @param cause
     *            The exception which caused this one
     */
    public WrongDriveTypeException(Throwable cause) {
        super(cause);
    }

    /**
     * @see Exception#Exception(String, Throwable)
     * @param msg
     *            A message string describing the exception
     * @param cause
     *            The exception which caused this one
     */
    public WrongDriveTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
