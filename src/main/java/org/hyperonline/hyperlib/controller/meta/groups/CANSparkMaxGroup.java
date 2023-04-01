package org.hyperonline.hyperlib.controller.meta.groups;

import org.hyperonline.hyperlib.controller.HYPER_CANSparkMax;
import org.hyperonline.hyperlib.controller.meta.MetaController;

/**
 * Class that groups together two {@link HYPER_CANSparkMax}es as master and slave.
 * Useful to link controllers together to pass as one elsewhere.
 *
 * @author Dheeraj Prakash
 */
public class CANSparkMaxGroup extends ControllerGroup<HYPER_CANSparkMax, HYPER_CANSparkMax> implements MetaController<HYPER_CANSparkMax> {
    public CANSparkMaxGroup(HYPER_CANSparkMax master, HYPER_CANSparkMax slave, boolean invertSlave) {
        super(master, slave);
        slave.follow(master, invertSlave);
    }
}
