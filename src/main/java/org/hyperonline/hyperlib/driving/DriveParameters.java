package org.hyperonline.hyperlib.driving;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * The {@link DriveParameters} interface allows one to use polymorphism to
 * represent the various drive modes of the robot, such as tank drive, arcade
 * drive, meccanum drive (Cartesian or polar), swerve drive, etc. A single
 * instance of {@link DriveParameters} should contain a specific set of values
 * for one of those modes.
 * 
 * Using this interface, one can drive the robot by continuously supplying the
 * drive train with {@link DriveParameters} objects. One can also create new
 * drive modes and switch between modes without having to modify the drivetrain
 * subsystem, or write separate commands for each mode. See the hypercode2016
 * userDrive class for a good example of this.
 * 
 * @author James Hagborg
 *
 */
public interface DriveParameters {
	/**
	 * Drive the robot. This method should not call any "stateful" methods of the
	 * drivetrain (anything that starts with "set") to allow one to switch between
	 * modes easily.
	 * 
	 * TODO: pass a wrapper of DifferentialDrive, which only allows certain methods
	 * TODO: remove currentGyro. This is pretty irrelevant, and can be obtained in
	 * other ways.
	 * 
	 * @param driveTrain  A {@link DifferentialDrive} object representing the
	 *                    drivetrain of the robot.
	 * @param currentGyro The current gyro heading, if a gyro exists. Nothing should
	 *                    use this right now, so just pass in 0.0 if you're not
	 *                    sure.
	 *                    
	 * @throws WrongDriveTypeException
	 */
	void drive(DifferentialDrive driveTrain, double currentGyro) throws WrongDriveTypeException;

	/**
	 * Drive the robot. This method should not call any "stateful" methods of the
	 * drivetrain (anything that starts with "set") to allow one to switch between
	 * modes easily.
	 * 
	 * @param driveTrain  A {@link MecanumDrive} object representing the drivetrain
	 *                    of the robot.
	 * @param currentGyro The current gyro heading, if a gyro exists. Nothing should
	 *                    use this right now, so just pass in 0.0 if you're not
	 *                    sure.
	 *                    
	 * @throws WrongDriveTypeException
	 */
	void drive(MecanumDrive driveTrain, double currentGyro) throws WrongDriveTypeException;
}
