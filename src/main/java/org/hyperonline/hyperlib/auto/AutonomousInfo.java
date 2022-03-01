package org.hyperonline.hyperlib.auto;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.util.sendable.SendableRegistry;

import java.util.Collection;
import java.util.HashMap;

/**
 * {@link AutonomousInfo}
 *
 * @author James
 */
public class AutonomousInfo implements NTSendable {
  private final HashMap<String, AutonomousRoutine> m_allRoutines;
  private final HashMap<String, AutonomousStrategy> m_allStrategies;
  private final String m_defaultSelection;
  private NetworkTableEntry m_currentSelection;

  private AutonomousInfo(
      HashMap<String, AutonomousRoutine> routines,
      HashMap<String, AutonomousStrategy> strats,
      String defaultSelection) {
    SendableRegistry.setName(this, "AutonomousInfo");
    m_allRoutines = new HashMap<>(routines);
    m_allStrategies = new HashMap<>(strats);
    m_defaultSelection = defaultSelection;
  }

  /**
   * @return {AutonomousStrategy}
   */
  public AutonomousStrategy getSelection() {
    String selection = m_defaultSelection;
    System.out.println("Entering getSelection");
    if (m_currentSelection != null) {
      System.out.println("m_currentSelection is not null, so I'm picking");
      System.out.println("Full path of entry: " + m_currentSelection.getInfo().name);
      if (!m_currentSelection.exists()) {
        System.out.println("Entry does not exist.  That's an issue.");
      }
      selection = m_currentSelection.getString(m_defaultSelection);
    }
    System.out.println("Selected: " + selection);
    System.out.println("Done with getSelection");
    return m_allStrategies.get(selection);
  }

  public Collection<AutonomousStrategy> getStrategies() {
    return m_allStrategies.values();
  }

  public AutonomousStrategy getDefault() {
    return m_allStrategies.get(m_defaultSelection);
  }

  @Override
  public void initSendable(NTSendableBuilder builder) {
    builder.setSmartDashboardType("AutonomousInfo");
    for (AutonomousRoutine rtn : m_allRoutines.values()) {
      rtn.initSendable("Routines/" + rtn.getName() + "/", builder);
    }
    for (AutonomousStrategy strat : m_allStrategies.values()) {
      strat.initSendable("Strategies/" + strat.getName() + "/", builder);
    }
    builder.addStringProperty("Default", () -> m_defaultSelection, null);
    m_currentSelection = builder.getEntry("Selection");
    System.out.println(
        "In initSendable: the path of the entry is " + m_currentSelection.getInfo().name);
  }

  /**
   * {@link Builder}
   *
   * @author James
   */
  public static class Builder {
    private HashMap<String, AutonomousRoutine> m_allRoutines = new HashMap<>();
    private HashMap<String, AutonomousStrategy> m_allStrategies = new HashMap<>();
    private String m_defaultSelection;

    /**
     * @param strat strategy to add
     * @return {Builder}
     */
    public Builder addStrategy(AutonomousStrategy strat) {
      m_allStrategies.put(strat.getName(), strat);
      for (AutonomousRoutine rtn : strat.getPossibleRoutines()) {
        addWithSubroutines(rtn);
      }
      return this;
    }

    private void addWithSubroutines(AutonomousRoutine rtn) {
      if (!m_allRoutines.containsKey(rtn.getName())) {
        m_allRoutines.put(rtn.getName(), rtn);
        for (AutonomousRoutine sub : rtn.getSubroutines()) {
          addWithSubroutines(sub);
        }
      }
    }
    /**
     * @param strat strategy to set as the default
     * @return {Builder}
     */
    public Builder addDefault(AutonomousStrategy strat) {
      m_defaultSelection = strat.getName();
      addStrategy(strat);
      return this;
    }
    /**
     * @return {AutonomousInfo}
     */
    public AutonomousInfo build() {
      return new AutonomousInfo(m_allRoutines, m_allStrategies, m_defaultSelection);
    }
  }
}
