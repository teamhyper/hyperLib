package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;

public interface HasForwardLimit {
    boolean canMoveForward();
    boolean isAtForwardLimit();
    boolean canMove(DoubleSupplier speed);
    Command autoResetSensorAtForwardLimit();
}
