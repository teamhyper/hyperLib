package org.usfirst.frc.team69.util.oi;

import java.lang.reflect.Field;

public class ButtonData {
    private int m_port;
    private Action m_action;
    private Field m_field;
    
    public enum Action {
        WHEN_PRESSED("RELEASED"),
        WHEN_RELEASED("PRESSED"),
        WHILE_HELD("HELD");
        
        private final String m_desc;
        
        private Action(String desc) {
            m_desc = desc;
        }
        
        public String desc() { return m_desc; }
    }
    
    public ButtonData(int port, Action action, Field field) {
        m_port = port;
        m_action = action;
        m_field = field;
    }
    
    public int port() { return m_port; }
    public Action action() { return m_action; }
    public String name() { return m_field.getName(); }
    public Field field() { return m_field; }
    public String description() {
        return String.format("%s: %s", m_action.desc(), name());
    }
}
