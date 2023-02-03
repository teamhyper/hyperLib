package org.hyperonline.hyperlib.oi;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.hyperonline.hyperlib.oi.ButtonData.Action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The {@link OI} class represents the operator interface. This class reads the OI layout from
 * another class, called the "oi map class", and sets up the necessary joysticks, buttons, and
 * commands.
 *
 * <p>This class can be used in two ways: on the robot, it actually creates commands and binds them
 * to buttons, and stores joysticks to check their values. Off the robot, it can still parse the map
 * class, validate it, and draw diagrams of the controls.
 *
 * @author James Hagborg
 */
public class OI<LD extends GenericHID, RD extends GenericHID, LO extends GenericHID, RO extends GenericHID> {
  private Class<?> m_mapClass;

  private boolean m_onRobot;
  private boolean m_commandsInitialized = false;
  private BadOIMapException m_lastException;
  private HashMap<Integer, ControllerWithData> m_joysticks = new HashMap<Integer, ControllerWithData>();
  private LO m_leftOperator;
  private RO m_rightOperator;
  private LD m_leftDriver;
  private RD m_rightDriver;
  /**
   * Construct a new OI instance. Note that after being constructed, the commands are not yet
   * initialized. You must call {@link #initCommands()}
   *
   * @param oiMap A class to use as the map of the OI. Look at the examples for the format of this
   *     class.
   * @param onRobot Whether we are running this on the robot, as opposed to on a PC. If we are on a
   *     PC then we won't actually construct WPILib joysticks and commands, only the map of the
   *     controls.
   */
  public OI(Class<?> oiMap, boolean onRobot) throws BadOIMapException {
    if (oiMap == null) {
      throw new NullPointerException();
    }

    m_onRobot = onRobot;
    m_mapClass = oiMap;
    mapControllers();
  }

  /**
   * Get the left operator joystick. This is the stick specified by the {@link
   * MapController.Role#LEFT_OPERATOR} role.
   *
   * @return The left operator {@link Joystick}
   */
  public LO leftOperator() {
    return getControllers(m_leftOperator);
  }

  /**
   * Get the right operator joystick. This is the stick specified by the {@link
   * MapController.Role#RIGHT_OPERATOR} role.
   *
   * @return The left operator {@link Joystick}
   */
  public RO rightOperator() {
    return getControllers(m_rightOperator);
  }

  /**
   * Get the left driver joystick. This is the stick specified by the {@link
   * MapController.Role#LEFT_DRIVER} role.
   *
   * @return The left operator {@link Joystick}
   */
  public LD leftDriver() {
    return getControllers(m_leftDriver);
  }

  /**
   * Get the right driver joystick. This is the stick specified by the {@link
   * MapController.Role#RIGHT_DRIVER} role.
   *
   * @return The left operator {@link Joystick}
   */
  public RD rightDriver() {
    return getControllers(m_rightDriver);
  }

  /**
   * Get the controller on the given port. This is useful if you have a controller which does not fall
   * into the driver/operator category (and as such there is no stored type information for it).
   *
   * @param port The port the joystick was created on
   * @return The {@link GenericHID} object
   */
  public GenericHID getControllers(int port) {
    ControllerWithData jsData = m_joysticks.get(port);
    return getControllers(jsData == null ? null : jsData.controller);
  }

  /**
   * Get a list of {@link ControllerData} objects describing the layout of the OI. This list is a
   * copy, so you are free to modify it. This method is used by the OI mapper, but you may use it if
   * you want your application to be aware of the OI map.
   *
   * @return A list of {@link ControllerData} objects holding data parsed from the OI map
   */
  public ArrayList<ControllerData> getControllerData() {
    ArrayList<ControllerData> result = new ArrayList<>();
    for (ControllerWithData jsData : m_joysticks.values()) {
      result.add(jsData.data);
    }
    return result;
  }

  /**
   * Validate the OI based on a set of rules set in {@link Validator}. In addition, if errors were
   * encountered in parsing, they will throw an exception here.
   *
   * @throws BadOIMapException if errors were encountered in parsing, or the {@link Validator}
   *     throws an exception.
   * @see Validator
   */
  public void validate() throws BadOIMapException {
    if (m_lastException != null) {
      throw m_lastException;
    }

    Validator.validate(getControllerData());
  }

  /**
   * Draw diagrams of the OI using {@link ControllerMapper#drawMap(java.util.List)}
   *
   * @throws IOException if there is an error reading or writing the diagrams
   * @see ControllerMapper#drawMap(java.util.List)
   */
  public void drawMaps() throws IOException {
    ControllerMapper.drawMap(getControllerData());
  }

  private void checkControllersExist() {
    if (!m_onRobot) {
      throw new UnsupportedOperationException("Cannot get controllers when not on the robot");
    }
  }

