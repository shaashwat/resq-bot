package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 11/3/2015.
 */
public class ColorTest extends ResQ_Library {
    ColorSensor sensorRGB;

    @Override
    public void init() {
        sensorRGB = hardwareMap.colorSensor.get("color");
    }

    @Override
    public void loop() {
        int red = sensorRGB.red();
        int blue = sensorRGB.blue();
        int green = sensorRGB.green();
        telemetry.addData("blue", blue);
        telemetry.addData("red", red);
        telemetry.addData("assumed", getScaledColor(red, blue, green));
    }
}
