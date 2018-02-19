package org.hyperonline.autoviewer.widget;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import org.hyperonline.autoviewer.AutonomousInfo;
import org.hyperonline.autoviewer.AutonomousRoutineData;
import org.hyperonline.autoviewer.AutonomousStrategyData;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.shuffleboard.api.sources.DataSource;
import edu.wpi.first.shuffleboard.api.util.NetworkTableUtils;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import edu.wpi.first.shuffleboard.plugin.networktables.sources.NetworkTableSource;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

@Description(name = "Autonomous Info Viewer", dataTypes = AutonomousInfo.class)
@ParametrizedController("AutonomousInfoWidget.fxml")
public class AutonomousInfoWidget extends SimpleAnnotatedWidget<AutonomousInfo> {

    public static final String PREFERENCES_LOCATION = "Preferences/$auto_preferences";
    public static final String SEPERATOR = "/";
    
    @FXML
    private BorderPane root;
    @FXML
    private ChoiceBox<String> chooser;
    @FXML
    private GridPane content;
    
    class NTSpinnerBinding {
        public final Spinner<Double> spinner;
        public final DataSource<Double> source;
        public NTSpinnerBinding(Spinner<Double> sp, DataSource<Double> sr) {
            spinner = sp;
            source = sr;
        }
    }
    
    private ArrayList<NTSpinnerBinding> bindings = new ArrayList<>();
    private boolean m_weSelected = false;
    
    @FXML
    protected void initialize() {
        dataOrDefault.addListener((__, oldData, newData) -> {
            // Update the chooser if needed
            for (String strat : newData.getStrategyNames()) {
                System.out.println("Strategy: " + strat);
            }
            System.out.printf("default=%s, selected=%s, #routines=%d", newData.getDefault(), newData.getSelection(), newData.getRoutineNames().size());
            if (!oldData.getStrategyNames().equals(newData.getStrategyNames())) {
                chooser.getItems().clear();
                chooser.getItems().addAll(newData.getStrategyNames());
            }
            if (!Objects.equals(oldData.getSelection(), newData.getSelection())) {
                updateSelection(newData);
            }
            
            // Populate the content
            // Nearly any change could indirectly mean we need to update this
            repopulateContent(newData);
        });
        chooser.getSelectionModel()
               .selectedItemProperty()
               .addListener((__, oldData, newData) -> {
                   if (!m_weSelected) {
                       setData(getData().withSelection(newData));
                   }
                   m_weSelected = false;
               });
    }
    
    @Override
    public Pane getView() {
        return root;
    }
    
    private void updateSelection(AutonomousInfo newData) {
        System.out.println("Update from network: " + newData.getSelection());
        m_weSelected = true;
        if (newData.getSelection() != null) {
            chooser.getSelectionModel().select(newData.getSelection());
        } else {
            chooser.getSelectionModel().clearSelection();
        }
    }
    
    private void unbindAll() {
        for (NTSpinnerBinding binding : bindings) {
            binding.spinner.getValueFactory().valueProperty().unbindBidirectional(binding.source.dataProperty());
        }
        bindings.clear();
    }
    
    private void resetContent() {
        System.out.println("Resetting content");
        unbindAll();
        content.getChildren().removeIf(__ -> true);
    }
    
