package org.usfirst.frc.team69.util.oi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team69.util.oi.ButtonData.Action;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class OI {
    private Class<?> m_mapClass;
    
    private boolean m_onRobot;
    private boolean m_commandsInitialized = false;
    
    private static class JSWithData {
        public Joystick js;
        public JoystickData data;
    }
    
    private HashMap<Integer, JSWithData> m_joysticks = new HashMap<Integer, JSWithData>();
    
    private Joystick m_leftOperator;
    private Joystick m_rightOperator;
    private Joystick m_leftDriver;
    private Joystick m_rightDriver;
    
    /**
     * Construct a new OI instance
     * 
     * @param oiMap A class to use as the map of the OI.  Look at the examples for the
     * format of this class.
     * @param onRobot Whether we are running this on the robot, as opposed to on a PC.
     * If we are on a PC then we won't actually construct WPILib joysticks and commands,
     * only the map of the controls. 
     */
    public OI(Class<?> oiMap, boolean onRobot) {
        if (oiMap == null) {
            throw new NullPointerException();
        }
        
        m_onRobot = onRobot;
        m_mapClass = oiMap;
        mapJoysticks();
    }
    
    /**
     * Get the left operator joystick.  This is the stick specified by the
     * {@link MapJoystick.Role#LEFT_OPERATOR} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick leftOperator() { return getJoystick(m_leftOperator); }
    
    /**
     * Get the right operator joystick.  This is the stick specified by the
     * {@link MapJoystick.Role#RIGHT_OPERATOR} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick rightOperator() { return getJoystick(m_rightOperator); }
    
    /**
     * Get the left driver joystick.  This is the stick specified by the
     * {@link MapJoystick.Role#LEFT_DRIVER} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick leftDriver() { return getJoystick(m_leftDriver); }
    
    /**
     * Get the right driver joystick.  This is the stick specified by the
     * {@link MapJoystick.Role#RIGHT_DRIVER} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick rightDriver() { return getJoystick(m_rightDriver); }
    
    /**
     * Get the joystick on the given port.  This is useful if you have a
     * joystick which does not fall into the driver/operator category.
     * 
     * @param port The port the joystick was created on
     * @return The {@link Joystick} object
     */
    public Joystick getJoystick(int port) {
        JSWithData jsData = m_joysticks.get(port);
        return getJoystick(jsData == null ? null : jsData.js);
    }
    
    public ArrayList<JoystickData> getJoystickData() {
        ArrayList<JoystickData> result = new ArrayList<JoystickData>();
        for (JSWithData jsData : m_joysticks.values()) {
            result.add(jsData.data);
        }
        return result;
    }
    
    private void checkJoysticksExist() {
        if (!m_onRobot) {
            throw new UnsupportedOperationException("Cannot get joysticks when not on the robot");
        }
    }
    
    private Joystick getJoystick(Joystick js) {
        checkJoysticksExist();
        if (js == null) {
            throw new IllegalStateException("The given joystick does not exist");
        }
        return js;
    }
    
    private void mapJoysticks() {
        for (Class<?> jsClass : m_mapClass.getClasses()) {
            MapJoystick jsAnnotation = jsClass.getAnnotation(MapJoystick.class);
            if (jsAnnotation == null) {
                reportWarning(String.format("%s does not have a @MapJoystick annotation. " +
                        "If this class is meant to describe a joystick, you must add one.",
                        jsClass.getSimpleName()));
                continue;
            }
            
            JSWithData jsData = new JSWithData();
            jsData.data = createDataFromMap(jsAnnotation, jsClass);
            if (m_onRobot) {
                jsData.js = createJoystickFromMap(jsAnnotation);
            }
            m_joysticks.put(jsAnnotation.port(), jsData);
        }
        
    }
    
    private JoystickData createDataFromMap(MapJoystick jsAnnotation, Class<?> jsClass) {
        ArrayList<ButtonData> buttons = new ArrayList<ButtonData>();
        for (Field f : jsClass.getFields()) {
            WhenPressed wp = f.getAnnotation(WhenPressed.class);
            WhenReleased wr = f.getAnnotation(WhenReleased.class);
            WhileHeld wh = f.getAnnotation(WhileHeld.class);
            
            if (wp != null) {
                buttons.add(new ButtonData(wp.value(), Action.WHEN_PRESSED, f));
            }
            if (wr != null) {
                buttons.add(new ButtonData(wr.value(), Action.WHEN_RELEASED, f));
            }
            if (wh != null) {
                buttons.add(new ButtonData(wh.value(), Action.WHILE_HELD, f));
            }
        }
        
        JoystickData jsData = new JoystickData(
                jsAnnotation.port(),
                jsAnnotation.role(),
                jsAnnotation.type(),
                jsClass,
                buttons);
        return jsData;
    }
    
    private Joystick createJoystickFromMap(MapJoystick jsAnnotation) {
        Joystick js = new Joystick(jsAnnotation.port());

        switch (jsAnnotation.role()) {
        case LEFT_DRIVER: m_leftDriver = js; break;
        case RIGHT_DRIVER: m_rightDriver = js; break;
        case LEFT_OPERATOR: m_leftOperator = js; break;
        case RIGHT_OPERATOR: m_rightOperator = js; break;
        default:
        }
        
        return js;
    }
    
    /**
     * Initialize the commands.  This creates instances of the classes corresponding
     * to each joystick, and binds the commands in each to buttons.  This should be
     * run after all subsystems have been initialized.
     */
    public void initCommands() {
        if (m_commandsInitialized) {
            throw new IllegalStateException("Cannot initialize commands twice");
        }
        if (!m_onRobot) {
            throw new UnsupportedOperationException("Cannot initialize commands when not on the robot");
        }
        
        m_commandsInitialized = true;
        
        for (JSWithData jsWithData : m_joysticks.values()) {
            Class<?> jsClass = jsWithData.data.mapClass();
            Object jsClsInstance;
            try {
                jsClsInstance = jsClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                reportError(String.format("OI: Could not instantiate %s: %s",
                        jsClass.getSimpleName(), e.getMessage()));
                continue;
            }
            
            for (ButtonData btnData : jsWithData.data.buttons()) {
                JoystickButton button = new JoystickButton(jsWithData.js, btnData.port());
                Field cmdField = btnData.field();
                Command command;
                
                try {
                    command = (Command) cmdField.get(jsClsInstance);
                } catch (IllegalAccessException e) {
                    reportError(String.format("OI: Could not access field %s in class %s: %s", 
                            cmdField.getName(), jsClass.getSimpleName(), e.getMessage()));
                    continue;
                }
                
                if (command == null) {
                    reportError(String.format("OI: Command %s in %s is null",
                            cmdField.getName(), jsClass.getSimpleName()));
                    continue;
                }
                
                switch (btnData.action()) {
                case WHEN_PRESSED: button.whenPressed(command); break;
                case WHEN_RELEASED: button.whenReleased(command); break;
                case WHILE_HELD: button.whileHeld(command); break;
                }
            }
        }
    }
    
    private void reportError(String msg) {
        if (m_onRobot) {
            DriverStation.reportError(msg, false);
        } else {
            System.err.println("Error: " + msg);
        }
    }
    
    private void reportWarning(String msg) {
        if (m_onRobot) {
            DriverStation.reportWarning(msg, false);
        } else {
            System.err.println("Warning: " + msg);
        }
    }
}
