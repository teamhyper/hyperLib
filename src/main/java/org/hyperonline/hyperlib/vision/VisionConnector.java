package org.hyperonline.hyperlib.vision;

/**
 * A Connector that allows for the subscribing of and publishing to NetworkTables
 * @author Chris McGroarty
 */
public interface VisionConnector {
    /**
     * subscribes to a VisionConnector's VisionResult in NetworkTable
     */
    void subscribe();

    /**
     * cancel the subscription/listener to NetworkTable
     */
    void unsubscribe();

    /**
     * publishes a VisionConnector's VisionResult to NetworkTable
     */
    void publish();
}
