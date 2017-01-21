package org.usfirst.frc.team69.util.oi;

import java.lang.reflect.Field;

public class ButtonData {
    private int m_port;
    private Action m_action;
    private Field m_field;
    
    public enum Action {
        WHEN_PRESSED,
        WHEN_RELEASED,
        WHILE_HELD
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
}
