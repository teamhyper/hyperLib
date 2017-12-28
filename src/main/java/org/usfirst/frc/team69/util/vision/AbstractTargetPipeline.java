package org.usfirst.frc.team69.util.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.vision.VisionPipeline;

/**
 * Implementation of a WPILib vision pipeline that finds green rectangles.
 * Further processing of the rectangles is done in the abstract method 
 * {@link #processTargets(List, Mat)}.
 * 
 * To use, extend this and implement {@link #processTargets(List, Mat)}.  
 * If you have any extra result data, make a new class extending VisionResult 
 * to store it.  An object of this type will get passed between the vision 
 * thread and the main thread, so make it immutable to be safe.
 * 
 * @author James Hagborg
 *
 * @param <T> The type of the result from the vision thread
 */
public abstract class AbstractTargetPipeline<T extends VisionResult> implements VisionPipeline {

    private Mat m_lastImage;
    private Mat m_filtered = new Mat();
    private Mat m_labels = new Mat();
    private Mat m_stats = new Mat();
    private Mat m_centroids = new Mat();
    
    private final ArrayList<Rect> m_targets = new ArrayList<>();
    
    private static final Scalar LOWER_BOUND = new Scalar(63, 147, 100);
    private static final Scalar UPPER_BOUND = new Scalar(87, 255, 255);
    private static final int MIN_AREA = 20;
    
    public static final Scalar CROSSHAIRS_COLOR = new Scalar(255, 255, 255);
    public static final Scalar CONTOUR_COLOR = new Scalar(255, 0, 0);
    public static final Scalar RECTANGLE_COLOR = new Scalar(0, 255, 0);
    public static final Scalar CENTER_COLOR = new Scalar(0, 0, 255);
    
    
    private volatile int m_crossX = 10, m_crossY = 10;
    /**
     * The result of the pipeline, which is shared across threads.  Access to
     * this variable is atomic.  Since it is immutable, there should be no
     * risk of race conditions.
     */
    private volatile T m_lastResult;
        
    protected abstract T processTargets(List<Rect> targets, Mat output);
    
    
    /**
     * Construct a new pipeline.
     * 
     * @param initialResult The default value of the result, which will be
     * read if no image has been processed yet.
     */
    public AbstractTargetPipeline(T initialResult) {
        if (initialResult == null) {
            throw new NullPointerException("initialResult == null!");
        }
        m_lastResult = initialResult;
    }
    
    @Override
    public void process(Mat image) {
        m_lastImage = image;
        
        findTargets();
        
        T result = processTargets(m_targets, image);
        if (result == null) {
            throw new NullPointerException("processTargets returned null!");
        }
        
        m_lastResult = result;
        
        
        drawCrosshairs(image);
    }
    
    private void findTargets() {
        Imgproc.cvtColor(m_lastImage, m_filtered, Imgproc.COLOR_BGR2HSV);
        Core.inRange(m_filtered, LOWER_BOUND, UPPER_BOUND, m_filtered);
        Imgproc.connectedComponentsWithStats(m_filtered, m_labels, m_stats, m_centroids);
        
        final int numContours = m_stats.rows();
        m_targets.clear();
        for (int i = 1; i < numContours; i++) {
            final int x = (int) m_stats.get(i, Imgproc.CC_STAT_LEFT)[0];
            final int y = (int) m_stats.get(i, Imgproc.CC_STAT_TOP)[0];
            final int width = (int) m_stats.get(i, Imgproc.CC_STAT_WIDTH)[0];
            final int height = (int) m_stats.get(i, Imgproc.CC_STAT_HEIGHT)[0];
            final int area = (int) m_stats.get(i, Imgproc.CC_STAT_AREA)[0];
            
            if (area >= MIN_AREA) {
                final Rect rect = new Rect(x, y, width, height);
                Imgproc.rectangle(m_lastImage, rect.tl(), rect.br(), RECTANGLE_COLOR);
                m_targets.add(rect);
            }
        }
    }
    
    private void drawCrosshairs(Mat image) {
        int x = getCrosshairsX();
        int y = getCrosshairsY();
        Imgproc.line(image, new Point(0, y), new Point(image.width(), y), CROSSHAIRS_COLOR);
        Imgproc.line(image, new Point(x, 0), new Point(x, image.height()), CROSSHAIRS_COLOR);
    }
    
    /**
     * Get the last result calculated by the vision pipeline.  It is safe to
     * call this from any thread.
     * 
     * @return A VisionResult object containing the results pipeline
     */
    public T getLastResult() {
        return m_lastResult;
    }
    
    /**
     * Get the image produced by the pipeline (with overlays)
     * This should only be called from the vision thread!
     * 
     * @return An openCV Mat of the rendered image
     */
    public Mat getImage() {
        return m_lastImage;
    }
    
    protected double xPIDGet() {
        return getLastResult().xError();
    }
    
    protected double yPIDGet() {
        return getLastResult().yError();
    }
    
    protected int getXError(Rect target) {
        return getCrosshairsX() - target.x - target.width / 2;
    }
    
    protected int getYError(Rect target) {
        return getCrosshairsY() - target.y - target.height / 2;
    }
    
    public void setCrosshairsX(int x) {
        m_crossX = x;
    }
    
    public void setCrosshairsY(int y) {
        m_crossY = y;
    }
    
    public int getCrosshairsX() {
        return m_crossX;
    }
    
    public int getCrosshairsY() {
        return m_crossY;
    }
}
