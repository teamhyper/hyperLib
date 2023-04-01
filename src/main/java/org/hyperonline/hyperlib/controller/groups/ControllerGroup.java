package org.hyperonline.hyperlib.controller.groups;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.hyperonline.hyperlib.controller.MetaController;
import org.hyperonline.hyperlib.controller.SendableMotorController;

/**
 * Base class that facilitates {@link CANSparkMaxGroup} and {@link PhoenixControllerGroup} with common functionality
 * (delegating methods to the two controllers involved).
 * Intentionally package private - external users don't need to see this.
 * @param <M> master motor type
 * @param <S> slave motor type
 *
 * @author Dheeraj Prakash
 */
abstract class ControllerGroup<M extends SendableMotorController, S extends SendableMotorController> implements MetaController<M> {
    protected final M master;
    protected final S slave;

    public ControllerGroup(M master, S slave) {
        this.master = master;
        this.slave = slave;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        master.setNeutralMode(mode);
        slave.setNeutralMode(mode);
    }

    @Override
    public void resetMotorConfig() {
        master.resetMotorConfig();
        slave.resetMotorConfig();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        master.initSendable(builder);
        slave.initSendable(builder);
    }

    @Override
    public void set(double speed) {
        master.set(speed);
    }

    @Override
    public double get() {
        return master.get();
    }

    @Override
    public M getController() {
        return master;
    }

    @Override
    public void setInverted(boolean isInverted) {
        throw new UnsupportedOperationException("Cannot invert " + getClass().getSimpleName() + ". Invert individual controllers instead.");
    }

    @Override
    public boolean getInverted() {
        return false;
    }

    @Override
    public void disable() {
        master.disable();
        slave.disable();
    }

    @Override
    public void stopMotor() {
        master.stopMotor();
        slave.stopMotor();
    }

    public M getMaster() {
        return master;
    }

    public S getSlave() {
        return slave;
    }
}
