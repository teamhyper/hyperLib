package org.hyperonline.autoviewer;

import java.util.Map;
import java.util.function.Function;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

public class AutonomousInfoType extends ComplexDataType<AutonomousInfo> {
    public static final AutonomousInfoType Instance = new AutonomousInfoType();
    
    protected AutonomousInfoType() {
        super("AutonomousInfo", AutonomousInfo.class);
    }

    @Override
    public Function<Map<String, Object>, AutonomousInfo> fromMap() {
        return AutonomousInfo::new;
    }

    @Override
    public AutonomousInfo getDefaultValue() {
        return new AutonomousInfo();
    }

}
