package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Admin on 11/3/2015.
 */
public class NxtGyroTest extends ResQ_Library {

    //Max is a squid
    GyroSensor sensorGyro;
    @Override
    public void init() {
        GyroSensor sensorGyro;
        sensorGyro = hardwareMap.gyroSensor.get("gyro");
        sensorGyro.calibrate();
    }

    @Override
    public void loop() {
        int heading = sensorGyro.getHeading();
        telemetry.addData("Heading", heading);
    }
}
