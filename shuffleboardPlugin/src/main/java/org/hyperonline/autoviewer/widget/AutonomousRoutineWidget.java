package org.hyperonline.autoviewer.widget;

import org.hyperonline.autoviewer.AutonomousRoutineData;
import org.hyperonline.autoviewer.AutonomousRoutineType;

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

    public static final String AUTO_ROUTINES_LOCATION = "AutoRoutines";
    public static final String PREFERENCES_LOCATION = "Preferences/$auto_preferences";
    public static final String SEPERATOR = "/";

    @FXML
    private GridPane root;

    @FXML
    protected void initialize() {
        dataOrDefault.addListener((__, oldData, newData) -> {
            // clear didn't work for some reason
            // not gunna question it now....
            root.getChildren().removeIf(___ -> true);

            int row = 0;
            row = addRoutineToUI(row, newData);

            for (String rtnName : newData.getSubroutineNames()) {
                AutonomousRoutineData rtn = getRoutineData(rtnName);
                row = addRoutineToUI(row, rtn);
            }
        });
    }
    
    private int addRoutineToUI(int row, AutonomousRoutineData data) {
        for (String prefName : data.getPreferenceNames()) {
            DataSource<Double> source = getDataSource(data.getName(), prefName);
            addUIEntry(row++, prefName, source);
        }
        return row;
    }
    
    private AutonomousRoutineData getRoutineData(String name) {
        String path = AUTO_ROUTINES_LOCATION + SEPERATOR + name;
        DataSource<?> source = NetworkTableSource.forKey(path);
        if (source.getDataType() != AutonomousRoutineType.Instance) {
            System.out.println("Routine " + name + " does not have the right type!");
            return new AutonomousRoutineData(name, new String[0], new String[0]);
        }
        return (AutonomousRoutineData) source.getData();
    }
    
    private void addUIEntry(int rowNum, String prefName, DataSource<Double> source) {
        Spinner<Double> spinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
        spinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0));
        spinner.setEditable(true);
        spinner.getValueFactory().valueProperty().bindBidirectional(source.dataProperty());
        GridPane.setRowIndex(spinner, rowNum);
        GridPane.setColumnIndex(spinner, 1);
        root.getChildren().add(spinner);

        Label label = new Label(prefName);
        GridPane.setColumnIndex(label, 0);
        GridPane.setRowIndex(label, rowNum);
        root.getChildren().add(label);
    }
    
    private DataSource<Double> getDataSource(String routine, String pref) {
        String path = PREFERENCES_LOCATION + SEPERATOR + routine + SEPERATOR + pref;

        if (!NetworkTableUtils.rootTable.containsKey(path)) {
            NetworkTableEntry entry = NetworkTableUtils.rootTable.getEntry(path);
            entry.setDouble(0.0);
        }

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
        
        return source;
    }

    @Override
    public Pane getView() {
        return root;
    }

}
