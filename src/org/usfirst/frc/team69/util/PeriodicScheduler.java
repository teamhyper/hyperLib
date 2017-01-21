package org.usfirst.frc.team69.util;

import java.util.ArrayList;

public class PeriodicScheduler {

    private static PeriodicScheduler theInstance;
    
    public static PeriodicScheduler getInstance() {
        if (theInstance == null) {
            theInstance = new PeriodicScheduler();
        }
        return theInstance;
    }
    
    private ArrayList<Runnable> events = new ArrayList<Runnable>();

    private PeriodicScheduler() {
    }
    
    public void addEvent(Runnable event) {
        events.add(event);
    }
    
    public void run() {
        for (Runnable event : events) {
            event.run();
        }
    }
}
