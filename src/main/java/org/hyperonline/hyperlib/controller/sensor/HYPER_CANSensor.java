package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.MotorFeedbackSensor;
import com.revrobotics.REVLibError;

public interface HYPER_CANSensor extends MotorFeedbackSensor {

    double getPosition();
    double getVelocity();
    REVLibError setPositionConversionFactor(double factor);
    REVLibError setVelocityConversionFactor(double factor);
    double getPositionConversionFactor();
    double getVelocityConversionFactor();
    REVLibError setInverted(boolean inverted);
    boolean getInverted();
}
