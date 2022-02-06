package org.hyperonline.hyperlib.pid;

import java.util.function.DoubleSupplier;

/**
 * This class is a {@link PrefPIDController} with no output. This is useful if
 * calculating the PID output is only part of calculating the outputs to
 * actuators.
 * 
 * @author James Hagborg
 *
 */
public class PassivePIDController extends PrefPIDController {

    /**
     * @see PrefPIDController#PrefPIDController(String, double, double, double,
     *      DoubleSupplier, java.util.function.DoubleConsumer)
     * @param prefString
     *            A string to identify preferences
     * @param Kp
     *            the default value for P
     * @param Ki
     *            the default value for I
     * @param Kd
     *            the default value for D
     * @param source
     *            the source of feedback
     */
    public PassivePIDController(String prefString, double Kp, double Ki,
            double Kd, DoubleSupplier source) {
        super(prefString, Kp, Ki, Kd, source, (x) -> {});
    }

    /**
     * @see PrefPIDController#PrefPIDController(String, double, double, double,
     *      DoubleSupplier, java.util.function.DoubleConsumer, double)
     * @param prefString
     *            A string to identify preferences
     * @param Kp
     *            the default value for P
     * @param Ki
     *            the default value for I
     * @param Kd
     *            the default value for D
     * @param source
     *            the source of feedback
     * @param period
     *            the period to run the PID controller at
     */
    public PassivePIDController(String prefString, double Kp, double Ki,
            double Kd, DoubleSupplier source, double period) {
        super(prefString, Kp, Ki, Kd, source, (x) -> {}, period);
    }

    /**
     * @see PrefPIDController#PrefPIDController(String, double, double, double,
     *      DoubleSupplier, java.util.function.DoubleConsumer)
     * @param prefString
     *            A string to identify preferences
     * @param Kp
     *            the default value for P
     * @param Ki
     *            the default value for I
     * @param Kd
     *            the default value for D
     * @param Kf
     *            the default value for F
     * @param source
     *            the source of feedback
     */
    public PassivePIDController(String prefString, double Kp, double Ki,
            double Kd, double Kf, DoubleSupplier source) {
        super(prefString, Kp, Ki, Kd, source, (x) -> {});
    }

    /**
     * @see PrefPIDController#PrefPIDController(String, double, double, double,
     *      double, DoubleSupplier, java.util.function.DoubleConsumer, double)
     * @param prefString
     *            A string to identify preferences
     * @param Kp
     *            the default value for P
     * @param Ki
     *            the default value for I
     * @param Kd
     *            the default value for D
     * @param Kf
     *            the default value for F
     * @param source
     *            the source of feedback
     * @param period
     *            the period to run the PID controller at
     */
    public PassivePIDController(String prefString, double Kp, double Ki,
            double Kd, double Kf, DoubleSupplier source, double period) {
        super(prefString, Kp, Ki, Kd, Kf, source, (x) -> {}, period);
    }

}
