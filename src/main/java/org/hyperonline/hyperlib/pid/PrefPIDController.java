package org.hyperonline.hyperlib.pid;

import edu.wpi.first.math.controller.PIDController;
import org.hyperonline.hyperlib.PeriodicScheduler;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * This class wraps a {@link PIDController}, using the Preferences file to store
 * and load parameters. The methods which set parameters only set defaults, and
 * the Preferences file is always used if it is set.
 * 
 * This allows for easy tuning of constants with persistent storage, without
 * having to recompile the code.
 * 
 * @author James Hagborg
 *
 */
public class PrefPIDController extends PIDController {
    public static final double kDefaultPeriod = 0.05;

    private static final String SEP = " ";
    private static final String P_SUF = SEP + "P";
    private static final String I_SUF = SEP + "I";
    private static final String D_SUF = SEP + "D";
    private static final String MIN_OUT_SUF = SEP + "MinOut";
    private static final String MAX_OUT_SUF = SEP + "MaxOut";
    private static final String TOLERANCE_SUF = SEP + "Tolerance";

    private double m_defP, m_defI, m_defD;
    private double m_defTolerance = -1; // -1 indicates no tolerance
    private double m_defMinOutput = -1, m_defMaxOutput = 1;
    
    private DoubleSupplier m_source;

    private final String m_prefString;

    /**
     * @see PIDController#PIDController(double, double, double)
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
     * @param output
     *            the output of the PID
     */
    public PrefPIDController(String prefString, double Kp, double Ki, double Kd,
                             DoubleSupplier source, DoubleConsumer output) {
        this(prefString, Kp, Ki, Kd, 0.0, source, output, kDefaultPeriod);
    }

    /**
     * @see PIDController#PIDController(double, double, double, double)
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
     * @param output
     *            the output of the PID
     * @param period
     *            the period to run the PID controller at
     */
    public PrefPIDController(String prefString, double Kp, double Ki, double Kd,
            DoubleSupplier source, DoubleConsumer output, double period) {
        this(prefString, Kp, Ki, Kd, 0.0, source, output, period);
    }

    /**
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
     * @param output
     *            the output of the PID
     * @param period
     *            the period to run the PID controller at
     */
    public PrefPIDController(String prefString, double Kp, double Ki, double Kd,
            double Kf, DoubleSupplier source, DoubleConsumer output, double period) {
        super(Kp, Ki, Kd, period);

        m_defP = Kp;
        m_defI = Ki;
        m_defD = Kd;
        m_prefString = prefString;
        m_source = source;

        createKeysIfEmpty();
        updatePreferences();

        PeriodicScheduler.getInstance().addEvent(this::updatePreferences);
    }

    private void createKeysIfEmpty() {
        if (!Preferences.containsKey(m_prefString + P_SUF))
            Preferences.setDouble(m_prefString + P_SUF, m_defP);
        if (!Preferences.containsKey(m_prefString + I_SUF))
            Preferences.setDouble(m_prefString + I_SUF, m_defI);
        if (!Preferences.containsKey(m_prefString + D_SUF))
            Preferences.setDouble(m_prefString + D_SUF, m_defD);
    }

    @Deprecated
    public void setPID(double p, double i, double d, double f) {
        DriverStation.reportError(
                "setPID will likely have no effect, since the PID reads "
                        + "its parameters from the preferences file, which is already set in the constructor. "
                        + "If you want to have different sets of parameters, consider using two PrefPIDController "
                        + "objects.",
                true);
    }

    @Deprecated
    @Override
    public void setPID(double p, double i, double d) {
        String msg = "setPID will likeley have not effect, since the PID "
                + "reads its parameters from the preferences file, which is "
                + "already set in the constructor. If you want to have "
                + "different sets of parameters, consider using two "
                + "PrefPIDController object.";
        DriverStation.reportError(msg, true);
    }

    @Deprecated
    public void setPercentTolerance(double tolerance) {
        DriverStation.reportError(
                "Only an absolute tolerance can be set using preferences. "
                        + "Use setAbsoluteTolerance().",
                true);
    }

    /**
     * Set the default absolute tolerance. In most cases it will only matter
     * what you pass to this the first time you call it, since it will then add
     * an entry to the preferences file if one does not yet exist.
     * 
     * @param tolerance
     *            The new default value for absolute tolerance.
     */
    @Override
    public void setTolerance(double tolerance) {
        m_defTolerance = tolerance;

        if (!Preferences.containsKey(m_prefString + TOLERANCE_SUF)) {
            Preferences.setDouble(m_prefString + TOLERANCE_SUF, tolerance);
        }

        updatePreferences();
    }

    /**
     * Set the default output range. In most cases it will only matter what you
     * pass to this the first time you call it, since it will then add an entry
     * to the preferences file if one does not yet exist.
     * 
     * @param min
     *            The lower bound on the output. By default -1.
     * @param max
     *            The upper bound on the output. By default 1.
     */
    public void setOutputRange(double min, double max) {
        m_defMinOutput = min;
        m_defMaxOutput = max;

        if (!Preferences.containsKey(m_prefString + MIN_OUT_SUF)) {
            Preferences.setDouble(m_prefString + MIN_OUT_SUF, min);
        }
        if (!Preferences.containsKey(m_prefString + MAX_OUT_SUF)) {
            Preferences.setDouble(m_prefString + MAX_OUT_SUF, max);
        }

        updatePreferences();
    }
    /**
     * Gets the tolerance of the PID
     * 
     * @return the tolerance of this PID 
     */
    public double getTolerance() {
    	return m_defTolerance;
    }
    
    /**
     * Is the system above the given target
     * 
     * @param target
     * 			setpoint to check if the PID system is above
     * 
     * @param reverse
     * 			is the system reversed
     * 
     * @return is the system above the given target
     */
    public boolean isAbove(double target, boolean reverse) {
    	return (reverse ? m_source.getAsDouble() <= target+m_defTolerance : m_source.getAsDouble() >= target-m_defTolerance);
    }
    /**
     * Is the system below the given target
     * 
     * @param target
     * 			setpoint to check if the PID system is below
     * 
     * @param reverse
     * 			is the system reversed
     * 
     * @return is the system below the given target
     */
    public boolean isBelow(double target, boolean reverse) {
    	return (reverse ? m_source.getAsDouble() >= target-m_defTolerance : m_source.getAsDouble() <= target+m_defTolerance);
    }
    

    private void updatePreferences() {
        double p = Preferences.getDouble(m_prefString + P_SUF, m_defP);
        double i = Preferences.getDouble(m_prefString + I_SUF, m_defI);
        double d = Preferences.getDouble(m_prefString + D_SUF, m_defD);
        double minOut = Preferences.getDouble(m_prefString + MIN_OUT_SUF,
                m_defMinOutput);
        double maxOut = Preferences.getDouble(m_prefString + MAX_OUT_SUF,
                m_defMaxOutput);
        double tolerance = Preferences.getDouble(m_prefString + TOLERANCE_SUF,
                m_defTolerance);

        super.setPID(p, i, d);
        super.enableContinuousInput(minOut, maxOut);
        if (tolerance >= 0) {
            super.setTolerance(tolerance);
        }
    }
}
