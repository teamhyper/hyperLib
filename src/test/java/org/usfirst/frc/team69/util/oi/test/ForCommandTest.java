package org.usfirst.frc.team69.util.oi.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.usfirst.frc.team69.util.CommandBuilder;

import edu.wpi.first.wpilibj.MockCommand;
import edu.wpi.first.wpilibj.UnitTestUtility;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class ForCommandTest {

    // We're looping conditional on code under test, want to make sure that doesn't break
    @Rule
    public Timeout globalTimeout = new Timeout(100, TimeUnit.MILLISECONDS);
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        UnitTestUtility.setupMockBase();
    }

    @Test
    public void testForLoop() {
        MockCommand mockCommand = new MockCommand();
        mockCommand.setHasFinished(true);
        Command loopCommand = new CommandBuilder()
                .forLoop(10, mockCommand)
                .build();
        loopCommand.start();
        Scheduler.getInstance().run();
        
        while (loopCommand.isRunning()) {
            Scheduler.getInstance().run();
        }
        
        assertEquals(10, mockCommand.getExecuteCount());
        assertEquals(10, mockCommand.getIsFinishedCount());
        assertEquals(10, mockCommand.getInitializeCount());
        assertEquals(10, mockCommand.getEndCount());
        assertEquals(0, mockCommand.getInterruptedCount());
    }

}
