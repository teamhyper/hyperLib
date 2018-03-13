package org.usfirst.frc.team69.util.oi.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.usfirst.frc.team69.util.oi.ButtonData;
import org.usfirst.frc.team69.util.oi.ButtonData.Action;
import org.usfirst.frc.team69.util.oi.JoystickData;
import org.usfirst.frc.team69.util.oi.MapJoystick;
import org.usfirst.frc.team69.util.oi.MapJoystick.Role;
import org.usfirst.frc.team69.util.oi.MapJoystick.Type;
import org.usfirst.frc.team69.util.oi.OI;
import org.usfirst.frc.team69.util.oi.WhenPressed;
import org.usfirst.frc.team69.util.oi.WhenReleased;
import org.usfirst.frc.team69.util.oi.WhileHeld;

import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 * @author James
 *
 */
public class OIBaseTest {

	/**
	 * 
	 * @author James
	 *
	 */
    public static class SingleJoystickMap {
    	/**
    	 * 
    	 * @author James
    	 *
    	 */
        @MapJoystick(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_2_AXIS)
        public static class LeftDriver {
            @WhenPressed(0) public final Command foo = null;
            @WhenPressed(4) public final Command bar = null;
            @WhileHeld(2) public final Command baz = null;
            @WhenReleased(2) public final Command buzz = null;
        }
    }
    /**
     * 
     * @author James
     *
     */
    public static class DoubleJoystickMap {
    	/**
    	 * 
    	 * @author James
    	 *
    	 */
        @MapJoystick(port = 0, role = Role.LEFT_DRIVER, type = Type.LOGITECH_2_AXIS)
        public static class LeftDriver {
            @WhenPressed(0) public final Command foo = null;
            @WhenPressed(1) public final Command bar = null;
        }
        /**
         * 
         * @author James
         *
         */
        @MapJoystick(port = 1, role = Role.RIGHT_DRIVER, type = Type.LOGITECH_3_AXIS)
        public static class RightDriver {
            @WhileHeld(0) public final Command baz = null;
            @WhenReleased(1) public final Command buzz = null;
        }
    }
    /**
     * Test mapping a single joystick
     */
    @Test
    public void testSingleJoystick() {
        OI oi = new OI(SingleJoystickMap.class, false);
        
        ArrayList<JoystickData> joysticks = oi.getJoystickData();
        assertEquals(1, joysticks.size());
        
        JoystickData js = joysticks.get(0);
        assertEquals(0, js.port());
        assertEquals("LeftDriver", js.name());
        assertEquals(Role.LEFT_DRIVER, js.role());
        assertEquals(Type.LOGITECH_2_AXIS, js.type());
    }
    /**
     * Test mapping with two joysticks
     */
    @Test
    public void testTwoJoysticks() {
        OI oi = new OI(DoubleJoystickMap.class, false);
        
        ArrayList<JoystickData> joysticks = oi.getJoystickData();
        assertEquals(2, joysticks.size());
        
        JoystickData js1 = joysticks.get(0);
        assertEquals(0, js1.port());
        assertEquals("LeftDriver", js1.name());
        assertEquals(Role.LEFT_DRIVER, js1.role());
        assertEquals(Type.LOGITECH_2_AXIS, js1.type());
        
        JoystickData js2 = joysticks.get(1);
        assertEquals(1, js2.port());
        assertEquals("RightDriver", js2.name());
        assertEquals(Role.RIGHT_DRIVER, js2.role());
        assertEquals(Type.LOGITECH_3_AXIS, js2.type());
    }
    /**
     * Test buttons on a single joystick
     */
    @Test
    public void testButtonsSingleJoystick() {
        OI oi = new OI(SingleJoystickMap.class, false);
        JoystickData js = oi.getJoystickData().get(0);
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
    public void testButtonsDoubleJoystick() {
        OI oi = new OI(DoubleJoystickMap.class, false);
        List<JoystickData> joysticks = oi.getJoystickData();
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
    /**
     * Test getting the left driver joystick from PC
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeftDriverFromPC() {
        OI oi = new OI(SingleJoystickMap.class, false);
        oi.leftDriver();
    }
    
    /**
     * Test getting a single joystick from PC
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetJoystickFromPC() {
        OI oi = new OI(SingleJoystickMap.class, false);
        oi.getJoystick(0);
    }
    
    /**
     * Test getting an empty joystick from PC
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetEmptyJoystickFromPC() {
        OI oi = new OI(SingleJoystickMap.class, false);
        oi.getJoystick(1);
    }
    
    /**
     * Test initializing commands from PC
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInitCommandsFromPC() {
        OI oi = new OI(SingleJoystickMap.class, false);
        oi.initCommands();
    }
    
    /**
     * Test that the button list is immutable
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testButtonListImmutable() {
        OI oi = new OI(SingleJoystickMap.class, false);
        oi.getJoystickData().get(0).buttons().clear();
    }
}
