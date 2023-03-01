package org.hyperonline.hyperlib.controller.sensor;

import com.revrobotics.*;
import edu.wpi.first.util.sendable.SendableBuilder;

public record HYPER_SparkMaxAbsoluteEncoder(AbsoluteEncoder encoder) implements HYPER_CANSensorSendable {

    @Override
    public MotorFeedbackSensor getSensor() {
        return encoder;
    }

    @Override
    public double getPosition() {
        return encoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }

    @Override
    public REVLibError setPositionConversionFactor(double factor) {
        return encoder.setPositionConversionFactor(factor);
    }

    @Override
    public REVLibError setVelocityConversionFactor(double factor) {
        return encoder.setVelocityConversionFactor(factor);
    }

    @Override
    public double getPositionConversionFactor() {
        return encoder.getPositionConversionFactor();
    }

    @Override
    public double getVelocityConversionFactor() {
        return encoder.getVelocityConversionFactor();
    }

    @Override
    public REVLibError setInverted(boolean inverted) {
        return encoder.setInverted(inverted);
    }

    @Override
    public boolean getInverted() {
        return encoder.getInverted();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("SparkMaxAbsoluteEncoder");
        builder.addDoubleProperty("Position", this::getPosition, null);
        builder.addDoubleProperty("Velocity", this::getVelocity, null);
    }

    public REVLibError setZeroOffset(double offset) {
        return encoder.setZeroOffset(offset);
    }

    public double getZeroOffset() {
        return encoder.getZeroOffset();
    }

    public REVLibError setAverageDepth(int depth) {
        return encoder.setAverageDepth(depth);
    }

    public int getAverageDepth(){
        return encoder.getAverageDepth();
    }
}
