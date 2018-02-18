package org.hyperonline.autoviewer.widget;

import org.hyperonline.autoviewer.AutonomousRoutineData;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.shuffleboard.api.sources.DataSource;
import edu.wpi.first.shuffleboard.api.util.NetworkTableUtils;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import edu.wpi.first.shuffleboard.plugin.networktables.sources.NetworkTableSource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

@Description(name = "Autonomous Routine Viewer", dataTypes = AutonomousRoutineData.class)
@ParametrizedController("AutonomousRoutineWidget.fxml")
public class AutonomousRoutineWidget extends SimpleAnnotatedWidget<AutonomousRoutineData> {

    public static final String PREFERENCES_LOCATION = "Preferences/$auto_routines";
    public static final String SEPERATOR = "/";

    @FXML
    private GridPane root;

    @FXML
    protected void initialize() {
        dataOrDefault.addListener((__, oldData, newData) -> {
            int i = 0;
            // clear didn't work for some reason
            // not gunna question it now....
            root.getChildren().removeIf(___ -> true);

            for (String prefName : newData.getPreferenceNames()) {
                String path = PREFERENCES_LOCATION + SEPERATOR + newData.getName() + SEPERATOR + prefName;

                if (!NetworkTableUtils.rootTable.containsKey(path)) {
                    NetworkTableEntry entry = NetworkTableUtils.rootTable.getEntry(path);
                    entry.setDouble(0.0);
                }

                path = NetworkTable.normalizeKey(path, false);

                System.out.println("type = " + NetworkTableUtils.rootTable.getEntry(path).getType());
                System.out.println("exists = " + NetworkTableUtils.rootTable.getEntry(path).exists());
                System.out.println("containskey = " + NetworkTableUtils.rootTable.containsKey(path));
                // Java can't check this is a valid cast at runtime
                // So we try to get the data to see if it works
                @SuppressWarnings("unchecked")
                DataSource<Double> source = (DataSource<Double>) NetworkTableSource.forKey(path);
                try {
                    source.getData();
                } catch (ClassCastException e) {
                    System.out.println("Wrong type, using none source");
                    source = DataSource.none();
                }

                Spinner<Double> spinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
                spinner.setValueFactory(
                        new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0));
                spinner.setEditable(true);
                spinner.getValueFactory().valueProperty().bindBidirectional(source.dataProperty());
                GridPane.setRowIndex(spinner, i);
                GridPane.setColumnIndex(spinner, 1);
                root.getChildren().add(spinner);

                Label label = new Label(prefName);
                GridPane.setColumnIndex(label, 0);
                GridPane.setRowIndex(label, i);
                root.getChildren().add(label);
                i++;
            }

            for (String rtnName : newData.getSubroutineNames()) {

            }
        });
    }

    @Override
    public Pane getView() {
        return root;
    }

}
