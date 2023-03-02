package org.hyperonline.hyperlib.subsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;

public interface HasReverseLimit {
    boolean canMoveReverse();
    boolean canMoveReverse(DoubleSupplier speed);
    boolean isAtReverseLimit();
    boolean canMove(DoubleSupplier speed);
    Command autoResetSensorAtReverseLimit();
}
