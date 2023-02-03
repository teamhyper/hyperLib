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
 * another class, called the "oi map class", and sets up the necessary controllers, buttons, and
 * commands.
 *
 * <p>This class can be used in two ways: on the robot, it actually creates commands and binds them
 * to buttons, and stores controllers to check their values. Off the robot, it can still parse the map
 * class, validate it, and draw diagrams of the controls.
 *
 * @author James Hagborg
 */
public class OI<LD extends GenericHID, RD extends GenericHID, LO extends GenericHID, RO extends GenericHID> {
  private Class<?> m_mapClass;

  private boolean m_onRobot;
  private boolean m_commandsInitialized = false;
  private BadOIMapException m_lastException;
  private HashMap<Integer, ControllerWithData> m_controllers = new HashMap<>();
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
   *     PC then we won't actually construct WPILib controllers and commands, only the map of the
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
   * Get the left operator controller. This is the stick specified by the {@link
   * MapController.Role#LEFT_OPERATOR} role.
   *
   * @return The left operator
   */
  public LO leftOperator() {
    return getControllers(m_leftOperator);
  }

  /**
   * Get the right operator controller. This is the stick specified by the {@link
   * MapController.Role#RIGHT_OPERATOR} role.
   *
   * @return The left operator
   */
  public RO rightOperator() {
    return getControllers(m_rightOperator);
  }

  /**
   * Get the left driver controller. This is the stick specified by the {@link
   * MapController.Role#LEFT_DRIVER} role.
   *
   * @return The left operator
   */
  public LD leftDriver() {
    return getControllers(m_leftDriver);
  }

  /**
   * Get the right driver controller. This is the stick specified by the {@link
   * MapController.Role#RIGHT_DRIVER} role.
   *
   * @return The left operator
   */
  public RD rightDriver() {
    return getControllers(m_rightDriver);
  }

  /**
   * Get the controller on the given port. This is useful if you have a controller which does not fall
   * into the driver/operator category (and as such there is no stored type information for it).
   *
   * @param port The port the controller was created on
   * @return The {@link GenericHID} object
   */
  public GenericHID getControllers(int port) {
    ControllerWithData controllerData = m_controllers.get(port);
    return getControllers(controllerData == null ? null : controllerData.controller);
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
    for (ControllerWithData data : m_controllers.values()) {
      result.add(data.data);
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
    for (Class<?> controllerClass : m_mapClass.getClasses()) {
      MapController annotation = controllerClass.getAnnotation(MapController.class);
      if (annotation == null) {
        String msg =
            "%s does not have a @MapController annotation. If "
                + "this class is meant to describe a controller, you "
                + "must add one.";
        reportWarning(String.format(msg, controllerClass.getSimpleName()));
        continue;
      }

      ControllerWithData data = new ControllerWithData();
      data.data = createDataFromMap(annotation, controllerClass);
      if (m_onRobot) {
        data.controller = createControllerFromMap(annotation);
      }

      int port = annotation.port();
      if (m_controllers.containsKey(port)) {
        m_lastException =
            new BadOIMapException(
                String.format(
                    "Both %s and %s are assigned to port %d.",
                    m_controllers.get(port).data.name(), data.data.name(), annotation.port()));
      }
      m_controllers.put(annotation.port(), data);
    }
  }

  private ControllerData createDataFromMap(MapController annotation, Class<?> cls) {
    ArrayList<ButtonData> buttons = new ArrayList<ButtonData>();
    for (Field f : cls.getFields()) {
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

    return new ControllerData(
        annotation.port(), annotation.role(), annotation.type(), cls, buttons);
  }

  private GenericHID createControllerFromMap(MapController jsAnnotation) throws BadOIMapException {
    var cls = switch (jsAnnotation.type()) {
      case LOGITECH_2_AXIS, LOGITECH_3_AXIS -> Joystick.class;
      case XBOX -> XboxController.class;
      case PS4 -> PS4Controller.class;
      case OTHER -> GenericHID.class;
    };
    var controller = instantiateHID(cls, jsAnnotation.port());

    // I'm not aware of any way to do it without casting, hence the controller type being in two different places
    switch (jsAnnotation.role()) {
      case LEFT_DRIVER -> m_leftDriver = (LD) controller;
      case RIGHT_DRIVER ->  m_rightDriver = (RD) controller;
      case LEFT_OPERATOR -> m_leftOperator = (LO) controller;
      case RIGHT_OPERATOR -> m_rightOperator = (RO) controller;
    }

    return controller;
  }

  private <T extends GenericHID> T instantiateHID(Class<T> cls, int port) throws BadOIMapException {
    try {
      // All WPILib GenericHID subclasses take an (int) constructor
      return cls.getConstructor(int.class).newInstance(port);
    } catch (NoSuchMethodException
            | InstantiationException
            | IllegalAccessException
            | InvocationTargetException e) {
      var err = new BadOIMapException(e);
      reportError(err.getMessage());
      throw err;
    }
  }

  /**
   * Initialize the commands. This creates instances of the classes corresponding to each controller,
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

    for (ControllerWithData controllerWithData : m_controllers.values()) {
      Class<?> cls = controllerWithData.data.mapClass();
      Object instance;
      try {
        instance = cls.getConstructor().newInstance();
      } catch (SecurityException
          | InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException
          | NoSuchMethodException e) {
        reportError(
            String.format(
                "OI: Could not instantiate %s: %s", cls.getSimpleName(), e.getMessage()));
        continue;
      }

      for (ButtonData btnData : controllerWithData.data.buttons()) {
        // If for some reason someone were to create and remove the OI multiple times, it
        // would clog up the LiveWindow, since we don't bother closing buttons.
        JoystickButton button = new JoystickButton(controllerWithData.controller, btnData.port());
        Field cmdField = btnData.field();
        Command command;

        try {
          command = (Command) cmdField.get(instance);
        } catch (IllegalAccessException e) {
          reportError(
              String.format(
                  "OI: Could not access field %s in class %s: %s",
                  cmdField.getName(), cls.getSimpleName(), e.getMessage()));
          continue;
        }

        if (command == null) {
          reportError(
              String.format(
                  "OI: Command %s in %s is null", cmdField.getName(), cls.getSimpleName()));
          continue;
        }

        switch (btnData.action()) {
          case WHEN_PRESSED -> button.onTrue(command);
          case WHEN_RELEASED -> button.onFalse(command);
          case WHILE_HELD -> button.whileTrue(command);
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
