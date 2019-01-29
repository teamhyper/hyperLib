package org.hyperonline.hyperlib.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.PIDSource;
import org.hyperonline.hyperlib.pid.DisplacementPIDSource;

/**
 * NetworkTables Connector for a AbstractTargetProcessor of SkewVisionResult
 *
 * @author Chris McGroarty
 */
public class SkewVisionConnector extends AbstractVisionConnector<SkewVisionResult> {

    private final NetworkTableEntry m_skew;

    /**
     *
     * @param subTableName name of the subtable to use in the connector
     * @param inst instance of NetworkTables
     */
    public SkewVisionConnector(String subTableName, NetworkTableInstance inst) {
        super(subTableName, inst);
        m_skew = m_table.getEntry("skew");
    }

    /**
     * @return A PID source tracking the skew of the target
     */
    public PIDSource skewPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().skew();
            }
        };
    }

    /**
     * retrieve a SkewVisionResult from NetworkTable Entries
     *
     * @return {SkewVisionResult}
     */
    @Override
    protected SkewVisionResult retrieve() {
        return new SkewVisionResult(super.retrieve(), m_skew.getDouble(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish() {
        super.publish();
        m_skew.setDouble(m_lastResult.skew());
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
    public SkewVisionResult getDefaultValue() {
        return new SkewVisionResult(0, 0, 0, 0, 0, false);
    }
}
