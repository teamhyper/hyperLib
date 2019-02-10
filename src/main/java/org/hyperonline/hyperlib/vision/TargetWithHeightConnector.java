package org.hyperonline.hyperlib.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.PIDSource;
import org.hyperonline.hyperlib.pid.DisplacementPIDSource;

/**
 * NetworkTables Connector for an AbstractTargetProcessor of TargetWithHeightResult
 *
 * @author Chris McGroarty
 */
public class TargetWithHeightConnector extends AbstractVisionConnector<TargetWithHeightResult> {

    private final NetworkTableEntry m_height;

    /**
     *
     * @param subTableName name of the subtable to use in the connector
     * @param inst instance of NetworkTables
     */
    public TargetWithHeightConnector(String subTableName, NetworkTableInstance inst) {
        super(subTableName, inst);
        m_height = m_table.getEntry("height");
    }

    /**
     * retrieve a TargetWithHeightResult from NetworkTable Entries
     *
     * @return {TargetWithHeightResult}
     */
    @Override
    protected TargetWithHeightResult retrieve() {
        return new TargetWithHeightResult(super.retrieve(), m_height.getDouble(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish() {
        super.publish();
        m_height.setDouble(m_lastResult.height());
    }

    /**
     * Get a PID source that returns the height of the target.
     *
     * @return A PID source returning the height of the target.
     */
    public PIDSource heightPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().height();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TargetWithHeightResult getDefaultValue() {
        return new TargetWithHeightResult(0, 0, 0, 0, 0, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void next(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags) {
        m_lastResult = retrieve();
    }
}
