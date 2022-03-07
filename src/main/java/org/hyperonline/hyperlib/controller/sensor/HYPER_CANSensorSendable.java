package org.hyperonline.hyperlib.controller.sensor;

/**
 * annoyingly in REVLib the {@link com.revrobotics.SparkMaxRelativeEncoder}, {@link com.revrobotics.SparkMaxAlternateEncoder}, and {@link com.revrobotics.SparkMaxAnalogSensor} do not share a common ancestor,
 * even though all implement getPosition, getVelocity, setPositionConversionFactor, setVelocityConversionFactor, setInverted.
 *
 * this is a work-around to allow the {@link org.hyperonline.hyperlib.pid.SparkMaxPID} to receive any of the 3 sensors types without needing additional PID classes per sensor type
 *
 * @author Chris McGroarty
 */
public interface HYPER_CANSensorSendable {
    HYPER_CANSensor getSensor();
}
