package org.usfirst.frc.team69.util.oi;

import java.util.Collections;
import java.util.List;

import org.usfirst.frc.team69.util.oi.MapJoystick.Role;
import org.usfirst.frc.team69.util.oi.MapJoystick.Type;

/**
 * This class is an intermediate data structure used in {@link OI} and in
 * RobotInspector.  It holds information parsed out of the OI map for a
 * particular joystick.
 * 
 * @author James Hagborg
 *
 */
public class JoystickData {
    private int m_port;
    private Role m_role;
    private Type m_type;
    private Class<?> m_class;
    private List<ButtonData> m_buttons;
    
    public JoystickData(int port, Role role, Type type, Class<?> mapClass, List<ButtonData> buttons) {
        m_port = port;
        m_role = role;
        m_type = type;
        m_class = mapClass;
        m_buttons = Collections.unmodifiableList(buttons);
    }
    
    public int port() { return m_port; }
    public Role role() { return m_role; }
    public Type type() { return m_type; }
    public String name() { return m_class.getSimpleName(); }
    public Class<?> mapClass() { return m_class; }
    public List<ButtonData> buttons() { return m_buttons; }
}
