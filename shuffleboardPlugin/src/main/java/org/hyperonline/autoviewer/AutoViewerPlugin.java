package org.hyperonline.autoviewer;

import java.util.List;
import java.util.Map;

import org.hyperonline.autoviewer.widget.AutonomousInfoWidget;
import org.hyperonline.autoviewer.widget.AutonomousRoutineWidget;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.plugin.Requires;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

@Description(group = "org.hyperonline",
    name = "AutoViewer",
    version = "0.1.0", 
    summary = "Select autonomous modes and set preferences"
)
@Requires(group = "edu.wpi.first.shuffleboard", name = "NetworkTables", minVersion = "1.0.0")
public class AutoViewerPlugin extends Plugin {
    @SuppressWarnings("rawtypes")
    @Override
    public List<DataType> getDataTypes() {
        return ImmutableList.of(
                //AutonomousRoutineType.Instance,
                AutonomousInfoType.Instance);
    }
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
                //WidgetType.forAnnotatedWidget(AutonomousRoutineWidget.class),
                WidgetType.forAnnotatedWidget(AutonomousInfoWidget.class));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Map<DataType, ComponentType> getDefaultComponents() {
        return ImmutableMap.<DataType, ComponentType>builder()
                //.put(AutonomousRoutineType.Instance, WidgetType.forAnnotatedWidget(AutonomousRoutineWidget.class))
                .put(AutonomousInfoType.Instance, WidgetType.forAnnotatedWidget(AutonomousInfoWidget.class))
                .build();
    }
}
