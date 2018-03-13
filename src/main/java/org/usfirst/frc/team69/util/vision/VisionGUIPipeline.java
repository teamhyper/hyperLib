package org.usfirst.frc.team69.util.vision;

import org.opencv.core.Mat;

import edu.wpi.first.wpilibj.vision.VisionPipeline;

/**
 * A pipeline which has separate methods for processing an input image and
 * writing its overlays to an output image. This allows multiple pipelines to
 * read from the same camera image, and composite all their outputs at the end.
 * 
 * @author James Hagborg
 *
 */
public interface VisionGUIPipeline extends VisionPipeline {
    /**
     * Read an input image. This method should extract any useful information
     * from the input mat, and update any shared variables that the robot thread
     * may access. It should NOT modify the image. Instead, save any information
     * you want to print, and use it in the next call to writeOutput.
     * 
     * @param mat 
     * 			image to process
     */
    void process(Mat mat);

    /**
     * Draw indicators onto the output image.
     * 
     * @param mat
     * 			image to write the indicators on
     */
    void writeOutput(Mat mat);
}
