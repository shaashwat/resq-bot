package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.ResQ_Library;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Admin on 11/3/2015.
 */
public class ColorTest extends ResQ_Library {
    ColorSensor sensorRGB;
    boolean calibrated = false;
    int redOffset;
    int blueOffset;
    int greenOffset;

    @Override
    public void init() {
        sensorRGB = hardwareMap.colorSensor.get("color");
    }

    @Override
    public void loop() {
        int red = sensorRGB.red();
        int blue = sensorRGB.blue();
        int green = sensorRGB.green();
        int alpha = sensorRGB.alpha();
        if(this.time >= 4){ //normal operation
            int offsettedRed = red - (redOffset + 200);
            int offsettedBlue = blue - blueOffset;
            int offsettedGreen = green - greenOffset;
            telemetry.addData("blue", offsettedBlue);
            telemetry.addData("red", offsettedRed);
            telemetry.addData("alpha", alpha);
            telemetry.addData("green", offsettedGreen);
            telemetry.addData("assumed", getScaledColor(offsettedRed, offsettedGreen, offsettedBlue));
            telemetry.addData("Status", "Running");
        }
        else if(this.time == 3) {
            telemetry.addData("Status", "Now Calibrating");
            redOffset = red;
            blueOffset = blue;
            greenOffset = green;

        }
        else {
            telemetry.addData("Status", "Please point sensor toward ground.");
        }
    }
}
