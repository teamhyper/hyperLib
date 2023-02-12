package org.hyperonline.hyperlib.controller;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class HYPER_PWMSparkMax extends PWMSparkMax implements SendableMotorController {
    /**
     * Common initialization code called by all constructors.
     *
     * @param channel The PWM channel number. 0-9 are on-board, 10-19 are on the MXP port
     */
    public HYPER_PWMSparkMax(int channel) {
        super(channel);
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetMotorConfig() {
        throw new UnsupportedOperationException();
    }
}
