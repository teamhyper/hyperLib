package org.hyperonline.hyperlib.controller.meta;

import org.hyperonline.hyperlib.controller.SendableMotorController;

/**
 * Interface to represent a raw (not {@link MetaController}) controller.
 * Intentionally has no methods, just serves as an endpoint in the MetaController composition chain.
 */
public interface RawController extends SendableMotorController {}
