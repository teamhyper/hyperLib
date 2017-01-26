package org.usfirst.frc.team69.util.pid;

import org.usfirst.frc.team69.util.PeriodicScheduler;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;

public class PrefPIDController extends PIDController {
    
    private static final String SEP = " ";
    private static final String P_SUF = SEP + "P";
    private static final String I_SUF = SEP + "I";
    private static final String D_SUF = SEP + "D";
    private static final String F_SUF = SEP + "F";
    private static final String MIN_OUT_SUF = SEP + "MinOut";
    private static final String MAX_OUT_SUF = SEP + "MaxOut";
    private static final String TOLERANCE_SUF = SEP + "Tolerance";
    
    private double m_defP, m_defI, m_defD, m_defF;
    private double m_defTolerance = -1; // -1 indicates no tolerance
    private double m_defMinOutput = -1, m_defMaxOutput = 1;
    
    private final String m_prefString;

    public PrefPIDController(String prefString, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
        this(prefString, Kp, Ki, Kd, 0.0, source, output, kDefaultPeriod);
    }

    public PrefPIDController(String prefString, double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
        this(prefString, Kp, Ki, Kd, 0.0, source, output, period);
    }

    public PrefPIDController(String prefString, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
        this(prefString, Kp, Ki, Kd, Kf, source, output, kDefaultPeriod);
    }

    public PrefPIDController(String prefString, double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output,
            double period) {
        super(Kp, Ki, Kd, Kf, source, output, period);
        
        m_defP = Kp;
        m_defI = Ki;
        m_defD = Kd;
        m_defF = Kf;
        m_prefString = prefString;
        
        createKeysIfEmpty();
        updatePreferences();
        
        PeriodicScheduler.getInstance().addEvent(this::updatePreferences);
    }
    
    private void createKeysIfEmpty() {
        Preferences pref = Preferences.getInstance();
        if (!pref.containsKey(m_prefString + P_SUF)) pref.putDouble(m_prefString + P_SUF, m_defP);
        if (!pref.containsKey(m_prefString + I_SUF)) pref.putDouble(m_prefString + I_SUF, m_defI);
        if (!pref.containsKey(m_prefString + D_SUF)) pref.putDouble(m_prefString + D_SUF, m_defD);
        if (!pref.containsKey(m_prefString + F_SUF)) pref.putDouble(m_prefString + F_SUF, m_defF);
    }
    
    @Override
    public void setPID(double p, double i, double d, double f) {
        DriverStation.reportError("setPID will likely have no effect, since the PID reads " +
                "its parameters from the preferences file, which is already set in the constructor. " +
                "If you want to have different sets of parameters, consider using two PrefPIDController " +
                "objects.", true);
    }
    
    @Override
    public void setPID(double p, double i, double d) {
        DriverStation.reportError("setPID will likely have no effect, since the PID reads " +
                "its parameters from the preferences file, which is already set in the constructor. " +
                "If you want to have different sets of parameters, consider using two PrefPIDController " +
                "objects.", true);
    }
    
    @Override
    public void setPercentTolerance(double tolerance) {
        DriverStation.reportError("Only an absolute tolerance can be set using preferences. " +
                "Use setAbsoluteTolerance().", true);
    }
    
    @Override
    public void setAbsoluteTolerance(double tolerance) {
        m_defTolerance = tolerance;
        
        Preferences pref = Preferences.getInstance();
        if (!pref.containsKey(m_prefString + TOLERANCE_SUF)) {
            pref.putDouble(m_prefString + TOLERANCE_SUF, tolerance);
        }
        
        updatePreferences();
    }
    
    @Override
    public void setOutputRange(double min, double max) {
        m_defMinOutput = min;
        m_defMaxOutput = max;
        
        Preferences pref = Preferences.getInstance();
        if (!pref.containsKey(m_prefString + MIN_OUT_SUF)) {
            pref.putDouble(m_prefString + MIN_OUT_SUF, min);
        }
        if (!pref.containsKey(m_prefString + MAX_OUT_SUF)) {
            pref.putDouble(m_prefString + MAX_OUT_SUF, max);
        }
        
        updatePreferences();
    }
    
    private void updatePreferences() {
        Preferences pref = Preferences.getInstance();
        double p = pref.getDouble(m_prefString + P_SUF, m_defP);
        double i = pref.getDouble(m_prefString + I_SUF, m_defI);
        double d = pref.getDouble(m_prefString + D_SUF, m_defD);
        double f = pref.getDouble(m_prefString + F_SUF, m_defF);
        double minOut = pref.getDouble(m_prefString + MIN_OUT_SUF, m_defMinOutput);
        double maxOut = pref.getDouble(m_prefString + MAX_OUT_SUF, m_defMaxOutput);
        double tolerance = pref.getDouble(m_prefString + TOLERANCE_SUF, m_defTolerance);
        
        super.setPID(p, i, d, f);
        super.setOutputRange(minOut, maxOut);
        if (tolerance >= 0) {
            super.setAbsoluteTolerance(tolerance);
        }
    }
}
