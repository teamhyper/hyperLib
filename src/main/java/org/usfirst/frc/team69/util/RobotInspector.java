package org.usfirst.frc.team69.util;

import java.io.IOException;

import org.usfirst.frc.team69.util.port.DuplicatePortException;
import org.usfirst.frc.team69.util.port.InvalidPortException;
import org.usfirst.frc.team69.util.port.PortMapper;

/**
 * This is the entry point for running RobotInspector.  This will verify both the
 * operator interface and port mapping and generate diagrams in the /diagrams
 * directory.  To run this, right-click in eclipse and run as a java application.
 * 
 * @author James Hagborg
 *
 */
public class RobotInspector {

    public static void main(String[] args) {
//        try {
//            System.out.println("Checking operator interface...");
//            new JoystickMapper().mapJoysticks();
//            System.out.println("OI successfully mapped.");
//        } catch (DuplicateButtonException | InvalidButtonException e) {
//            System.out.println("Button mapping error: " + e.getMessage());
//            System.out.println("Check your OI file!");
//        } catch (IOException e) {
//            System.out.println("IOException occured while making joystick map: " + e.getMessage());
//        }

        System.out.println();
	// We need to turn this off temporarily, until we can accept the class name as a command line arg

	/*
        try {
            System.out.println("Checking RobotMap...");
            new PortMapper().mapPorts(RobotMap.class);
            System.out.println("Robot ports successfully mapped.");
        } catch (DuplicatePortException | InvalidPortException e) {
            System.out.println("Wiring error: " + e.getMessage());
            System.out.println("Check your RobotMap file!");
        } catch (IOException e) {
            System.out.println("IOException occured while making wiring diagram: " + e.getMessage());
        }
	*/
    }


}
