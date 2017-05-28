package edu.wpi.first.wpilibj.command;

import java.util.Enumeration;

/**
 * Helper class to get the requirements of a {@link Command}. This is necessary
 * because the getRequirements method is package-scope, but we want to use it
 * anyway.
 * 
 * @author James Hagborg
 *
 */
public class GetRequirements {
    private GetRequirements() {
    }

    /**
     * Get the requirements of a command
     * 
     * @param cmd
     *            The command to use
     * @return An enumeration of the requirements
     */
    @SuppressWarnings("rawtypes")
    public static Enumeration getRequirements(Command cmd) {
        return cmd.getRequirements();
    }

}
