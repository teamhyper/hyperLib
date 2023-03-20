package org.hyperonline.hyperlib.driving;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;

import java.util.function.DoubleSupplier;

/**
 * helpers for modifying joystick input from drivers
 *
 * @author Chris McGroarty
 */
public class DriverInput {

    /**
     * @param input    speed to deadzone
     * @param deadband min speed required
     * @return the speed or 0 if it is too small
     */
    public static double deadband(double input, double deadband) {
        return MathUtil.applyDeadband(input, deadband);
    }

    /**
     * @param input      input speed to govern
     * @param speedlimit max speed allowed
     * @return speed limited input
     */
    public static double governor(double input, double speedlimit) {
        return MathUtil.clamp(input, -speedlimit, speedlimit);
    }

    public static double governor(DoubleSupplier input, double speedlimit) {
        return governor(input.getAsDouble(), speedlimit);
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
     * @param ff    amount to reduce by
     * @param low   minimum speed allowed
     * @param high  maximum speed allowed
     * @return reduced input
     */
    public static double inverseFeedforward(double input, double ff, double low, double high) {
        if (input == 0) {
            return 0;
        }
        double sign = DriverInput.getSign(input);
        return MathUtil.clamp(sign * (Math.abs(input) - Math.abs(ff)), low, high);
    }

    /**
     * @param input the speed to reduce
     * @param ff    amount to reduce by
     * @return reduced input
     */
    public static double inverseFeedforward(double input, double ff) {
        return inverseFeedforward(input, ff, -1.0, 1.0);
    }


    /**
     * @param input the speed to feedforward
     * @param ff    amount to increase by
     * @return feedforwarded input
     */
    public static double feedforward(double input, double ff) {
        return feedforward(input, ff, -1.0, 1.0);
    }

    /**
     * @param input the speed to feedforward
     * @param ff    amount to increase by
     * @param low   minimum speed allowed
     * @param high  maximum speed allowed
     * @return feedforwarded input
     */
    public static double feedforward(double input, double ff, double low, double high) {
        if (input == 0) {
            return 0;
        }
        double sign = DriverInput.getSign(input);
        return MathUtil.clamp(sign * (Math.abs(input) + Math.abs(ff)), low, high);
    }

    /**
     * helper that resets a SlewRateLimiter if the target speed is zero,
     * allowing the drivetrain or mechanism to stop instead of decelerating at the given rate
     *
     * @param speed  target speed
     * @param filter SlewRateLimiter to calculate value from
     * @param reset  should the SlewRateLimiter be reset before calculating
     * @return rate limited value or 0
     */
    public static double filterAllowZero(double speed, SlewRateLimiter filter, boolean reset) {
        if (reset) {
            filter.reset(speed);
        }

        return filter.calculate(speed);
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
