package org.hyperonline.hyperlib;

import static org.junit.jupiter.api.Assertions.*;

import org.hyperonline.hyperlib.CommandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.MockCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * {@link ForCommandTest}
 * @author James
 *
 */
public class ForCommandTest {    
    private MockCommand mockCommand;
    private Command loopCommand;

    /**
     * 
     */
    @BeforeEach
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
