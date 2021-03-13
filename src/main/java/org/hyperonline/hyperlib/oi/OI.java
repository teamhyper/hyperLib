package org.hyperonline.hyperlib.oi;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.hyperonline.hyperlib.oi.ButtonData.Action;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The {@link OI} class represents the operator interface. This class reads the
 * OI layout from another class, called the "oi map class", and sets up the
 * necessary joysticks, buttons, and commands.
 * 
 * This class can be used in two ways: on the robot, it actually creates
 * commands and binds them to buttons, and stores joysticks to check their
 * values. Off the robot, it can still parse the map class, validate it, and
 * draw diagrams of the controls.
 * 
 * @author James Hagborg
 *
 */
public class OI {
    private Class<?> m_mapClass;

    private boolean m_onRobot;
    private boolean m_commandsInitialized = false;
    private BadOIMapException m_lastException;

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
     * Construct a new OI instance. Note that after being constructed, the
     * commands are not yet initialized. You must call {@link #initCommands()}
     * 
     * @param oiMap
     *            A class to use as the map of the OI. Look at the examples for
     *            the format of this class.
     * @param onRobot
     *            Whether we are running this on the robot, as opposed to on a
     *            PC. If we are on a PC then we won't actually construct WPILib
     *            joysticks and commands, only the map of the controls.
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
     * Get the left operator joystick. This is the stick specified by the
     * {@link MapJoystick.Role#LEFT_OPERATOR} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick leftOperator() {
        return getJoystick(m_leftOperator);
    }

    /**
     * Get the right operator joystick. This is the stick specified by the
     * {@link MapJoystick.Role#RIGHT_OPERATOR} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick rightOperator() {
        return getJoystick(m_rightOperator);
    }

    /**
     * Get the left driver joystick. This is the stick specified by the
     * {@link MapJoystick.Role#LEFT_DRIVER} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick leftDriver() {
        return getJoystick(m_leftDriver);
    }

    /**
     * Get the right driver joystick. This is the stick specified by the
     * {@link MapJoystick.Role#RIGHT_DRIVER} role.
     * 
     * @return The left operator {@link Joystick}
     */
    public Joystick rightDriver() {
        return getJoystick(m_rightDriver);
    }

    /**
     * Get the joystick on the given port. This is useful if you have a joystick
     * which does not fall into the driver/operator category.
     * 
     * @param port
     *            The port the joystick was created on
     * @return The {@link Joystick} object
     */
    public Joystick getJoystick(int port) {
        JSWithData jsData = m_joysticks.get(port);
        return getJoystick(jsData == null ? null : jsData.js);
    }

    /**
     * Get a list of {@link JoystickData} objects describing the layout of the
     * OI. This list is a copy, so you are free to modify it. This method is
     * used by the OI mapper, but you may use it if you want your application to
     * be aware of the OI map.
     * 
     * @return A list of {@link JoystickData} objects holding data parsed from
     *         the OI map
     */
    public ArrayList<JoystickData> getJoystickData() {
        ArrayList<JoystickData> result = new ArrayList<JoystickData>();
        for (JSWithData jsData : m_joysticks.values()) {
            result.add(jsData.data);
        }
        return result;
    }

    /**
     * Validate the OI based on a set of rules set in {@link Validator}. In
     * addition, if errors were encountered in parsing, they will throw an
     * exception here.
     * 
     * @throws BadOIMapException
     *             if errors were encountered in parsing, or the
     *             {@link Validator} throws an exception.
     * 
     * @see Validator
     */
    public void validate() throws BadOIMapException {
        if (m_lastException != null) {
            throw m_lastException;
        }

        Validator.validate(getJoystickData());
    }

    /**
     * Draw diagrams of the OI using
     * {@link JoystickMapper#drawMap(java.util.List)}
     * 
     * @throws IOException
     *             if there is an error reading or writing the diagrams
     * 
     * @see JoystickMapper#drawMap(java.util.List)
     */
    public void drawMaps() throws IOException {
        JoystickMapper.drawMap(getJoystickData());
    }

    private void checkJoysticksExist() {
        if (!m_onRobot) {
            throw new UnsupportedOperationException(
                    "Cannot get joysticks when not on the robot");
        }
    }

    private Joystick getJoystick(Joystick js) {
        checkJoysticksExist();
        if (js == null) {
            throw new IllegalStateException(
                    "The given joystick does not exist");
        }
        return js;
    }

    private void mapJoysticks() {
        for (Class<?> jsClass : m_mapClass.getClasses()) {
            MapJoystick jsAnnotation = jsClass.getAnnotation(MapJoystick.class);
            if (jsAnnotation == null) {
                String msg = "%s does not havae a @MapJoystick annotation. If "
                        + "this class is meant to describe a joystick, you "
                        + "must add one.";
                reportWarning(String.format(msg, jsClass.getSimpleName()));
                continue;
            }

            JSWithData jsData = new JSWithData();
            jsData.data = createDataFromMap(jsAnnotation, jsClass);
            if (m_onRobot) {
                jsData.js = createJoystickFromMap(jsAnnotation);
            }

            int port = jsAnnotation.port();
            if (m_joysticks.containsKey(port)) {
                m_lastException = new BadOIMapException(String.format(
                        "Both %s and %s are assigned to port %d.",
                        m_joysticks.get(port).data.name(), jsData.data.name()));
            }
            m_joysticks.put(jsAnnotation.port(), jsData);
        }

    }

    private JoystickData createDataFromMap(MapJoystick jsAnnotation,
            Class<?> jsClass) {
        ArrayList<ButtonData> buttons = new ArrayList<ButtonData>();
        for (Field f : jsClass.getFields()) {
            WhenPressed wp = f.getAnnotation(WhenPressed.class);
            WhenReleased wr = f.getAnnotation(WhenReleased.class);
            WhileHeld wh = f.getAnnotation(WhileHeld.class);

            if (wp != null) {
                buttons.add(new ButtonData(wp.value(), Action.WHEN_PRESSED, f));
            }
            if (wr != null) {
                buttons.add(
                        new ButtonData(wr.value(), Action.WHEN_RELEASED, f));
            }
            if (wh != null) {
                buttons.add(new ButtonData(wh.value(), Action.WHILE_HELD, f));
            }
        }

        JoystickData jsData = new JoystickData(jsAnnotation.port(),
                jsAnnotation.role(), jsAnnotation.type(), jsClass, buttons);
        return jsData;
    }

    private Joystick createJoystickFromMap(MapJoystick jsAnnotation) {
        Joystick js = new Joystick(jsAnnotation.port());

        switch (jsAnnotation.role()) {
        case LEFT_DRIVER:
            m_leftDriver = js;
            break;
        case RIGHT_DRIVER:
            m_rightDriver = js;
            break;
        case LEFT_OPERATOR:
            m_leftOperator = js;
            break;
        case RIGHT_OPERATOR:
            m_rightOperator = js;
            break;
        default:
        }

        return js;
    }

    /**
     * Initialize the commands. This creates instances of the classes
     * corresponding to each joystick, and binds the commands in each to
     * buttons. This should be run after all subsystems have been initialized.
     */
    public void initCommands() {
        if (m_commandsInitialized) {
            throw new IllegalStateException("Cannot initialize commands twice");
        }
        if (!m_onRobot) {
            throw new UnsupportedOperationException(
                    "Cannot initialize commands when not on the robot");
        }

        m_commandsInitialized = true;

        for (JSWithData jsWithData : m_joysticks.values()) {
            Class<?> jsClass = jsWithData.data.mapClass();
            Object jsClsInstance;
            try {
                jsClsInstance = jsClass.getConstructor().newInstance();
            } catch (SecurityException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException e) {
                reportError(String.format("OI: Could not instantiate %s: %s",
                        jsClass.getSimpleName(), e.getMessage()));
                continue;
            }

            for (ButtonData btnData : jsWithData.data.buttons()) {
                // If for some reason someone were to create and remove the OI multiple times, it
                // would clog up the LiveWindow, since we don't bother closing buttons.
                @SuppressWarnings("resource")
                JoystickButton button = new JoystickButton(jsWithData.js, btnData.port());
                Field cmdField = btnData.field();
                Command command;

                try {
                    command = (Command) cmdField.get(jsClsInstance);
                } catch (IllegalAccessException e) {
                    reportError(String.format(
                            "OI: Could not access field %s in class %s: %s",
                            cmdField.getName(), jsClass.getSimpleName(), e.getMessage()));
                    continue;
                }

                if (command == null) {
                    reportError(String.format("OI: Command %s in %s is null",
                            cmdField.getName(), jsClass.getSimpleName()));
                    continue;
                }

                switch (btnData.action()) {
                case WHEN_PRESSED:
                    button.whenPressed(command);
                    break;
                case WHEN_RELEASED:
                    button.whenReleased(command);
                    break;
                case WHILE_HELD:
                    button.whileHeld(command);
                    break;
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
