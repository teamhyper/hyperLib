package org.hyperonline.hyperlib.vision;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Pipeline that draws an indicator around a target, showing what the goal
 * height should be. This is currently done by drawing two bars above and below
 * the center of the target.
 * 
 * @author James Hagborg
 *
 */
public class HeightIndicatorPipeline implements VisionGUIPipeline {

    private final Supplier<? extends VisionResult> m_targetLocation;
    private final IntSupplier m_height;
    private final Scalar m_color;

    /**
     * Construct a new pipeline with the specified parameters.
     * 
     * @param targetLocation
     *            Supplier of the target location, so this pipeline knows where
     *            to draw the indicator. Usually this is the getLastResult
     *            method of some pipeline which runs before this one.
     * @param height
     *            The goal height of the target. Usually this is a constant or
     *            the get method of a preference.
     * @param b Blue component of color
     * @param g Green component of color
     * @param r Red component of color
     */
    public HeightIndicatorPipeline(
            Supplier<? extends VisionResult> targetLocation, IntSupplier height,
            int b, int g, int r) {
        m_targetLocation = Objects.requireNonNull(targetLocation);
        m_height = Objects.requireNonNull(height);
        m_color = new Scalar(b, g, r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Mat mat) {
        // do nothing
    }

    private static final double MARKER_WIDTH = 4;

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        VisionResult res = m_targetLocation.get();

        if (res.foundTarget()) {
            int height = m_height.getAsInt();
            double x = res.xAbsolute();
            double y = res.yAbsolute();

            Point tl = new Point(x - MARKER_WIDTH / 2.0, y - height / 2.0);
            Point tr = new Point(x + MARKER_WIDTH / 2.0, y - height / 2.0);
            Point bl = new Point(x - MARKER_WIDTH / 2.0, y + height / 2.0);
            Point br = new Point(x + MARKER_WIDTH / 2.0, y + height / 2.0);
            Imgproc.line(mat, tl, tr, m_color);
            Imgproc.line(mat, bl, br, m_color);
        }
    }

}
