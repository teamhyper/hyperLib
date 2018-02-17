package org.usfirst.frc.team69.util.auto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.usfirst.frc.team69.util.CommandBuilder;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * The AutonomousRoutine class contains all the information needed for a
 * particular autonomous routine. It contains the code to build the command, as
 * well as information about what preferences need to be set, and what other
 * routines may be called as subroutines.
 * 
 * Logic for deciding which routine should be placed in the AutonomousStrategy
 * class.
 * 
 * @author James Hagborg
 *
 */
public abstract class AutonomousRoutine extends SendableBase {
    public static final String UNNAMED = "<unnamed routine>";

    private HashSet<AutonomousPreference> m_prefs = new HashSet<>();
    private HashSet<AutonomousRoutine> m_subroutines = new HashSet<>();

    /**
     * Construct a new AutonomousRoutine.
     */
    public AutonomousRoutine() {
        // Don't add this to livewindow
        super(false);

        String name = getClass().getSimpleName();
        if (name.isEmpty()) {
            name = UNNAMED;
        }
        setName(name);
    }

    /**
     * Abstract method for implementing the actual logic of the command.
     * Ideally, this would use the preferences and subroutines that have already
     * been declared.
     * 
     * @param builder
     *            The CommandBuilder object to use in building the command.
     */
    public abstract void build(CommandBuilder builder);

    /**
     * Get a list of the preferences that this routine requires.
     * 
     * @return A list of AutonomousPreference objects
     */
    public List<AutonomousPreference> getSupportedPreferences() {
        // Guava's ImmutableList would be nice right about now
        return new ArrayList<>(m_prefs);
    }

    /**
     * Add a preference of type double to the routine.
     * 
     * @param name
     *            The name of the preference
     */
    protected void addDoublePreference(String name) {
        m_prefs.add(new AutonomousPreference(this, name));
    }

    /**
     * Indicate that another routine is a subroutine of this one. This does not
     * affect the execution of code, but is used to display preferences to the
     * driver station. Adding two subroutines with the same name has no effect.
     * 
     * @param routine
     *            The subroutine to add.
     */
    protected void addSubroutine(AutonomousRoutine routine) {
        m_subroutines.add(Objects.requireNonNull(routine));
    }

    /**
     * Get a list of all routines which are included as subroutines in this
     * routine.
     * 
     * @return A list of AutonomousRoutine objects
     */
    public List<AutonomousRoutine> getSubroutines() {
        return new ArrayList<>(m_subroutines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("AutonomousRoutine");
        builder.addStringArrayProperty("Subroutines", this::getSubroutineNames, null);
        builder.addStringArrayProperty("Preferences", this::getPreferenceNames, null);
    }

    private String[] getPreferenceNames() {
        return m_prefs.stream().map(pref -> pref.getName()).toArray(String[]::new);
    }

    private String[] getSubroutineNames() {
        return m_subroutines.stream().map(rtn -> rtn.getName()).toArray(String[]::new);
    }

    /**
     * Two AutonomousRoutines are considered to be equal if they have the same
     * name, with the exception that two unnamed routines are equivalent iff
     * they occupy the same memory location.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AutonomousRoutine)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        AutonomousRoutine ar = (AutonomousRoutine) other;
        // Name should uniquely identify the routine
        return getName().equals(ar.getName()) && !getName().equals(UNNAMED);
    }

    /**
     * {@see #equals}
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
