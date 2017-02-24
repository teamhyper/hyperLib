package org.usfirst.frc.team69.util;

import java.util.ArrayList;

/**
 * The {@link PeriodicScheduler} class allows one to easily add tasks to
 * run periodically in every mode except test mode.  For example,
 * one can add methods to report information to the driver station, or to
 * check if preferences have been changed.
 * 
 * This class should be thread-safe, provided it is OK to run all events
 * from the main thread.
 * 
 * @author James Hagborg
 *
 */
public class PeriodicScheduler {

    private static PeriodicScheduler theInstance;
    
    /**
     * Return the single instance of {@link PeriodicScheduler}.  It is safe to
     * call this method from any thread.
     * 
     * @return The sinlge instance of {@link PeriodicScheduler}
     */
    public static synchronized PeriodicScheduler getInstance() {
        if (theInstance == null) {
            theInstance = new PeriodicScheduler();
        }
        return theInstance;
    }
    
    private ArrayList<Runnable> events = new ArrayList<Runnable>();

    private PeriodicScheduler() {
    }
    
    /**
     * Add an event to the list of events.  This event will run once every
     * time {@link #run()} is called.  If using {@link HYPERRobot}, this
     * happens once every autonomousPeriodic, teleopPeriodic, and disabledPeriodic.
     * 
     * As of hyperLib 0.3.1, it is safe to call this from within an event,
     * and to call it from any thread.
     * 
     * @param event A {@link Runnable} object representing the task to run 
     */
    public synchronized void addEvent(Runnable event) {
        events.add(event);
    }
    
    /**
     * Run all of the events which have been added to the scheduler.  If you
     * are using {@link HYPERRobot}, you do not need to call this manually.
     */
    public synchronized void run() {
        /* If we use an iterator, then callind addEvent from an event itself
         * will cause a ConcurrentModificationException */
        for (int i = 0; i < events.size(); i++) {
            events.get(i).run();
        }
    }
}
