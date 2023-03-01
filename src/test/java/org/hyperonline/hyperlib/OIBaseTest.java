package org.hyperonline.hyperlib;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import org.hyperonline.hyperlib.oi.*;
import org.hyperonline.hyperlib.oi.ButtonData.Action;
import org.hyperonline.hyperlib.oi.MapController.Role;
import org.hyperonline.hyperlib.oi.MapController.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OIBaseTest {

    public static class SingleJoystickMap {
        @MapController(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_2_AXIS)
        public static class LeftDriver {
            @WhenPressed(0) public final Command foo = null;
            @WhenPressed(4) public final Command bar = null;
            @WhileHeld(2) public final Command baz = null;
            @WhenReleased(2) public final Command buzz = null;
        }
    }
    
    
    public static class DoubleJoystickMap {
        @MapController(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_2_AXIS)
        public static class LeftDriver {
            @WhenPressed(0) public final Command foo = null;
            @WhenPressed(1) public final Command bar = null;
        }
        
        @MapController(port = 1, role = Role.RIGHT_DRIVER, type = Type.LOGITECH_3_AXIS)
        public static class RightDriver {
            @WhileHeld(0) public final Command baz = null;
            @WhenReleased(1) public final Command buzz = null;
        }
    }

    public static class ControllerJoystickMap {
        @MapController(port = 0, role = Role.LEFT_DRIVER, type = Type.XBOX)
        public static class LeftDriver {
            @WhenPressed(1) public final Command foo = null;
            @WhenReleased(2) public final Command bar = null;
        }

        @MapController(port = 1, role = Role.RIGHT_DRIVER, type = Type.PS4)
        public static class RightDriver {
            @WhenPressed(0) public final Command foo = null;
            @WhenReleased(1) public final Command bar = null;
        }

        @MapController(port = 2, role = Role.LEFT_OPERATOR, type = Type.LOGITECH_2_AXIS)
        public static class LeftOperator {
            @WhenPressed(0) public final Command foo = null;
            @WhenReleased(1) public final Command bar = null;
        }

        @MapController(port = 3, role = Role.RIGHT_OPERATOR, type = Type.LOGITECH_3_AXIS)
        public static class RightOperator {
            @WhenPressed(0) public final Command foo = null;
            @WhenReleased(1) public final Command bar = null;
        }
    }

    /**
     * Test mapping a single joystick
     */
    @Test
    public void testSingleJoystick() throws BadOIMapException {
        OI<Joystick, ?, ?, ?> oi = new OI<>(SingleJoystickMap.class, false);
        
        ArrayList<ControllerData> joysticks = oi.getControllerData();
        assertEquals(1, joysticks.size());
        
        ControllerData js = joysticks.get(0);
        assertEquals(0, js.port());
        assertEquals("LeftDriver", js.name());
        assertEquals(Role.LEFT_DRIVER, js.role());
        assertEquals(Type.LOGITECH_2_AXIS, js.type());
    }
    /**
     * Test mapping with two joysticks
     */
    @Test
    public void testTwoJoysticks() throws BadOIMapException {
        OI<Joystick, Joystick, ?, ?> oi = new OI<>(DoubleJoystickMap.class, false);
        
        ArrayList<ControllerData> joysticks = oi.getControllerData();
        assertEquals(2, joysticks.size());
        
        ControllerData js1 = joysticks.get(0);
        assertEquals(0, js1.port());
        assertEquals("LeftDriver", js1.name());
        assertEquals(Role.LEFT_DRIVER, js1.role());
        assertEquals(Type.LOGITECH_2_AXIS, js1.type());
        
        ControllerData js2 = joysticks.get(1);
        assertEquals(1, js2.port());
        assertEquals("RightDriver", js2.name());
        assertEquals(Role.RIGHT_DRIVER, js2.role());
        assertEquals(Type.LOGITECH_3_AXIS, js2.type());
    }
    /**
     * Test buttons on a single joystick
     */
    @Test
    public void testButtonsSingleJoystick() throws BadOIMapException {
        OI<Joystick, ?, ?, ?> oi = new OI<>(SingleJoystickMap.class, false);
        ControllerData js = oi.getControllerData().get(0);
        List<ButtonData> buttons = js.buttons();
        
        assertEquals(4, buttons.size());
        
        assertEquals(0, buttons.get(0).port());
        assertEquals(Action.WHEN_PRESSED, buttons.get(0).action());
        assertEquals("foo", buttons.get(0).name());
        
        assertEquals(4, buttons.get(1).port());
        assertEquals(Action.WHEN_PRESSED, buttons.get(1).action());
        assertEquals("bar", buttons.get(1).name());
        
        assertEquals(2, buttons.get(2).port());
        assertEquals(Action.WHILE_HELD, buttons.get(2).action());
        assertEquals("baz", buttons.get(2).name());
        
        assertEquals(2, buttons.get(3).port());
        assertEquals(Action.WHEN_RELEASED, buttons.get(3).action());
        assertEquals("buzz", buttons.get(3).name());
    }
    
    /**
     * Test buttons with double joysticks
     */
    @Test
    public void testButtonsDoubleJoystick() throws BadOIMapException {
        OI<Joystick, Joystick, ?, ?> oi = new OI<>(DoubleJoystickMap.class, false);

        List<ControllerData> joysticks = oi.getControllerData();
        List<ButtonData> buttons = joysticks.get(0).buttons();
        assertEquals(2, buttons.size());
        
        assertEquals(0, buttons.get(0).port());
        assertEquals(Action.WHEN_PRESSED, buttons.get(0).action());
        assertEquals("foo", buttons.get(0).name());
        
        assertEquals(1, buttons.get(1).port());
        assertEquals(Action.WHEN_PRESSED, buttons.get(1).action());
        assertEquals("bar", buttons.get(1).name());
        
        buttons = joysticks.get(1).buttons();
        assertEquals(2, buttons.size());
        
        assertEquals(0, buttons.get(0).port());
        assertEquals(Action.WHILE_HELD, buttons.get(0).action());
        assertEquals("baz", buttons.get(0).name());
        
        assertEquals(1, buttons.get(1).port());
        assertEquals(Action.WHEN_RELEASED, buttons.get(1).action());
        assertEquals("buzz", buttons.get(1).name());
    }

    @Test
    public void testControllers() throws BadOIMapException {
        OI<XboxController, PS4Controller, Joystick, Joystick> oi = new OI<>(ControllerJoystickMap.class, false);

        assertInstanceOf(XboxController.class, oi.leftDriver());
        assertInstanceOf(PS4Controller.class, oi.rightDriver());
        assertInstanceOf(Joystick.class, oi.leftOperator());
        assertInstanceOf(Joystick.class, oi.rightOperator());
        fail();
    }
}