    private void repopulateContent(AutonomousInfo newData) {
        resetContent();
        
        String stratName = newData.getSelection();
        if (stratName == null) {
            content.getChildren().add(new Label("No strategy selected"));
            return;
        }
        AutonomousStrategyData strat = newData.getStrategy(stratName);
        if (strat == null) {
            content.getChildren().add(new Label("Data for strategy '" + stratName + "' not found"));
            return;
        }
        
        System.out.println("Routines: " + String.join(",", strat.getPossibleRoutines()));
        
        int col = 0;
        for (String routine : strat.getPossibleRoutines()) {
            Label header = new Label("If the FMS data is one of: " 
                    + String.join(",", strat.scenariosForRoutine(routine))
                    + "\nThen " + routine + " will run.");
            GridPane.setColumnSpan(header, 2);
            GridPane.setColumnIndex(header, col);
            content.getChildren().add(header);
            populateColumn(col, newData, routine);
            col += 2;
        }
        String defaultRoutine = strat.getDefault();
        if (defaultRoutine == null) {
            Label header = new Label("No default routine is set.");
            GridPane.setColumnSpan(header, 2);
            GridPane.setColumnIndex(header, col);
            content.getChildren().add(header);
        } else {
            Label header = new Label("The default routine is " + defaultRoutine + ".\n");
            GridPane.setColumnSpan(header, 2);
            GridPane.setColumnIndex(header, col);
            content.getChildren().add(header);
            
            populateColumn(col, newData, defaultRoutine);
        }
    }
    
    private void populateColumn(int col, AutonomousInfo newData, String routine) {
        int row = 1;
        for (String rtn : getAllSubroutines(newData, routine)) {
            AutonomousRoutineData rtnData = newData.getRoutine(rtn);
            if (rtnData != null) { 
                row = addHeader(row, col, "Preferences declared in " + rtn + ":");
                row = addRoutineToUI(row, col, newData.getRoutine(rtn));
            }
        }
    }
    
    private List<String> getAllSubroutines(AutonomousInfo info, String routine) {
        Deque<String> toSearch = new ArrayDeque<>();
        List<String> result = new ArrayList<>();
        
        toSearch.push(routine);
        while (!toSearch.isEmpty()) {
            String top = toSearch.pop();
            System.out.println("Processing: " + top);
            if (result.contains(top)) {
                continue;
            }
            result.add(top);
            AutonomousRoutineData rtn = info.getRoutine(top);
            if (rtn == null) {
                System.out.println("subroutine was null!");
                continue;
            }
            System.out.println(rtn.getSubroutineNames().length);
            for (String sub : rtn.getSubroutineNames()) {
                System.out.println("Processing subroutines");
                toSearch.push(sub);
            }
        }
        
        return result;
    }
    
    private int addRoutineToUI(int row, int col, AutonomousRoutineData data) {
        for (String prefName : data.getPreferenceNames()) {
            DataSource<Double> source = getDataSource(data.getName(), prefName);
            addUIEntry(row++, col, prefName, source);
        }
        return row;
    }
    
    private int addHeader(int rowNum, int colNum, String text) {
        if (rowNum != 0) {
            Separator sep = new Separator(Orientation.HORIZONTAL);
            sep.setMinHeight(10);
            GridPane.setColumnSpan(sep, 2);
            GridPane.setRowIndex(sep, rowNum++);
            GridPane.setColumnIndex(sep, colNum);
            content.getChildren().add(sep);
        }
        
        Label header = new Label(text);
        GridPane.setColumnSpan(header, 2);
        GridPane.setRowIndex(header, rowNum++);
        GridPane.setColumnIndex(header, colNum);
        content.getChildren().add(header);
        
        return rowNum;
    }
    
    private void addUIEntry(int rowNum, int colNum, String prefName, DataSource<Double> source) {
        Spinner<Double> spinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
        spinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0));
        spinner.setEditable(true);
        spinner.getValueFactory().valueProperty().bindBidirectional(source.dataProperty());
        GridPane.setRowIndex(spinner, rowNum);
        GridPane.setColumnIndex(spinner, colNum + 1);
        content.getChildren().add(spinner);

        Label label = new Label(prefName);
        GridPane.setColumnIndex(label, colNum);
        GridPane.setRowIndex(label, rowNum);
        content.getChildren().add(label);
        
        bindings.add(new NTSpinnerBinding(spinner, source));
    }
    
    private DataSource<Double> getDataSource(String routine, String pref) {
        String path = PREFERENCES_LOCATION + SEPERATOR + routine + SEPERATOR + pref;

        NetworkTableEntry entry = NetworkTableUtils.rootTable.getEntry(path);
        entry.setDefaultDouble(0.0);
        entry.setPersistent();

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
}
