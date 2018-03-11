package org.usfirst.frc.team69.util.vision;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Interface for taking a list of rectangles found, and putting that information
 * together into the result type.
 * 
 * @author James Hagborg
 *
 * @param <T>
 *            The data type of the result.
 */
public interface TargetProcessor<T extends VisionResult> {
    /**
     * Extract the result type from a list of rectangles found.
     * 
     * @param targets
     *            The list of targets found.
     * @return The extracted result.
     */
    void process(List<Rect> targets);

    /**
     * Draw overlays on the image. This should give the user some indication of
     * what the result of {@link #process(List)} was.
     * 
     * @param mat
     *            The image to draw on.
     * @param lastResult
     *            The most recent return value of {@link #process(List)}.
     */
    void writeOutput(Mat mat);

    /**
     * Return a default value for the result type. This is the result that is
     * returned before the first run of the pipeline.
     * 
     * @return The default value for the retult type.
     */
    T getDefaultValue();
}