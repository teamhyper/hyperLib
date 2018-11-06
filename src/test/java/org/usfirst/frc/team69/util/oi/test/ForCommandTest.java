package org.usfirst.frc.team69.util.oi.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.usfirst.frc.team69.util.CommandBuilder;

import edu.wpi.first.wpilibj.MockCommand;
import edu.wpi.first.wpilibj.UnitTestUtility;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * {@link ForCommandTest}
 * @author James
 *
 */
public class ForCommandTest {

    // We're looping conditional on code under test, want to make sure that doesn't break
    @Rule
    public Timeout globalTimeout = new Timeout(100, TimeUnit.MILLISECONDS);
    
    private MockCommand mockCommand;
    private Command loopCommand;
    
    /**
     * @throws {Exception}
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        UnitTestUtility.setupMockBase();
    }

    /**
     * 
     */
    @Before
    public void setUp() {
        mockCommand = new MockCommand();
        mockCommand.setHasFinished(true);
        loopCommand = new CommandBuilder()
                .forLoop(10, mockCommand)
                .build();
        Scheduler.getInstance().removeAll();
    }
    
    private void runLoopOnce() {
        loopCommand.start();
        Scheduler.getInstance().run();
        
        while (loopCommand.isRunning()) {
            Scheduler.getInstance().run();
        }
    }
    
    /**
     * Test the ForLoop
     */
    @Test
    public void testForLoop() {
        runLoopOnce();
        
        assertEquals(10, mockCommand.getExecuteCount());
        assertEquals(10, mockCommand.getIsFinishedCount());
        assertEquals(10, mockCommand.getInitializeCount());
        assertEquals(10, mockCommand.getEndCount());
        assertEquals(0, mockCommand.getInterruptedCount());
    }

    /**
     * Test multiple runs of the Command
     */
    @Test
    public void testMultipleRuns() {
        runLoopOnce();
        runLoopOnce();
        
        assertEquals(20, mockCommand.getExecuteCount());
        assertEquals(20, mockCommand.getIsFinishedCount());
        assertEquals(20, mockCommand.getInitializeCount());
        assertEquals(20, mockCommand.getEndCount());
        assertEquals(0, mockCommand.getInterruptedCount());
    }
}
