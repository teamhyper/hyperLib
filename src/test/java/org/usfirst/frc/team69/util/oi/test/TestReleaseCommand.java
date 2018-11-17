package org.usfirst.frc.team69.util.oi.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.usfirst.frc.team69.util.QuickCommand;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * {@link TestReleaseCommand}
 * @author James
 *
 */
public class TestReleaseCommand {

    private Command runningCommand;
    private Command defaultCommand;
    private Command releaseCommand;
    private Subsystem subsystem;
    private Scheduler scheduler;

    @BeforeEach
    public void setUp() throws Exception {
        subsystem = new MySubsystem();
        defaultCommand = QuickCommand.continuous(subsystem, () -> {});
        runningCommand = QuickCommand.continuous(subsystem, () -> {});
        releaseCommand = QuickCommand.release(subsystem);
        
        scheduler = Scheduler.getInstance();
        scheduler.removeAll();
    }

    private class MySubsystem extends Subsystem {
        @Override
        protected void initDefaultCommand() {
            setDefaultCommand(defaultCommand);
        }
    }

    /**
     * Test the the Command requires the Subsystem
     */
    @Test
    public void testRequiresSubsystem() {
        assertTrue(releaseCommand.doesRequire(subsystem));
    }
    
    /**
     * Tests that 
     */
    @Test
    public void testKillsRunningAndStartsDefault() {
        runningCommand.start();
        scheduler.run();
        assertTrue(runningCommand.isRunning());
        
        releaseCommand.start();
        scheduler.run();
        assertTrue(releaseCommand.isRunning());
        assertFalse(runningCommand.isRunning());
        assertFalse(defaultCommand.isRunning());
        
        scheduler.run();
        assertTrue(defaultCommand.isRunning());
        assertFalse(releaseCommand.isRunning());
        assertFalse(runningCommand.isRunning());
    }

}
