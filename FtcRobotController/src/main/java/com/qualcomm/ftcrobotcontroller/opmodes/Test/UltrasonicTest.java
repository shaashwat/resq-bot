package com.qualcomm.ftcrobotcontroller.opmodes.Test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;

/**
 * Created by William on 10/29/2015.
 */
public class UltrasonicTest extends ResQ_Library {
    @Override
    public void init() {
        mapSensor();
    }

    @Override
    public void loop() {
        telemetry.addData("dist", getDistance());
    }
}
