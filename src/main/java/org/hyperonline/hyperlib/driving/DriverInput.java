package org.hyperonline.hyperlib.driving;

import java.util.function.DoubleSupplier;

/**
 * helpers for modifying joystick input from drivers
 *
 * @author Chris McGroarty
 */
public class DriverInput {

  /**
   * @param input speed to deadzone
   * @param deadband min speed required
   * @return the speed or 0 if it is too small
   */
  public static double deadband(double input, double deadband) {
    return (Math.abs(input) > deadband ? input : 0);
  }

  /**
   * @param input input speed to govern
   * @param speedlimit max speed allowed
   * @return speed limited input
   */
  public static double governor(double input, double speedlimit) {
    double sign = DriverInput.getSign(input);
    return (Math.abs(input) < speedlimit ? input : sign * speedlimit);
  }

  public static double governor(DoubleSupplier input, double speedlimit) {
    double sign = DriverInput.getSign(input.getAsDouble());
    return (Math.abs(input.getAsDouble()) < speedlimit ? input.getAsDouble() : sign * speedlimit);
  }

  /**
   * @param input the speed to square
   * @return squared speed
   */
  public static double squareInput(double input) {
    double sign = DriverInput.getSign(input);
    return sign * (input * input);
  }

  /**
   * @param input the speed to square
   * @return squared speed
   */
  public static double squareInput(DoubleSupplier input) {
    double sign = DriverInput.getSign(input.getAsDouble());
    return sign * (input.getAsDouble() * input.getAsDouble());
  }

  /**
   * @param input the speed to reduce
   * @param reduction amount to reduce by
   * @return reduced input
   */
  public static double reduceInput(double input, double reduction) {
    double sign = DriverInput.getSign(input);
    return sign * Math.abs(input) - Math.abs(reduction);
  }

  /**
   * @param input
   * @return
   */
  private static double getSign(double input) {
    return (input == 0 ? 0 : input / Math.abs(input));
  }

  /**
   * @param input speed to cube
   * @return cubed speed
   */
  public static double cubeInput(double input) {
    return input * input * input;
  }
}
