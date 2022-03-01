package org.hyperonline.hyperlib.auto;

import edu.wpi.first.networktables.NTSendableBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * {@link AutonomousStrategy}
 *
 * @author James
 */
public class AutonomousStrategy {

  private final Map<String, AutonomousRoutine> m_map;
  private final AutonomousRoutine m_default;
  private final String m_name;

  private AutonomousStrategy(
      String name, Map<String, AutonomousRoutine> map, AutonomousRoutine def) {
    m_name = name;
    m_map = new HashMap<>(map);
    m_default = def;
  }

  /**
   * @return {AutonomousRoutine}
   */
  public AutonomousRoutine getDefaultRoutine() {
    return m_default;
  }

  /**
   * @param input value of scenario to get the routine for
   * @return {AutonomousRoutine}
   */
  public AutonomousRoutine getRoutineForScenario(String input) {
    return m_map.getOrDefault(input, m_default);
  }

  public String getName() {
    return m_name;
  }

  public Collection<AutonomousRoutine> getPossibleRoutines() {
    return m_map.values();
  }

  void initSendable(String prefix, NTSendableBuilder builder) {
    builder.addStringProperty(prefix + ".type", () -> "AutonomousStrategy", null);
    if (m_default != null) {
      builder.addStringProperty(prefix + "Default", m_default::getName, null);
    }
    for (Entry<String, AutonomousRoutine> ent : m_map.entrySet()) {
      builder.addStringProperty(
          prefix + "Scenarios/" + ent.getKey(), () -> ent.getValue().getName(), null);
    }
  }

  /**
   * {@link Builder}
   *
   * @author James
   */
  public static class Builder {
    private final HashMap<String, AutonomousRoutine> m_map = new HashMap<>();
    private AutonomousRoutine m_default;
    private String m_name;

    /**
     * @param name the strategy's name
     */
    public Builder(String name) {
      m_name = Objects.requireNonNull(name);
    }

    /**
     * @param input value of the scenario to match
     * @param rtn routine to be run in this scenario
     * @return {Builder}
     */
    public Builder addScenario(String input, AutonomousRoutine rtn) {
      Objects.requireNonNull(input);
      Objects.requireNonNull(rtn);
      m_map.put(input, rtn);
      return this;
    }

    /**
     * @param rtn routine to set as the default for this AutonomousStrategy
     * @return {Builder}
     */
    public Builder addDefault(AutonomousRoutine rtn) {
      m_default = rtn;
      return this;
    }

    /**
     * @return {AutonomousStrategy}
     */
    public AutonomousStrategy build() {
      return new AutonomousStrategy(m_name, m_map, m_default);
    }
  }
}
