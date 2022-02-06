package org.hyperonline.hyperlib.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wpi.first.cscore.CvSource;
import org.hyperonline.hyperlib.pref.DoublePreference;
import org.hyperonline.hyperlib.pref.PreferencesSet;
import org.hyperonline.hyperlib.pref.ScalarPreference;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * A pipeline which finds rectangles within a given color range.
 * 
 * Note that most customization is not done in code, but is instead done in
 * preferences. If no customization is done, reasonable defaults for finding
 * green rectangles are assumed.
 * 
 * @author James Hagborg
 *
 */
public class FindTargetsPipeline implements VisionGUIPipeline {

    private final TargetProcessor m_targetProcessor;
    private final String m_name;

    /*
     * Preferences
     */
    private final PreferencesSet m_prefs;
    private final ScalarPreference m_lowerBound;
    private final ScalarPreference m_upperBound;
    private final DoublePreference m_minArea;

    /**
     * Construct a new pipeline with the given name and target processor.
     * 
     * @param name
     *            The name used to define the preferences set associated to this
     *            pipeline.
     * @param processor
     *            Interface specifying how to extract the result from a list of
     *            rectangles.
     */
    public FindTargetsPipeline(String name, TargetProcessor processor) {
        m_name = Objects.requireNonNull(name);
        m_targetProcessor = Objects.requireNonNull(processor);

        m_prefs = new PreferencesSet(name);
        m_lowerBound = m_prefs.addScalar("LowerBound", "HSV", 30, 200, 100);
        m_upperBound = m_prefs.addScalar("UpperBound", "HSV", 80, 255, 255);
        m_minArea = m_prefs.addDouble("MinArea", 20);
    }

    /*
     * Intermediate steps used in processing. There's no need to store them
     * between frames, but we do so just to avoid reallocating them each time.
     */
    private Mat m_hsv = new Mat();
    private Mat m_filtered = new Mat();
    private Mat m_labels = new Mat();
    private Mat m_stats = new Mat();
    private Mat m_centroids = new Mat();
    private List<Rect> m_targets = new ArrayList<>();

    /*
     * Debug camera source to make tweaking preferences easier This isn't
     * initialized until the first time process is called. That way I can set
     * the dimensions to be the same as the incoming image automatically.
     */
    private CvSource m_debugSource = null;
    
    private void putDebugImage(Mat mat) {
        if (m_debugSource == null) {
            m_debugSource = CameraServer.putVideo(m_name + " debug stream", mat.width(), mat.height());
        }
        m_debugSource.putFrame(mat);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Mat mat) {
        // Move to HSV color space
        Imgproc.cvtColor(mat, m_hsv, Imgproc.COLOR_BGR2HSV);
        // Check which pixels are in the given range
        Core.inRange(m_hsv, m_lowerBound.get(), m_upperBound.get(), m_filtered);
        // Show the filtered image to the debug stream
        putDebugImage(m_filtered);
        
        // TODO: Add an optional erode/dilate step here
        Imgproc.connectedComponentsWithStats(m_filtered, m_labels, m_stats, m_centroids);

        // Extract all the useful info from connected components
        final int numContours = m_stats.rows();
        m_targets.clear();
        for (int i = 1; i < numContours; i++) {
            final int x = (int) m_stats.get(i, Imgproc.CC_STAT_LEFT)[0];
            final int y = (int) m_stats.get(i, Imgproc.CC_STAT_TOP)[0];
            final int width = (int) m_stats.get(i, Imgproc.CC_STAT_WIDTH)[0];
            final int height = (int) m_stats.get(i, Imgproc.CC_STAT_HEIGHT)[0];
            final int area = (int) m_stats.get(i, Imgproc.CC_STAT_AREA)[0];

            /*
             * TODO: Add interface for more general filter criteria, such as
             * percentage of bounding box filled
             */
            if (area >= m_minArea.get()) {
                final Rect rect = new Rect(x, y, width, height);
                m_targets.add(rect);
            }
        }
        
        // Let the target processor do its thing
        m_targetProcessor.process(m_targets);
    }

    /*
     * Constants for drawing indicators. These could be made into preferences,
     * but do we really care that much?
     */
    private static final Scalar RECTANGLE_COLOR = new Scalar(0, 100, 255);

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        for (Rect rect : m_targets) {
            Imgproc.rectangle(mat, rect.tl(), rect.br(), RECTANGLE_COLOR);
        }

        m_targetProcessor.writeOutput(mat);
    }
}
