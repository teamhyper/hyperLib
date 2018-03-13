package org.usfirst.frc.team69.util.oi.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.usfirst.frc.team69.util.PeriodicScheduler;

import edu.wpi.first.wpilibj.UnitTestUtility;

/**
 * {@link PeriodicSchedulerTest}
 * @author James
 *
 */
public class PeroidicSchedulerTest {
    
    private PeriodicScheduler m_sched;
    
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
     */
    @Before
    public void setUp() {
        m_sched = PeriodicScheduler.getInstance();
    }
    /**
     * 
     */
    @After
    public void tearDown() {
        m_sched.clear();
    }
    /**
     * 
     */
    @Test
    public void testClearOnEmpty() {
        // just check that nothing crashes
        m_sched.clear();
        m_sched.run();
    }
    /**
     * Test that element is cleared from the scheduler
     */
    @Test
    public void testClearWithElement() {
        MockRunnable event = new MockRunnable();
        m_sched.addEvent(event);
        m_sched.clear();
        m_sched.run();
        assertEquals(0, event.getCount());
    }
    /**
     * Test that an empty schedule runs
     */
    @Test
    public void testRunOnEmpty() {
        // just check nothing crashes
        m_sched.run();
    }
    /**
     * Test that an event runs
     */
    @Test
    public void testRunEvent() {
        MockRunnable event = new MockRunnable();
        m_sched.addEvent(event);
        m_sched.run();
        assertEquals(1, event.getCount());
    }
    
    /**
     * Test adding from an event
     */
    @Test
    public void testAddFromEvent() {
        MockRunnable event = new MockRunnable();
        
        Runnable adder = new Runnable() {
            private boolean hasRun = false;
            @Override public void run() {
                if (!hasRun) {
                    m_sched.addEvent(event);
                    hasRun = true;
                }
            }
        };
        
        m_sched.addEvent(adder);
        m_sched.run();
        assertEquals(1, event.getCount());
        m_sched.run();
        assertEquals(2, event.getCount());
    }

}
