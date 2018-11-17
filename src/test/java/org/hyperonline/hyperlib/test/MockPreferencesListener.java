package org.hyperonline.hyperlib.test;

import org.hyperonline.hyperlib.pref.PreferencesListener;

/**
 * {@link MockPreferencesListener}
 * @author James
 *
 */
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
