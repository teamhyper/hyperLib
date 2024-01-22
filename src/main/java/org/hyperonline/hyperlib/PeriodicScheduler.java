package org.hyperonline.hyperlib;

import java.util.ArrayList;

/**
 * The {@link PeriodicScheduler} class allows one to easily add tasks to run periodically in every
 * mode except test mode. For example, one can add methods to report information to the driver
 * station, or to check if preferences have been changed.
 *
 * <p>This class should be thread-safe, provided it is OK to run all events from the main thread.
 *
 * @author James Hagborg
 */
public class PeriodicScheduler {

  private static PeriodicScheduler theInstance;
  private ArrayList<Runnable> events = new ArrayList<>();

  private PeriodicScheduler() {}

  /**
   * Return the single instance of {@link PeriodicScheduler}. It is safe to call this method from
   * any thread.
   *
   * @return The single instance of {@link PeriodicScheduler}
   */
  public static synchronized PeriodicScheduler getInstance() {
    // TODO: investigate if a refactor to static methods instead of getInstance() to match wpilib is worth it
    if (theInstance == null) {
      theInstance = new PeriodicScheduler();
    }
    return theInstance;
  }

  /**
   * Add an event to the list of events. This event will run once every time {@link #run()} is
   * called. If using {@link HYPERRobot}, this happens once every autonomousPeriodic,
   * teleopPeriodic, and disabledPeriodic.
   *
   * <p>As of hyperLib 0.3.1, it is safe to call this from within an event, and to call it from any
   * thread.
   *
   * @param event A {@link Runnable} object representing the task to run
   */
  public synchronized void addEvent(Runnable event) {
    events.add(event);
  }

  /** Remove all scheduled tasks. This is mainly useful for testing. */
  public synchronized void clear() {
    events.clear();
  }

  /**
   * Run all of the events which have been added to the scheduler. If you are using {@link
   * HYPERRobot}, you do not need to call this manually.
   */
  public synchronized void run() {
    /*
     * If we use an iterator, then calling addEvent from an event itself
     * will cause a ConcurrentModificationException
     */
    for (Runnable event : events) {
      event.run();
    }
  }
}
