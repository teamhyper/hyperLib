package org.usfirst.frc.team69.util;

import java.io.IOException;

import org.usfirst.frc.team69.util.oi.BadOIMapException;
import org.usfirst.frc.team69.util.oi.OI;
import org.usfirst.frc.team69.util.port.DuplicatePortException;
import org.usfirst.frc.team69.util.port.InvalidPortException;
import org.usfirst.frc.team69.util.port.PortMapper;

/**
 * This is the entry point for running RobotInspector. This will verify both the
 * operator interface and port mapping and generate diagrams in the /diagrams
 * directory.
 * 
 * The class names for the port map and OI map must be specified in the system
 * properties.  The ant build file at ant/robot_inspector.xml should set this
 * up for you.
 * 
 * @author James Hagborg
 *
 */
public class RobotInspector {

    /**
     * The entry point into the RobotInspector application.
     * @param args Not used.  Pass class names through system properties instead
     */
    public static void main(String[] args) {
        boolean errored = false;

        String oiClassName = System.getProperty("robotinspector.oimap.class");

        if (oiClassName == null) {
            System.out.println("OI class not set, so not checking");
        } else {
            try {
                Class <?> mapClass = RobotInspector.class.getClassLoader().loadClass(oiClassName);
                System.out.println("Checking operator interface at " + mapClass.getName() + "...");
                OI oi = new OI(mapClass, false);
                System.out.println("Validating...");
                oi.validate();
                System.out.println("Mapping...");
                oi.drawMaps();
                System.out.println("Robot ports successfully mapped.");
            } catch (ClassNotFoundException e) {
                System.err.printf("Could not find robot map class: %s", oiClassName);
                errored = true;
            } catch (BadOIMapException e) {
                System.err.println("OI error: " + e.getMessage());
                System.err.println("Check your OIMap file!");
                errored = true;
            } catch (IOException e) {
                System.err.println("IOException occured while making joystick diagram: " + e.getMessage());
                System.err.println("We can continue with the build, but you don't get any diagrams.");
            }
        }

        String mapClassName = System.getProperty("robotinspector.robotmap.class");

        if (mapClassName == null) {
            System.out.println("Robot Map class is not set, so nothing to check");
        } else {
            try {
                Class <?> mapClass = RobotInspector.class.getClassLoader().loadClass(mapClassName);
                System.out.println("Checking robot map at " + mapClass.getName() + "...");
                new PortMapper().mapPorts(mapClass);
                System.out.println("Robot ports successfully mapped.");
            } catch (ClassNotFoundException e) {
                System.err.printf("Could not find robot map class: %s", mapClassName);
                errored = true;
            } catch (DuplicatePortException | InvalidPortException e) {
                System.err.println("Wiring error: " + e.getMessage());
                System.err.println("Check your RobotMap file!");
                errored = true;
            } catch (IOException e) {
                System.err.println("IOException occured while making wiring diagram: " + e.getMessage());
                System.err.println("We can continue with the build, but you don't get any diagrams.");
            }
        }
        
        if (errored) {
            System.exit(-1);
        }
    }
}
