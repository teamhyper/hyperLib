package org.hyperonline.hyperlib.controller.groups;

import com.ctre.phoenix.motorcontrol.IMotorController;
import org.hyperonline.hyperlib.controller.MetaController;
import org.hyperonline.hyperlib.controller.SendableMotorController;

/**
 * Class that groups together two Phoenix speed controllers (TalonSRX/VictorSPX) as master and slave.
 * Useful to link controllers together to pass as one elsewhere.
 *
 * @param <M> type of master controller used
 * @param <S> type of slave controller used
 *
 * @author Dheeraj Prakash
 */
public class PhoenixControllerGroup<
        M extends SendableMotorController & IMotorController,
        S extends SendableMotorController & IMotorController
        > extends ControllerGroup<M, S> implements MetaController<M> {
    public PhoenixControllerGroup(M master, S slave, boolean invertSlave) {
        super(master, slave);
        slave.follow(master);
        slave.setInverted(invertSlave);
    }
}
