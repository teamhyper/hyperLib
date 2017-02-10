package org.usfirst.frc.team69.util.oi.test;

import java.util.function.BooleanSupplier;

public class MockCondition implements BooleanSupplier {

    private boolean m_cond;
    
    public MockCondition(boolean startState) {
        m_cond = startState;
    }
    
    public void set(boolean state) {
        m_cond = state;
    }
    
    @Override
    public boolean getAsBoolean() {
        return m_cond;
    }

}
