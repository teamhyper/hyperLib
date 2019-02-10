package org.hyperonline.hyperlib;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * HYPERVision provide code which is mostly the same in every vision application.
 *
 * @author Chris McGroarty
 */
public abstract class HYPERVision {

    protected CameraServer m_cameraServer;
    protected NetworkTableInstance m_tableInstance;

    // override this property to true in a child class that runs on the robot
    protected boolean m_isRobot = false;

    /**
     * Initialize the HYPERVision application with components initialized in a specific order
     */
    public final void visionInit() {
        initNetworkTable();
        initCameraServer();
        initCameras();
        initConnectors();
        initProcessors();
        initPipelines();
        initModules();
    }

    /**
     * Method runs throughout the application, allowing for
     * the scheduling of runnable tasks in the PeriodicScheduler
     */
    private void visionPeriodic() {
        PeriodicScheduler.getInstance().run();
    }

    /**
     * Run the Vision application
     * Matches startCompetition of a Robot
     */
    public void startCompetition() {
        visionInit();
        startModules();

        // only run periodic when not running on the robot
        // robot has its own periodics
        while (!m_isRobot) {
            visionPeriodic();
        }
    }

    /**
     * Initialize the NetworkTableInstance and start our team's client
     */
    protected void initNetworkTable() {
        m_tableInstance = NetworkTableInstance.getDefault();
        m_tableInstance.startClientTeam(69);
    }

    /**
     * Initialize the cameras used in this HYPERVision
     */
    protected abstract void initCameras();

    /**
     * Initialize the VisionConnectors used in this HYPERVision
     */
    protected abstract void initConnectors();

    /**
     * Initialize the TargetProcessors used in this HYPERVision
     */
    protected abstract void initProcessors();
    /**
     * Initialize the VIsionGUIPipelines used in this HYPERVision
     */
    protected abstract void initPipelines();
    /**
     * Initialize the VisionModules used in this HYPERVision
     */
    protected abstract void initModules();
    /**
     * Start the VisionModules used in this HYPERVision
     */
    protected abstract void startModules();

    /**
     * Initialize the CameraServer used in this HYPERVision
     */
    protected void initCameraServer() {
        m_cameraServer = CameraServer.getInstance();
    }

    /**
     * Update properties when Preferences have changed
     */
    protected abstract void onPreferencesUpdated();
}

