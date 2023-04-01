package org.hyperonline.hyperlib.controller;

import org.hyperonline.hyperlib.controller.groups.ControllerGroup;

/**
 * Interface that represents a meta-controller as opposed to a raw controller (see HYPER_* classes)
 * @param <T> type of controller this wraps - could also be a MetaController itself
 *
 * @author Dheeraj Prakash
 */
public interface MetaController<T extends SendableMotorController> extends SendableMotorController {
    /**
     * Get the controller that this meta-controller wraps
     * @return the controller this wraps
     */
    T getController();

    /**
     * Travel down the meta-controller chain and obtain the raw controller being wrapped.
     * @return the raw controller.
     *
     * FIXME: make this return not just SendableMotorController
     */
    default SendableMotorController getRawController() {
        SendableMotorController tmp = this.getController();
        while (true) {
            if (tmp instanceof MetaController<?>) tmp = ((MetaController<?>) tmp).getController();
            else if (tmp instanceof RawController) return tmp;
        }
    }

    /**
     * Travel down the meta-controller chain and obtain the {@link ControllerGroup} being used.
     * Useful to get the slave controller instead of master at the end of the chain.
     * @return ControllerGroup
     *
     * FIXME: make this return the correct subclass
     */
    default ControllerGroup<?, ?> getControllerGroup() {
        SendableMotorController tmp = this.getController();
        while (true) {
            if (tmp instanceof ControllerGroup<?,?>) return (ControllerGroup<?, ?>) tmp;
            else if (tmp instanceof MetaController<?>) tmp = ((MetaController<?>) tmp).getController();
            else return null;
        }
    }
}
