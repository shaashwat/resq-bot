package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by Admin on 11/3/2015.
 */
public class UltraTest extends OpMode {
    AnalogInput sensorUltra;

    @Override
    public void init() {
        sensorUltra = hardwareMap.analogInput.get("sonic_1");
    }

    @Override
    public void loop() {
        telemetry.addData("Distance", sensorUltra.getValue());
    }
}
