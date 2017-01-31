package org.usfirst.frc.team69.util.oi;

import java.util.List;

/**
 * The {@link Validator} class checks that the OI map follows a certain set of
 * rules, to check for common errors.  This runs as part of RobotInspector
 * 
 * @author James Hagborg

 * @see OI
 */
public class Validator {

    /**
     * Validate the OI according to a list of rules
     * 
     * @param oi A list of {@link JoystickData} objects to validate
     * @throws BadOIMapException if validation fails
     */
    public static void validate(List<JoystickData> oi) throws BadOIMapException {
        checkButtonsInRange();
        checkJoysticksInRange();
        checkSyntax();
    }
    
    private static void checkButtonsInRange() {
        // TODO
    }
    
    private static void checkJoysticksInRange() {
        // TODO
    }
    
    private static void checkSyntax() {
       // TODO
    }

}
