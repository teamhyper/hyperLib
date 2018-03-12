package org.usfirst.frc.team69.util.auto.desktoptester;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team69.util.pref.IntPreference;
import org.usfirst.frc.team69.util.pref.PreferencesSet;
import org.usfirst.frc.team69.util.vision.ClosestTargetProcessor;
import org.usfirst.frc.team69.util.vision.CrosshairsPipeline;
import org.usfirst.frc.team69.util.vision.FindTargetsPipeline;
import org.usfirst.frc.team69.util.vision.VisionModule;

import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;

public class VisionSystem {
    
    CvSource m_source;
    Mat m_image;
    Thread m_feederThread;
    
    void imageFeederThread() {
        double fps = m_source.getVideoMode().fps;
        long timeStep = (long) (1e9 / fps);
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (true) {
            System.out.println("Putting frame");
            m_source.putFrame(m_image);
            
            // wait a bit
            while ((currentTime = System.nanoTime()) < lastTime + timeStep) {
                try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            lastTime = currentTime;
        }
        
    }
    
    private VisionModule m_module;
    private FindTargetsPipeline m_pipeline;
    private ClosestTargetProcessor m_processor;
    private CrosshairsPipeline m_crosshairs;
    
    private PreferencesSet m_prefs = new PreferencesSet("Vision");
    private IntPreference m_xCross = m_prefs.addInt("Crosshairs X", 200);
    private IntPreference m_yCross = m_prefs.addInt("Crosshairs Y", 200);
    

    public VisionSystem() {
        CameraServerJNI.ForceLoad();
        System.out.println(Core.getBuildInformation());
        
        // Set up dummy source to feed images
        m_image = Imgcodecs.imread("/home/james/Robotics/RealFullField/6.jpg");
        if (m_image.empty()) {
            System.out.println("Could not load test image!  Vision will not work!");
            return;
        }
        m_source = CameraServer.getInstance().putVideo("Dummy source of file", m_image.width(), m_image.height());
        m_feederThread = new Thread(this::imageFeederThread);
        m_feederThread.setName("Image feeder thread");
        m_feederThread.setDaemon(true);
        m_feederThread.start();
        
        m_processor = new ClosestTargetProcessor(m_xCross::get, m_yCross::get);
        m_pipeline = new FindTargetsPipeline("My Pipeline", m_processor);
        m_crosshairs = new CrosshairsPipeline(m_xCross::get, m_yCross::get, 100, 100, 100);
        
        m_module = new VisionModule.Builder(m_source)
                .addPipeline(m_pipeline)
                .addPipeline(m_crosshairs)
                .build();
        
        m_module.start();
    }

}
