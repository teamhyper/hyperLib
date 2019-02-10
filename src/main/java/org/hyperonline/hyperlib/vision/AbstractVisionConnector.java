package org.hyperonline.hyperlib.vision;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.PIDSource;
import org.hyperonline.hyperlib.pid.DisplacementPIDSource;

import java.util.Objects;

/**
 * Base class with common functionality used by VisionConnectors. In particular,
 * this provides methods to store, publish, and subscribe to a result,
 * and access it via PID sources.
 *
 * @param <T> VisionResult type of this AbstractVisionConnector
 * @author Chris McGroarty
 */
public abstract class AbstractVisionConnector<T extends VisionResult> implements VisionConnector {

    protected final NetworkTableEntry m_xError;
    protected final NetworkTableEntry m_yError;
    protected final NetworkTableEntry m_xAbs;
    protected final NetworkTableEntry m_yAbs;
    protected final NetworkTableEntry m_foundTarget;
    protected final NetworkTableEntry m_lastResultTimestamp;
    protected T m_lastResult;
    protected NetworkTable m_table;
    private String m_mainTableName = "hypervision";
    private String m_subTableName;
    private NetworkTableInstance m_inst;
    private int m_listenerID;

    protected AbstractVisionConnector(String subTableName, NetworkTableInstance inst) {
        m_subTableName = Objects.requireNonNull(subTableName);

        m_inst = Objects.requireNonNull(inst);
        m_table = m_inst.getTable(getTableName());
        m_lastResult = getDefaultValue();

        m_xError = m_table.getEntry("xError");
        m_yError = m_table.getEntry("yError");
        m_xAbs = m_table.getEntry("xAbsolute");
        m_yAbs = m_table.getEntry("yAbsolute");
        m_foundTarget = m_table.getEntry("foundTarget");
        m_lastResultTimestamp = m_table.getEntry("lastResultTimestamp");
    }

    private String getTableName() {
        return "/" + m_mainTableName + "/" + m_subTableName;
    }

    /**
     * add listener for the lastResultTimestamp entry in NetworkTables
     * process the next result on an update
     */
    public void subscribe() {
        m_listenerID = m_table.addEntryListener("lastResultTimestamp", this::next, EntryListenerFlags.kUpdate);
    }

    /**
     * remove listener for the lastResultTimestamp entry in NetworkTables
     */
    public void unsubscribe() {
        m_table.removeEntryListener(m_listenerID);
    }

    /**
     * @return {T}
     */
    public abstract T getDefaultValue();

    /**
     * @param result {T}
     */
    public void updateResult(T result) {
        if (m_lastResult == null) {
            m_lastResult = getDefaultValue();
        }
        if (!m_lastResult.equals(result)) {
            m_lastResult = (result == null ? getDefaultValue() : result);
            if (m_inst.isConnected()) {
                this.publish();
            }
        } else {
            System.out.println("NetworkTableInstance(69) not connected");
        }
    }

    /**
     * publish VisionResult values to corresponding NetworkTable Entries
     * should be overriden and supered by any children of type T extends VisionResult
     */
    public void publish() {
        m_lastResultTimestamp.setDouble(System.currentTimeMillis());
        m_xError.setDouble(m_lastResult.xError());
        m_yError.setDouble(m_lastResult.yError());
        m_xAbs.setDouble(m_lastResult.xAbsolute());
        m_yAbs.setDouble(m_lastResult.yAbsolute());
        m_foundTarget.setBoolean(m_lastResult.foundTarget());
    }

    /**
     * retrieve a VisionResult from NetworkTable Entries
     * should be overriden and supered by any children of type T extends VisionResult
     *
     * @return {VisionResult}
     */
    protected VisionResult retrieve() {
        return new VisionResult(
                m_xError.getDouble(0),
                m_yError.getDouble(0),
                m_xAbs.getDouble(0),
                m_yAbs.getDouble(0),
                m_foundTarget.getBoolean(false)
        );
    }

    /**
     * evaluate and process the results in NetworkTables to my lastResult
     *
     * @param table NetworkTable to get values from
     * @param key Key of the entry that triggered the Listener
     * @param entry Entry that was updated
     * @param value the new Value
     * @param flags Listener flags (created, updated, removed, etc)
     */
    protected abstract void next(NetworkTable table, String key, NetworkTableEntry entry,
                                 NetworkTableValue value, int flags);


    /**
     * @return {T}
     */
    public T getLastResult() {
        return m_lastResult;
    }

    /**
     * Get a PID source which tracks the x-coordinate of the target.
     *
     * @return A PID source tracking the x-coordinate of the target.
     */
    public PIDSource xPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().xError();
            }
        };
    }

    /**
     * Get a PID source which tracks the y-coordinate of the target.
     *
     * @return A PID source tracking the y-coordinate of the target.
     */
    public PIDSource yPID() {
        return new DisplacementPIDSource() {
            @Override
            public double pidGet() {
                return getLastResult().yError();
            }
        };
    }
}
