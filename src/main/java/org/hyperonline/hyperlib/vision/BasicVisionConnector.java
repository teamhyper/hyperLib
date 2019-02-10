package org.hyperonline.hyperlib.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;

/**
 * NetworkTables Connector for a AbstractTargetProcessor of VisionResult
 *
 * @author Chris McGroarty
 */
public class BasicVisionConnector extends AbstractVisionConnector<VisionResult> {

    /**
     *
     * @param subTableName name of the subtable to use in the connector
     * @param inst instance of NetworkTables
     */
    public BasicVisionConnector(String subTableName, NetworkTableInstance inst) {
        super(subTableName, inst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void next(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags) {
        m_lastResult = retrieve();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VisionResult getDefaultValue() {
        return new VisionResult(0, 0, 0, 0, false);
    }
}
