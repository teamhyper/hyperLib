package org.hyperonline.autoviewer.widget;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import org.hyperonline.autoviewer.AutonomousInfo;
import org.hyperonline.autoviewer.AutonomousRoutineData;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.shuffleboard.api.sources.DataSource;
import edu.wpi.first.shuffleboard.api.util.NetworkTableUtils;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import edu.wpi.first.shuffleboard.plugin.networktables.sources.NetworkTableSource;
import javafx.application.Platform;
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
        System.out.println("Initializing!!!!");
        dataOrDefault.addListener((__, oldData, newData) -> {
            // Update the chooser if needed
            System.out.printf("default=%s, selected=%s, #routines=%d", newData.getDefault(), newData.getSelection(), newData.getRoutineNames().size());
            if (!oldData.getRoutineNames().equals(newData.getRoutineNames())) {
                System.out.println("Chainging items");
                chooser.getItems().clear();
                chooser.getItems().addAll(newData.getRoutineNames());
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
                       System.out.println("Update from user: " + newData);
                       setData(getData().withSelection(newData));
                       System.out.println("Done setting data");
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
        
        System.out.println("Displaying things");
        int row = 0;
        for (String rtn : getAllSubroutines(newData)) {
            System.out.println("adding routine " + rtn);
            System.out.println("Found subroutine: " + rtn);
            AutonomousRoutineData rtnData = newData.getRoutine(rtn);
            if (rtnData != null) { 
                row = addHeader(row, "Preferences declared in " + rtn + ":");
                row = addRoutineToUI(row, newData.getRoutine(rtn));
            } else {
                System.out.println("Warning: subroutine " + rtn + " not found");
            }
        }
    }
    
    private List<String> getAllSubroutines(AutonomousInfo info) {
        Deque<String> toSearch = new ArrayDeque<>();
        List<String> result = new ArrayList<>();
        
        if (info.getSelection() != null) {
            toSearch.push(info.getSelection());
        }
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
    
    private int addRoutineToUI(int row, AutonomousRoutineData data) {
        for (String prefName : data.getPreferenceNames()) {
            DataSource<Double> source = getDataSource(data.getName(), prefName);
            addUIEntry(row++, prefName, source);
        }
        return row;
    }
    
    private int addHeader(int rowNum, String text) {
        if (rowNum != 0) {
            Separator sep = new Separator(Orientation.HORIZONTAL);
            sep.setMinHeight(10);
            GridPane.setColumnSpan(sep, 2);
            GridPane.setRowIndex(sep, rowNum++);
            GridPane.setColumnIndex(sep, 0);
            content.getChildren().add(sep);
        }
        
        Label header = new Label(text);
        GridPane.setColumnSpan(header, 2);
        GridPane.setRowIndex(header, rowNum++);
        GridPane.setColumnIndex(header, 0);
        content.getChildren().add(header);
        
        return rowNum;
    }
    
    private void addUIEntry(int rowNum, String prefName, DataSource<Double> source) {
        Spinner<Double> spinner = new Spinner<>(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
        spinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, 0.0));
        spinner.setEditable(true);
        spinner.getValueFactory().valueProperty().bindBidirectional(source.dataProperty());
        GridPane.setRowIndex(spinner, rowNum);
        GridPane.setColumnIndex(spinner, 1);
        content.getChildren().add(spinner);

        Label label = new Label(prefName);
        GridPane.setColumnIndex(label, 0);
        GridPane.setRowIndex(label, rowNum);
        content.getChildren().add(label);
        
        bindings.add(new NTSpinnerBinding(spinner, source));
    }
    
    private DataSource<Double> getDataSource(String routine, String pref) {
        String path = PREFERENCES_LOCATION + SEPERATOR + routine + SEPERATOR + pref;

        if (!NetworkTableUtils.rootTable.containsKey(path)) {
            NetworkTableEntry entry = NetworkTableUtils.rootTable.getEntry(path);
            entry.setDefaultDouble(0.0);
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
}
