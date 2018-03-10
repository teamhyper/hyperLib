package org.usfirst.frc.team69.util.vision;

import java.util.Objects;
import java.util.function.IntSupplier;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team69.util.pref.IntPreference;

/**
 * Pipeline which draws crosshairs in a given position.
 * 
 * @author james
 *
 */
public class CrosshairsPipeline implements VisionGUIPipeline {

    private final Scalar m_color;
    private final IntSupplier m_x, m_y;

    /**
     * Construct a new crosshairs pipeline. On each frame, the position of the
     * crosshairs is read from the given suppliers. Usually, this is the method
     * {@link IntPreference#get}.
     * 
     * @param x
     *            Supplier for x coordinate.
     * @param y Supplier for y coordinate.
     * @param b Blue component of color.
     * @param g Green component of color.
     * @param r Red component of color.
     */
    public CrosshairsPipeline(IntSupplier x, IntSupplier y, int b, int g, int r) {
        m_color = new Scalar(b, g, r);
        m_x = Objects.requireNonNull(x);
        m_y = Objects.requireNonNull(y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Mat mat) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(Mat mat) {
        int x = m_x.getAsInt();
        int y = m_y.getAsInt();
        Imgproc.line(mat, new Point(0, y), new Point(mat.width(), y), m_color);
        Imgproc.line(mat, new Point(x, 0), new Point(x, mat.height()), m_color);
    }

}
