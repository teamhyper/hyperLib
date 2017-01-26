package org.usfirst.frc.team69.util;

import java.util.ArrayList;

/**
 * The {@link PeriodicScheduler} class allows one to easily add tasks to
 * run periodically in every mode except test mode.  For example,
 * one can add methods to report information to the driver station, or to
 * check if preferences have been changed.
 * 
 * @author James Hagborg
 *
 */
public class PeriodicScheduler {

    private static PeriodicScheduler theInstance;
    
    /**
     * Return the single instance of {@link PeriodicScheduler}
     * 
     * @return The sinlge instance of {@link PeriodicScheduler}
     */
    public static PeriodicScheduler getInstance() {
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
     * @param event A {@link Runnable} object representing the task to run 
     */
    public void addEvent(Runnable event) {
        events.add(event);
    }
    
    /**
     * Run all of the events which have been added to the scheduler.  If you
     * are using {@link HYPERRobot}, you do not need to call this manually.
     */
    public void run() {
        for (Runnable event : events) {
            event.run();
        }
    }
}
