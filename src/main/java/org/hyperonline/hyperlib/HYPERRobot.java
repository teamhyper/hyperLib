package org.hyperonline.hyperlib;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The {@link org.hyperonline.hyperlib.HYPERRobot} extends {@link TimedRobot} to provide code which
 * is mostly the same in every robot. Commands and periodic events (through {@link
 * PeriodicScheduler} run in every mode except test mode, where the {@link LiveWindow} runs.
 *
 * @author James Hagborg
 */
public abstract class HYPERRobot extends TimedRobot {
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

  /** {@inheritDoc} */
  @Override
  public void disabledPeriodic() {}

  /** {@inheritDoc} */
  @Override
  public void autonomousPeriodic() {}

  /** {@inheritDoc} */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    PeriodicScheduler.getInstance().clear();
  }

  /** {@inheritDoc} */
  @Override
  public void simulationInit() {}

  /** {@inheritDoc} */
  @Override
  public void simulationPeriodic() {}
}
