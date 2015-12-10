package com.qualcomm.ftcrobotcontroller.opmodes;

import android.database.CrossProcessCursor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 10/11/2015.
 */
public class GirlTeleop extends ResQ_Library {

    double srvoHang1Position;
    double srvoHang2Position;

    @Override
    public void init() {
        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //Do the map thing
        initializeMapping();

        //srvoHang1Position = srvoHang_1.getPosition();
        //srvoHang2Position = srvoHang_2.getPosition();
        //srvoHang_1.setPosition(1.0);
    }


    @Override
    public void loop() {

        float right = ProcessToMotorFromJoy(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessToMotorFromJoy(-gamepad1.left_stick_y);
        /*
         * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        drive(left, right); //Used with tracks

        //Drive modifications
        if (gamepad1.x) {
            //Track speed 100%
            setDriveGear(3);
        }
        if (gamepad1.y) {
            //Track speed 50%
            setDriveGear(2);
        }
        if (gamepad1.b) {
            //Track speed 25%
            setDriveGear(1);
        }
        if (gamepad1.a) {
            //reverse drive
            driveReverse = !driveReverse;
        }
    }
}