package org.hyperonline.hyperlib.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.cscore.VideoSource;
import org.opencv.core.Mat;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * A {@link VisionModule} consists of a camera, thread, output stream, and
 * potentially multiple pipelines. Each pipeline reads from the camera, and
 * writes any overlays onto the output image. Each pipeline is responsible for
 * managing its own result.
 * 
 * @author James Hagborg
 *
 */
public class VisionModule {
    private final CvSource m_source;
    private final CvSink m_sink;
    private final Thread m_thread;
    private final List<VisionGUIPipeline> m_pipelines;

    /**
     * Builder class for a {@link VisionModule}. Configure options here, and
     * then call {@link #build()} to construct a module with the given
     * parameters.
     * 
     * @author James Hagborg
     *
     */
    public static class Builder {
        private final VideoSource m_source;
        private final List<VisionGUIPipeline> m_pipelines;
        private String m_name;

        /**
         * Create a new module builder with the specified video source.
         * 
         * @param source
         *            The {@link VideoSource} to use as input to the pipelines.
         *            Usually this is a USB or Axis camera created using the
         *            {@link CameraServer} class.
         */
        public Builder(VideoSource source) {
            m_name = source.getName();
            m_pipelines = new ArrayList<>();
            m_source = source;
        }

        /**
         * Set the name of the module. This determines the name of the output
         * server and the worker thread. By default, the name of the stream is
         * the same as the name of the video source.
         * 
         * @param name
         *            The new name.
         * @return This builder object.
         */
        public Builder setName(String name) {
            m_name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Add a pipeline to this module. Pipelines are run in the order that
         * they are added. This will determine which UI elements appear on top
         * of which others.
         * 
         * @param pipeline
         *            The pipeline to add.
         * @return This builder object.
         */
        public Builder addPipeline(VisionGUIPipeline pipeline) {
            m_pipelines.add(Objects.requireNonNull(pipeline));
            return this;
        }

        /**
         * Construct a {@link VisionModule} object with the parameters
         * specified.
         * 
         * @return A new stream object.
         */
        public VisionModule build() {
            return new VisionModule(m_name, m_source, m_pipelines);
        }
    }

    /**
     * Private constructor called by the builder
     * 
     * @param name
     * @param source
     * @param pipelines
     */
    private VisionModule(String name, VideoSource source, List<VisionGUIPipeline> pipelines) {
        VideoMode vmode = source.getVideoMode();
        m_sink = CameraServer.getInstance().getVideo(source);
        m_source = CameraServer.getInstance().putVideo(name + " (with overlays)", vmode.width, vmode.height);
        m_pipelines = new ArrayList<>(pipelines);

        m_thread = new Thread(this::visionThread);
        m_thread.setName("Vision thread for " + name);
        m_thread.setDaemon(true);
    }

    /**
     * Start the vision thread running.
     */
    public void start() {
        m_thread.start();
    }

    /**
     * Entry point for the vision thread.
     */
    private void visionThread() {
        final Mat mat = new Mat();
        while (true) {
            if (m_sink.grabFrame(mat) != 0) {
                for (VisionGUIPipeline pipeline : m_pipelines) {
                    pipeline.process(mat);
                }
                for (VisionGUIPipeline pipeline : m_pipelines) {
                    pipeline.writeOutput(mat);
                }
                m_source.putFrame(mat);
            } else {
                DriverStation.reportError("In grabFrame on vision thread: " + m_sink.getError(), false);
            }
        }
    }
}
