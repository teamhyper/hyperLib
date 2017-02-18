package org.usfirst.frc.team69.util.oi;

import java.lang.reflect.Field;

/**
 * This class is an intermediate data structure used in {@link OI} and in
 * RobotInspector.  It holds information parsed out of the OI map for a
 * particular binding of a button to a command.
 * 
 * @author James Hagborg
 *
 */
public class ButtonData {
    private int m_port;
    private Action m_action;
    private Field m_field;
    
    /**
     * An Action describes what action on a button (pressed, released, held)
     * should trigger a command to run.
     * 
     * @author James Hagborg
     *
     */
    public enum Action {
        WHEN_PRESSED("P"),
        WHEN_RELEASED("R"),
        WHILE_HELD("H");
        
        private final String m_desc;
        
        private Action(String desc) {
            m_desc = desc;
        }
        
        /**
         * Get a short string description of the action
         * @return A string description of the action
         */
        public String desc() { return m_desc; }
    }
    
    /**
     * Construct a new ButtonData object from parameters
     * 
     * @param port The id number of the button, starting at 1
     * @param action The action which will trigger a command
     * @param field The field of the OIMap class which corresponds to the
     * command to run
     */
    public ButtonData(int port, Action action, Field field) {
        m_port = port;
        m_action = action;
        m_field = field;
    }
    
    /**
     * Get the id of the button
     * @return The id of the button
     */
    public int port() { return m_port; }
    
    /**
     * Get the action which triggers a command
     * @return the action which triggers a command
     */
    public Action action() { return m_action; }
    
    /**
     * Get the name of the button/command mapping.  This is the name of the
     * field in OIMap corresponding to this button.
     * @return The name of the button/command mapping.
     */
    public String name() { return m_field.getName(); }
    
    /**
     * Get the field in the OI map corresponding to this button
     * @return The filed in the OI map corresponding to this button
     */
    public Field field() { return m_field; }
    
    /**
     * Get a string description of the button/command mapping.  This includes
     * the action and the name.  This is used for making the joystick diagrams.
     * @return A string description of the button/command mapping.
     */
    public String description() {
        return String.format("%s: %s", m_action.desc(), name());
    }
}
