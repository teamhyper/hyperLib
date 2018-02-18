package org.hyperonline.autoviewer;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class AutonomousRoutineType extends ComplexDataType<AutonomousRoutineData> {

    public static final AutonomousRoutineType Instance = new AutonomousRoutineType();
    
    protected AutonomousRoutineType() {
        super("AutonomousRoutine", AutonomousRoutineData.class);
    }

    @Override
    public Function<Map<String, Object>, AutonomousRoutineData> fromMap() {
        return AutonomousRoutineData::new;
    }

    @Override
    public AutonomousRoutineData getDefaultValue() {
        return new AutonomousRoutineData();
    }

}
