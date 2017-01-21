package org.usfirst.frc.team69.util;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public abstract class HYPERRobot extends IterativeRobot {
    @Override
    public void robotInit() {
        // Set the WPILib command scheduler to run automatically.
        PeriodicScheduler.getInstance().addEvent(Scheduler.getInstance()::run);
        
        initSubsystems();
        initCommands();
    }
    
    protected abstract void initOI();
    protected abstract void initSubsystems();
    protected abstract void initHelpers();
    protected abstract void initCommands();
    
    @Override
    public void disabledPeriodic() {
        PeriodicScheduler.getInstance().run();
    }
    
    @Override
    public void autonomousPeriodic() {
        PeriodicScheduler.getInstance().run();
    }
    
    @Override
    public void teleopPeriodic() {
        PeriodicScheduler.getInstance().run();
    }
    
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
}
