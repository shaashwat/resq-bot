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
    boolean teamed = false;
    Team currentTeam;
    int redOffset;
    int blueOffset;
    int greenOffset;

    @Override
    public void init() {
        sensorRGB = hardwareMap.colorSensor.get("color");
    }

    @Override
    public void loop() {
        if(this.time >= 4){ //normal operation
            Team scaledColor = getColor();
            if(scaledColor != Team.UNKNOWN && !teamed) {
                teamed = true;
                currentTeam = scaledColor;
            }
            if(teamed){
                telemetry.addData("Current Team", currentTeam);
            }
            else if(!teamed) {
                telemetry.addData("Current Team", "none");
            }
            //telemetry.addData("green", offsettedGreen);
            telemetry.addData("assumed", scaledColor);

            telemetry.addData("Status", "Running");
        }
        else if(this.time == 3) {
            telemetry.addData("Status", "Now Calibrating");
            calibrateColors();

        }
        else {
            telemetry.addData("Status", "Please point sensor toward ground.");
        }
    }
}
