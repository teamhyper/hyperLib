package org.hyperonline.hyperlib.vision;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * Interface for taking a list of rectangles found, and doing something useful
 * with that information, such as making it available as a PID source.
 * 
 * @author James Hagborg
 */
public interface TargetProcessor {
    /**
     * Extract the result type from a list of rectangles found.
     * 
     * @param targets
     *            The list of targets found.
     */
    void process(List<Rect> targets);

    /**
     * Draw overlays on the image. This should give the user some indication of
     * what the result of {@link #process(List)} was.
     * 
     * @param mat
     *            The image to draw on.
     */
    void writeOutput(Mat mat);
}