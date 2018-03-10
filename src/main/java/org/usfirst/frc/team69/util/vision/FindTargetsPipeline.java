package org.usfirst.frc.team69.util.vision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team69.robot.vision.DisplacementPIDSource;
import org.usfirst.frc.team69.robot.vision.VisionResult;
import org.usfirst.frc.team69.util.pref.DoublePreference;
import org.usfirst.frc.team69.util.pref.PreferencesSet;
import org.usfirst.frc.team69.util.pref.ScalarPreference;

import edu.wpi.first.wpilibj.PIDSource;

/**
 * A pipeline which finds rectangles within a given color range.
 * 
 * Note that most customization is not done in code, but is instead done in
 * preferences. If no customization is done, reasonable defaults for finding
 * green rectangles are assumed.
 * 
 * @author James Hagborg
 *
 * @param <T>
 *            The data type of the result.
 */
public class FindTargetsPipeline<T extends VisionResult> implements VisionGUIPipeline {

    private TargetProcessor<T> m_targetProcessor;

    /*
     * The last result found.
     */
    private volatile T m_lastResult;

    /*
     * Preferences
     */
    private PreferencesSet m_prefs;
    private ScalarPreference m_lowerBound;
    private ScalarPreference m_upperBound;
    private DoublePreference m_minArea;

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
    public FindTargetsPipeline(String name, TargetProcessor<T> processor) {
        m_targetProcessor = Objects.requireNonNull(processor);
        m_lastResult = processor.getDefaultValue();

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Mat mat) {
        Imgproc.cvtColor(mat, m_hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(m_hsv, m_lowerBound.get(), m_upperBound.get(), m_filtered);
        // TODO: Add an optional erode/dilate step here
        Imgproc.connectedComponentsWithStats(m_filtered, m_labels, m_stats, m_centroids);

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

        m_lastResult = m_targetProcessor.process(m_targets);
    }

    /*
     * Constants for drawing indicators. These could be made into preferences,
     * but do we really care that much?
     */
    private static final Scalar RECTANGLE_COLOR = new Scalar(255, 0, 0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        for (Rect rect : m_targets) {
            Imgproc.rectangle(mat, rect.tl(), rect.br(), RECTANGLE_COLOR);
        }

        m_targetProcessor.writeOutput(mat, m_lastResult);
    }

    /**
     * Get the most recent output from the pipeline. If no result has been
     * computed yet, returns the default value.
     * 
     * @return The most recent output from the pipeline.
     */
    public T getLastResult() {
        return m_lastResult;
    }

    /**
     * Get a PID source which tracks the x-coordinate of the target. This method
     * is convenient and covers most cases, but will ignore any extra
     * information in your result type.
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
     * Get a PID source which tracks the y-coordinate of the target. This method
     * is convenient and covers most cases, but will ignore any extra
     * information in your result type.
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
