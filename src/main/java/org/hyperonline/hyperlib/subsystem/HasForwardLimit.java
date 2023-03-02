package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;

public interface HasForwardLimit {
    boolean isAtForwardLimit();
    boolean canMoveForward(DoubleSupplier speed);
    boolean canMove(DoubleSupplier speed);
    Command autoResetSensorAtForwardLimit();
}
