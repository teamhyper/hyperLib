package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * A class which represents arcade drive. Arcade drive uses two values: move and
 * rotate. Move determines forwards-and-backwards motion, while rotate
 * determines rotational motion. This drive mode is useful for writing
 * autonomous methods which attempt to track a target, or hold a gyro angle. It
 * can also be used to drive directly from the joystick.
 * 
 * This class simply wraps
 * {@link DifferentialDrive#arcadeDrive(double, double, boolean)}.
 * 
 * This class is immutable. That means you must construct a new instance each
 * time you want to change the power to the drivetrain.
 * 
 * @author James Hagborg
 *
 */
public class ArcadeDriveParams implements DriveParameters {
    private final double m_move, m_rotate;
    private final boolean m_squareInputs;

    /**
     * Construct a new {@link ArcadeDriveParams}.
     * 
     * @param move
     *            The amount to move forwards or backwards
     * @param rotate
     *            The amount to rotate
     * @param squareInputs
     *            Whether to square the inputs. This is desirable if the input
     *            is coming from a joystick, as it creates a "soft deadzone". If
     *            coming from another source, like a PID controller, this should
     *            be <code>false</code>.
     */
    public ArcadeDriveParams(double move, double rotate, boolean squareInputs) {
        m_move = move;
        m_rotate = rotate;
        m_squareInputs = squareInputs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drive(DifferentialDrive driveTrain, double currentGyro) {
        driveTrain.arcadeDrive(m_move, m_rotate, m_squareInputs);
    }

    /**
     * Get the move parameter
     * 
     * @return the move parameter
     */
    public double move() {
        return m_move;
    }

    /**
     * Get the rotate parameter
     * 
     * @return the rotate parameter
     */
    public double rotate() {
        return m_rotate;
    }

    /**
     * Get the squareInputs parameter
     * 
     * @return the squareInputs parameter
     */
    public boolean squareInputs() {
        return m_squareInputs;
    }
}
