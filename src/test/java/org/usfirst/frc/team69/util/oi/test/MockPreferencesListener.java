package org.usfirst.frc.team69.util.oi.test;

import org.usfirst.frc.team69.util.pref.PreferencesListener;

public class MockPreferencesListener implements PreferencesListener {

    private int m_count;
    
    public int getCount() {
        return m_count;
    }
    
    @Override
    public void onPreferencesUpdated() {
        m_count++;
    }

}