  private <T extends GenericHID> T getControllers(T js) {
    checkControllersExist();
    if (js == null) {
      throw new IllegalStateException("The given controller does not exist");
    }
    return js;
  }

  private void mapControllers() throws BadOIMapException {
    for (Class<?> jsClass : m_mapClass.getClasses()) {
      MapController jsAnnotation = jsClass.getAnnotation(MapController.class);
      if (jsAnnotation == null) {
        String msg =
            "%s does not havae a @MapController annotation. If "
                + "this class is meant to describe a joystick, you "
                + "must add one.";
        reportWarning(String.format(msg, jsClass.getSimpleName()));
        continue;
      }

      ControllerWithData jsData = new ControllerWithData();
      jsData.data = createDataFromMap(jsAnnotation, jsClass);
      if (m_onRobot) {
        jsData.controller = createJoystickFromMap(jsAnnotation);
      }

      int port = jsAnnotation.port();
      if (m_joysticks.containsKey(port)) {
        m_lastException =
            new BadOIMapException(
                String.format(
                    "Both %s and %s are assigned to port %d.",
                    m_joysticks.get(port).data.name(), jsData.data.name()));
      }
      m_joysticks.put(jsAnnotation.port(), jsData);
    }
  }

  private ControllerData createDataFromMap(MapController jsAnnotation, Class<?> jsClass) {
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

    ControllerData jsData =
        new ControllerData(
            jsAnnotation.port(), jsAnnotation.role(), jsAnnotation.type(), jsClass, buttons);
    return jsData;
  }

  private GenericHID createJoystickFromMap(MapController jsAnnotation) throws BadOIMapException {
    var cls = typeEnumtoClass(jsAnnotation.type());
    var controller = createController(cls, jsAnnotation.port());

    switch (jsAnnotation.role()) {
      case LEFT_DRIVER -> m_leftDriver = (LD) controller;
      case RIGHT_DRIVER ->  m_rightDriver = (RD) controller;
      case LEFT_OPERATOR -> m_leftOperator = (LO) controller;
      case RIGHT_OPERATOR -> m_rightOperator = (RO) controller;
    }

    return controller;
  }

  private <T extends GenericHID> T createController(Class<T> cls, int port) throws BadOIMapException {
    try {
      return cls.getConstructor(int.class).newInstance(port);
    } catch (NoSuchMethodException
            | InstantiationException
            | IllegalAccessException
            | InvocationTargetException e) {
      throw new BadOIMapException(e);
    }
  }

  private Class<? extends GenericHID> typeEnumtoClass(MapController.Type type) {
      return switch (type) {
          case LOGITECH_2_AXIS, LOGITECH_3_AXIS -> Joystick.class;
          case XBOX -> XboxController.class;
          case PS4 -> PS4Controller.class;
      };
  }

  /**
   * Initialize the commands. This creates instances of the classes corresponding to each joystick,
   * and binds the commands in each to buttons. This should be run after all subsystems have been
   * initialized.
   */
  public void initCommands() {
    if (m_commandsInitialized) {
      throw new IllegalStateException("Cannot initialize commands twice");
    }
    if (!m_onRobot) {
      throw new UnsupportedOperationException("Cannot initialize commands when not on the robot");
    }

    m_commandsInitialized = true;

    for (ControllerWithData jsWithData : m_joysticks.values()) {
      Class<?> jsClass = jsWithData.data.mapClass();
      Object jsClsInstance;
      try {
        jsClsInstance = jsClass.getConstructor().newInstance();
      } catch (SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException
          | NoSuchMethodException e) {
        reportError(
            String.format(
                "OI: Could not instantiate %s: %s", jsClass.getSimpleName(), e.getMessage()));
        continue;
      }

      for (ButtonData btnData : jsWithData.data.buttons()) {
        // If for some reason someone were to create and remove the OI multiple times, it
        // would clog up the LiveWindow, since we don't bother closing buttons.
        JoystickButton button = new JoystickButton(jsWithData.controller, btnData.port());
        Field cmdField = btnData.field();
        Command command;

        try {
          command = (Command) cmdField.get(jsClsInstance);
        } catch (IllegalAccessException e) {
          reportError(
              String.format(
                  "OI: Could not access field %s in class %s: %s",
                  cmdField.getName(), jsClass.getSimpleName(), e.getMessage()));
          continue;
        }

        if (command == null) {
          reportError(
              String.format(
                  "OI: Command %s in %s is null", cmdField.getName(), jsClass.getSimpleName()));
          continue;
        }

        switch (btnData.action()) {
          case WHEN_PRESSED:
            button.onTrue(command);
            break;
          case WHEN_RELEASED:
            button.onFalse(command);
            break;
          case WHILE_HELD:
            button.whileTrue(command);
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

  private static class ControllerWithData {
    public GenericHID controller;
    public ControllerData data;
  }
}
