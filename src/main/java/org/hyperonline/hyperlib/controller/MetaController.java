package org.hyperonline.hyperlib.controller;

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
}
