package org.usfirst.frc.team69.util.oi.test;

/**
 * {@link MockRunnable}
 * @author James
 *
 */
public class MockRunnable implements Runnable {

    private int m_count = 0;
    
    public int getCount() {
        return m_count;
    }
    
    @Override
    public void run() {
        m_count++;
    }

}
