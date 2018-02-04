package org.usfirst.frc.team69.util.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * A class which represents tank drive. This mode takes two inputs: left and
 * right, and drives each side of the robot independently.
 * 
 * This class simply wraps {@link DifferentialDrive#tankDrive(double, double, boolean)}.    
 * 
 * This class is immutable. That means you must construct a new instance each
 * time you want to change the power to the drivetrain.
 * 
 * @author James Hagborg
 *
 */
public class TankDriveParams implements DriveParameters {

    private final double m_left, m_right;
    private final boolean m_squareInputs;

    /**
     * Construct a new {@link TankDriveParams} object.
     * 
     * @param left
     *            The power to the left side of the drivetrain
     * @param right
     *            The power to the right side of the drivetrain
     * @param squareInputs
     *            Whether to square the inputs. This is desirable if the input
     *            is coming from a joystick, as it creates a "soft deadzone". If
     *            coming from another source, like a PID controller, this should
     *            be <code>false</code>.
     */
    public TankDriveParams(double left, double right, boolean squareInputs) {
        m_left = left;
        m_right = right;
        m_squareInputs = squareInputs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drive(DifferentialDrive driveTrain, double currentGyro) {
        driveTrain.tankDrive(m_left, m_right, m_squareInputs);
    }


    /**
     * Get the left parameter
     * 
     * @return the left parameter
     */
    public double left() {
        return m_left;
    }

    /**
     * Get the right parameter
     * 
     * @return the right parameter
     */
    public double right() {
        return m_right;
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
