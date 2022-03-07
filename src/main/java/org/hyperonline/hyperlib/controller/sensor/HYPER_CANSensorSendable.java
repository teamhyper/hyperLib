package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.REVLibError;
import edu.wpi.first.util.sendable.Sendable;

/**
 * annoyingly in REVLib the {@link com.revrobotics.SparkMaxRelativeEncoder}, {@link com.revrobotics.SparkMaxAlternateEncoder}, and {@link com.revrobotics.SparkMaxAnalogSensor} do not share a common ancestor,
 * even though all implement getPosition, getVelocity, setPositionConversionFactor, setVelocityConversionFactor, setInverted.
 *
 * this is a work-around to allow the {@link org.hyperonline.hyperlib.pid.SparkMaxPID} to receive any of the 3 sensors types without needing additional PID classes per sensor type
 *
 * @author Chris McGroarty
 */
public interface HYPER_CANSensorSendable extends Sendable {
    MotorFeedbackSensor getSensor();

    double getPosition();
    double getVelocity();

    REVLibError setPositionConversionFactor(double factor);
    REVLibError setVelocityConversionFactor(double factor);
    double getPositionConversionFactor();
    double getVelocityConversionFactor();
    REVLibError setInverted(boolean inverted);
    boolean getInverted();
}
