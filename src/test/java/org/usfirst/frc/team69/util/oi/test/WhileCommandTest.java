package org.usfirst.frc.team69.util.oi.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.first.wpilibj.MockCommand;
import edu.wpi.first.wpilibj.UnitTestUtility;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WhileCommand;

/**
 * 
 * @author James
 *
 */
public class WhileCommandTest {

    private MockCondition condition;
    private MockCommand body;
    private WhileCommand whileCommand;
    
    /**
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        UnitTestUtility.setupMockBase();
    }

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Scheduler.getInstance().removeAll();
        
        condition = new MockCondition(true);
        body = new MockCommand();
        whileCommand = new WhileCommand(condition, body);
    }
    
    /**
     * Test Startup
     */
    @Test
    public void testStartup() {
        // Nothing is running at first
        whileCommand.start();
        assertFalse(whileCommand.isRunning());
        assertFalse(body.isRunning());
        
        // On the first tick, the while command starts up
        Scheduler.getInstance().run();
        assertTrue(whileCommand.isRunning());
        assertFalse(body.isRunning());
        
        // On the next tick, the body starts up
        Scheduler.getInstance().run();
        assertTrue(whileCommand.isRunning());
        assertTrue(body.isRunning());
    }

    /**
     * Test that the Command runs
     */
    @Test
    public void testWhileCommandRuns() {
        // Run the body 10 times, with each iteration taking 5 ticks
        whileCommand.start();
        Scheduler.getInstance().run();          // queue the command
        Scheduler.getInstance().run();          // actually run it
        
        for (int iter = 0; iter < 10; iter++) {
            // run 4 ticks normally
            for (int tick = 0; tick < 4; tick++) {
                Scheduler.getInstance().run();
            }
            
            // on the 5th tick it finishes, this takes 2 ticks to happen
            body.setHasFinished(true);
            Scheduler.getInstance().run();      // re-queue the body
            body.setHasFinished(false);
            Scheduler.getInstance().run();      // initialize the body
        }
        
        assertEquals(10, body.getInitializeCount());
        assertEquals(50, body.getExecuteCount());
        assertEquals(50, body.getIsFinishedCount());
        assertEquals(10, body.getEndCount());
        assertEquals(0, body.getInterruptedCount());
        // The loop ran 10 times, and started an 11th
        assertEquals(11, condition.getTimesChecked());
    }
    
    /**
     * Test the TransferRequirements
     */
    @Test
    public void testTransferRequirements() {
        Subsystem req = new Subsystem() {
            @Override protected void initDefaultCommand() {}
        };
        
        Command bodyWithReqs = new InstantCommand() {
            { requires(req); }
        };
        
        WhileCommand whileCommandWithReqs = new WhileCommand(condition, bodyWithReqs);
        assertTrue(whileCommandWithReqs.doesRequire(req));
        assertFalse(bodyWithReqs.doesRequire(req));
    }
    
    /**
     * Test that the Command does not run if condition is false
     */
    @Test
    public void testDoesNotRunIfFalse() {
        whileCommand.start();
        condition.set(false);
        
        Scheduler.getInstance().run(); // queue the whileCommand
        Scheduler.getInstance().run(); // should check and stop
        
        assertFalse(whileCommand.isRunning());
        assertFalse(body.isRunning());
        assertEquals(0, body.getInitializeCount());
        assertEquals(0, body.getExecuteCount());
        assertEquals(0, body.getIsFinishedCount());
        assertEquals(0, body.getEndCount());
        assertEquals(0, body.getInterruptedCount());
        assertEquals(1, condition.getTimesChecked());
    }
    
    /**
     * Test that the Command ends when the condition is false
     */
    @Test
    public void testEndsWhenFalse() {
        whileCommand.start();
        body.setHasFinished(true);
        
        Scheduler.getInstance().run(); // queue the whileCommand
        Scheduler.getInstance().run(); // queue the body
        Scheduler.getInstance().run(); // run the body once
        
        condition.set(false);
        Scheduler.getInstance().run(); // about to re-queue, but condition is false
        
        assertFalse(whileCommand.isRunning());
        assertFalse(body.isRunning());
        assertEquals(1, body.getInitializeCount());
        assertEquals(1, body.getExecuteCount());
        assertEquals(1, body.getIsFinishedCount());
        assertEquals(1, body.getEndCount());
        assertEquals(0, body.getInterruptedCount());
        // checked once at start, another at end
        assertEquals(2, condition.getTimesChecked());
    }
    
    /**
     * Test the CancelWhile Command
     */
    @Test
    public void testCancelWhileCommand() {
        whileCommand.start();
        Scheduler.getInstance().run(); // queue the whileCommand
        Scheduler.getInstance().run(); // queue the body
        Scheduler.getInstance().run(); // queue run the body once
        
        whileCommand.cancel();
        Scheduler.getInstance().run(); // make the cancellation take effect
        
        assertFalse(whileCommand.isRunning());
        assertFalse(body.isRunning());
        assertEquals(1, body.getInitializeCount());
        assertEquals(1, body.getExecuteCount());
        assertEquals(1, body.getIsFinishedCount());
        assertEquals(0, body.getEndCount());
        assertEquals(1, body.getInterruptedCount());
        assertEquals(1, condition.getTimesChecked());
    }
    
    /**
     * Test the CancelBody Command
     */
    @Test
    public void testCancelBodyCommand() {
        whileCommand.start();
        Scheduler.getInstance().run(); // queue the whileCommand
        Scheduler.getInstance().run(); // queue the body
        Scheduler.getInstance().run(); // queue run the body once
        
        body.cancel();
        Scheduler.getInstance().run(); // make the cancellation take effect
        Scheduler.getInstance().run(); // Let the command re-queue
        Scheduler.getInstance().run(); // Let the command start up again
        
        assertTrue(whileCommand.isRunning());
        assertTrue(body.isRunning());
        // don't bother checking execute count, that's dependant on
        // implementation details of the scheduler (what order it runs the
        // two commands in)
        assertEquals(2, body.getInitializeCount());
        assertEquals(0, body.getEndCount());
        assertEquals(1, body.getInterruptedCount());
        assertEquals(2, condition.getTimesChecked());
    }

}
