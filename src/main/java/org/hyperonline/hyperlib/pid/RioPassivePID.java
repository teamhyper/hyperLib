package org.hyperonline.hyperlib.pid;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * @deprecated use {@link RioPID} without an output instead
 */
@Deprecated
public class RioPassivePID extends RioPID {
    public RioPassivePID(String name, DoubleSupplier source, double Kp, double Ki, double Kd, double tolerance) {
        super(name, source, Kp, Ki, Kd, tolerance);
    }

    public RioPassivePID(String name, DoubleSupplier source, DoubleConsumer output, double Kp, double Ki, double Kd, double tolerance) {
        super(name, source, output, Kp, Ki, Kd, tolerance);
    }
}
