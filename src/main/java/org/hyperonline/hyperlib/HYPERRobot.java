package org.hyperonline.hyperlib;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.hyperonline.hyperlib.controller.HYPER_CANSparkMax;

import java.util.ArrayList;

/**
 * The {@link org.hyperonline.hyperlib.HYPERRobot} extends {@link TimedRobot} to provide code which
 * is mostly the same in every robot. Commands and periodic events (through {@link
 * PeriodicScheduler} run in every mode except test mode, where the {@link LiveWindow} runs.
 *
 * @author James Hagborg
 */
public abstract class HYPERRobot extends TimedRobot {

    protected static final ArrayList<HYPER_CANSparkMax> m_sparkMaxes = new ArrayList<>();
    public static NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();

    /**
     * Initialize the robot. This initializes in the following order:
     *
     * <ul>
     *   <li>Operator Interface (buttons and joysticks, not commands)
     *   <li>Subsystems (which in turn initialize hardware like speed controllers
     *   <li>"Helper classes" which depend on subsystems but have commands which depend on them (like
     *       UserDrive and Vision in hypercode)
     *   <li>Commands, including autonomous commands and commands on the OI
     * </ul>
     */
    @Override
    public final void robotInit() {
        initOI();
        initSubsystems();
        burnSparkMaxFlash();
        initHelpers();
        initCommands();
    }

    @Override
    public final void robotPeriodic() {
        CommandScheduler.getInstance().run();
        PeriodicScheduler.getInstance().run();
    }

    /**
     * Initialize the operator interface. This should create joysticks and buttons, but not any
     * commands, as {@link #initCommands} will not be called until after this.
     *
     * <p>If you are using the hyperLib {@link org.hyperonline.hyperlib.oi.OI}, this is where you
     * would call the constructor for the OI.
     */
    protected abstract void initOI();

    /**
     * Initialize subsystems. This is where you should call the constructors for any subsystems. The
     * subsystems may reference the joysticks/buttons on the OI, as they will have already been
     * initialized at this point.
     */
    protected abstract void initSubsystems();

    /**
     * Initialize helper classes. For example, in hypercode, UserDrive is a class which manages the
     * drive modes in teleop. This class depends on the DriveTrain but has commands which depend on it
     * in the OI. For any classes like this, initialize them here.
     */
    protected abstract void initHelpers();

    /**
     * Initialize commands. This is where you should add commands to the OI and the autonomous command
     * chooser. If you are using the hyperLib {@link org.hyperonline.hyperlib.oi.OI}, this is where
     * you should call {@link org.hyperonline.hyperlib.oi.OI#initCommands}
     */
    protected abstract void initCommands();

    /**
     * {@inheritDoc}
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void autonomousPeriodic() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
        PeriodicScheduler.getInstance().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationInit() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationPeriodic() {
    }

    public static void addSparkMax(HYPER_CANSparkMax motor) {
        m_sparkMaxes.add(motor);
    }

    /**
     * burn config to flash for any SparkMax controllers registered as "on" the robot
     */
    private void burnSparkMaxFlash() {
        System.out.println("Start: Burn SparkMax Flash");
        if (!this.m_sparkMaxes.isEmpty()) {
            Timer.delay(1);
            this.m_sparkMaxes.forEach(HYPER_CANSparkMax::safeBurnFlash);
            Timer.delay(0.25);
        } else {
            System.out.println("Robot has no SparkMaxes");
        }
        System.out.println("End: Burn SparkMax Flash");
    }
}
