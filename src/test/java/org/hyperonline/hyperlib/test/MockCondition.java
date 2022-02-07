package org.hyperonline.hyperlib.test;

import java.util.function.BooleanSupplier;
/**
 * {@link MockCondition}
 * @author James
 *
 */
public class MockCondition implements BooleanSupplier {

    private boolean m_cond;
    private int timesChecked = 0;
    
    /**
     * 
     * @param startState
     *          starting state for condition
     */
    public MockCondition(boolean startState) {
        m_cond = startState;
    }
    
    /**
     * 
     * @param state
     *          set the condition state
     */
    public void set(boolean state) {
        m_cond = state;
    }
    
    @Override
    public boolean getAsBoolean() {
        timesChecked++;
        return m_cond;
    }

    public int getTimesChecked() {
        return timesChecked;
    }
}
