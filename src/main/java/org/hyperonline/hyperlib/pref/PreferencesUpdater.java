package org.hyperonline.hyperlib.pref;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import org.hyperonline.hyperlib.PeriodicScheduler;

/**
 * class that handles checking for {@link org.hyperonline.hyperlib.pref.PreferencesSet} updates.
 * allows automatic (via {@link org.hyperonline.hyperlib.PeriodicScheduler} or manual (via ${link PreferenceUpdateCommand}) checking
 *
 * @author Chris McGroarty
 */
public class PreferencesUpdater {
  private static final ParallelCommandGroup manualCheckForUpdateCmd = new ParallelCommandGroup();
  private static boolean hasSetAutoUpdate = false;
  private static boolean autoCheckForUpdate = true; // default to true so old robot code behaves as expected

  public static void setAutoCheckForUpdate(boolean autoCheckForUpdate) {
    if (!PreferencesUpdater.hasSetAutoUpdate) {
      PreferencesUpdater.autoCheckForUpdate = autoCheckForUpdate;
      PreferencesUpdater.hasSetAutoUpdate = true;
      // add our manual update check command to the SendableRegistry
      SendableRegistry.add(PreferencesUpdater.manualCheckForUpdateCmd, "Manual Check for Preference Updates");
    } else {
      throw new IllegalStateException("PreferencesSet auto update already configured");
    }
  }

  public static void addUpdateChecker(Runnable preferencesSetUpdateChecker) {
    if(PreferencesUpdater.autoCheckForUpdate) {
      // if we're allowing automatic updates, add to the PeriodicSchedule
      PeriodicScheduler.getInstance().addEvent(preferencesSetUpdateChecker);
    } else {
      // otherwise, check on creation for updates
      preferencesSetUpdateChecker.run();
      // and add our checkForUpdates method to the manual update command
      PreferencesUpdater.manualCheckForUpdateCmd.addCommands(
          new PreferenceUpdateCommand(preferencesSetUpdateChecker));
    }
  }

  /**
   * command that extends {@link InstantCommand} to check for updates even when disabled.
   *
   * @author Chris McGroarty
   */
  private static class PreferenceUpdateCommand extends InstantCommand {

    public PreferenceUpdateCommand(Runnable toRun) {
      super(toRun);
    }

    @Override
    public boolean runsWhenDisabled() {
      return true;
    }
  }
}
