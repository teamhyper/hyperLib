package org.usfirst.frc.team69.util.vision;

import org.usfirst.frc.team69.util.DisplacementOnlyPIDSource;
import org.usfirst.frc.team69.util.pref.IntPreference;
import org.usfirst.frc.team69.util.pref.PreferencesSet;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * A vision module consists of all the resources needed to manage a single 
 * camera, pipeline, and video stream.
 * 
 * @author James Hagborg
 *
 * @param <T> The type of the result from the vision pipeline
 * 
 * @see AbstractTargetPipeline
 */
public class VisionModule<T extends VisionResult> {
    private final UsbCamera m_cam;
    private final VisionThread m_thread;
    private final AbstractTargetPipeline<T> m_pipeline;
    private final CvSource m_cvSource;
    private final MjpegServer m_server;
    private final MjpegServer m_rawServer;
    private final boolean m_enabled;
    
    private final PreferencesSet m_prefs;
    private final IntPreference m_exposurePref;
    private final IntPreference m_exposureVisiblePref;
    private final IntPreference m_xCrossPref;
    private final IntPreference m_yCrossPref;
    
    /**
     * Construct a new vision module with the given properties.
     * 
     * @param name A string identifier for the module (e.g. "Shooter")
     * @param pipeline The pipeline to use to process images.
     * @param cameraPort The ID number for the camera.
     * @param outPort The port number for the outgoing mjpeg stream (with overlays from the pipeline)
     * @param rawPort The port number for the outgoing mjpeg stream (without overlays from the pipeline)
     * @param enabled If true, use the pipeline.  If false, send the camera directly to the stream, with no pipeline.
     */
    public VisionModule(String name, AbstractTargetPipeline<T> pipeline,
            int cameraPort, int outPort, int rawPort, boolean enabled) {
        if (pipeline == null) {
            throw new NullPointerException("pipeline == null");
        } else if (name == null) {
            throw new NullPointerException("name == null");
        }
        
        m_enabled = enabled;
        
        m_cam = new UsbCamera(name + " camera", cameraPort);
        VideoMode vmode = m_cam.getVideoMode();
        m_pipeline = pipeline;
        
        m_prefs = new PreferencesSet("Vision " + name, this::onPreferencesUpdated);
        m_exposurePref = m_prefs.addInt("Exposure", 30);
        m_exposureVisiblePref = m_prefs.addInt("Exposure Visible", 80);
        m_xCrossPref = m_prefs.addInt("X Crosshairs", 50);
        m_yCrossPref = m_prefs.addInt("Y Crosshairs", 50);
        onPreferencesUpdated();
        
        if (enabled) {
            m_cvSource = new CvSource(name + " cvsource", vmode);
            m_thread = new VisionThread(m_cam, m_pipeline, this::copyOutputImage);
                        
            m_server = new MjpegServer(name + " server", outPort);
            m_server.setSource(m_cvSource);
            m_rawServer = new MjpegServer(name + " raw server", rawPort);
            m_rawServer.setSource(m_cam);
            
            m_thread.start();
        } else {
            m_server = new MjpegServer(name + " server", outPort);
            m_server.setSource(m_cam);
            m_rawServer = new MjpegServer(name + " raw server", rawPort);
            m_rawServer.setSource(m_cam);
            
            m_cvSource = null;
            m_thread = null;
            
            m_cam.setExposureAuto();
        }
    }
    
    private void onPreferencesUpdated() {
        if (m_enabled) {
            m_cam.setExposureManual(m_exposurePref.get());
            m_pipeline.setCrosshairsX(m_xCrossPref.get());
            m_pipeline.setCrosshairsY(m_yCrossPref.get());
        }
    }
    
    /**
     * Configure camera exposure to be used for human vision.
     */
    public void setVisibleExposure() {
        m_cam.setExposureManual(m_exposureVisiblePref.get());
    }
    
    /**
     * Configure camera exposure to be used for tracking.
     */
    public void setTrackingExposure() {
        m_cam.setExposureManual(m_exposurePref.get());
    }
    
    private void copyOutputImage(AbstractTargetPipeline<T> pipeline) {
        m_cvSource.putFrame(pipeline.getImage());
    }
    
    /**
     * Get the last result from the pipeline.
     * @return The last result from the pipeline.
     */
    public T getLastResult() {
        return m_pipeline.getLastResult();
    }
    
    /**
     * Get a PID source for the difference between the x crosshairs and the target
     * @return A PID source
     */
    public PIDSource xPID() {
        return new DisplacementOnlyPIDSource() {
            @Override public double pidGet() {
                return m_pipeline.xPIDGet();
            }
        };
    }
    
    /**
     * Get a PID source for the difference between the y crosshairs and the target
     * @return A PID source
     */
    public PIDSource yPID() {
        return new DisplacementOnlyPIDSource() {
            @Override public double pidGet() {
                return m_pipeline.yPIDGet();
            }
        };
    }
}
