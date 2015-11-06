package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Admin on 11/3/2015.
 */
public class JacobTest extends OpMode {
    UltrasonicSensor sensorUltra;

    @Override
    public void init() {
        sensorUltra = hardwareMap.ultrasonicSensor.get("sonic_1");
    }

    @Override
    public void loop() {
        telemetry.addData("Distance", sensorUltra.getUltrasonicLevel());
    }
}
