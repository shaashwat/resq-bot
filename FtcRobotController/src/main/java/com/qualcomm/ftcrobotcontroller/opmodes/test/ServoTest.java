package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Admin on 11/3/2015.
 */
public class ServoTest extends OpMode {
    Servo srvoHang_1;
    Servo srvoHang_2;

    @Override
    public void init() {
        srvoHang_1 = hardwareMap.servo.get("s1");
        srvoHang_2 = hardwareMap.servo.get("s2");
    }

    @Override
    public void loop() {
        if (gamepad2.right_trigger >= 0.5f) {
            srvoHang_1.setPosition(1.0);
        } else if (srvoHang_1.getPosition() >= 0.1) {
            srvoHang_1.setPosition(0.0);
        }

        if (gamepad2.left_trigger >= 0.5f) {
            srvoHang_2.setPosition(1.0);
        } else if (gamepad2.left_bumper) {
            srvoHang_2.setPosition(0.0);
        }
    }
}
