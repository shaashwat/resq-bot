package com.qualcomm.ftcrobotcontroller.opmodes;

import android.database.CrossProcessCursor;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aman on 10/11/2015.
 */
public class ResQ_TeleOp extends ResQ_Library {

    @Override
    public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //Sensors
        sensorUltra_1 = hardwareMap.analogInput.get("sonic_1");

        //Driving Mapping
        motorLeftFront = hardwareMap.dcMotor.get("motor_1");
        motorLeftMid = hardwareMap.dcMotor.get("motor_2");
        motorLeftBack = hardwareMap.dcMotor.get("motor_3");
        motorRightFront = hardwareMap.dcMotor.get("motor_4");
        motorRightMid = hardwareMap.dcMotor.get("motor_5");
        motorRightBack = hardwareMap.dcMotor.get("motor_6");

        //Other Mapping
        //motorHangingMech = hardwareMap.dcMotor.get("motor_7");
        srvoHang_1 = hardwareMap.servo.get("servo_1");
        srvoHang_2 = hardwareMap.servo.get("servo_2");
        srvoDong_Left = hardwareMap.servo.get("servo_3"); //The left servo
        srvoDong_Right = hardwareMap.servo.get("servo_4"); //The right servo
        srvoPushButton = hardwareMap.servo.get("servo_5");
        srvoScoreClimbers = hardwareMap.servo.get("servo_6");


        //set the direction of the motors
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftMid.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        //set the direction of the servos
        srvoDong_Left.setDirection(Servo.Direction.FORWARD);
        srvoDong_Right.setDirection(Servo.Direction.FORWARD);
        srvoHang_1.setDirection(Servo.Direction.FORWARD);
        srvoHang_1.setDirection(Servo.Direction.FORWARD);
    }


    @Override
    public void loop() {

        float right = ProcessDriveInput(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessDriveInput(-gamepad1.left_stick_y);
        //float x = ProcessDriveInput(-gamepad1.left_stick_x); //Used with wheels
        //float y = ProcessDriveInput(-gamepad1.left_stick_y);
		/*
		 * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 * Alternatively, with wheels, one joystick moves both sets of wheels
		 */

        //****************DRIVING****************//

        drive(left, right); //Used with tracks
        //singleStickDrive(x, y); //Used with wheels

        //****************BLOCK MANIPULATION****************//


        //****************OTHER****************//

        //Donglers
        //Left Dongler System
        //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        //README: I am using gamepad2.right_trigger/bumper for the hanging code please re-map the dongler controls!
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        /*if (gamepad2.right_trigger >= 0.5f) { //lower right trigger means Left Dongler
            if (leftDongDown) { //its already down, so lets move it back up
                srvoDong_Left.setPosition(0.0);
                leftDongDown = false;
            } else { //leftDongDown is false, so make it true and move it down
                srvoDong_Left.setPosition(0.5);
                leftDongDown = true;
            }
        }
        //Right Dongler System
        if (gamepad2.right_bumper) { //upper right button/bumper means Left Dongler
            if (rightDongDown) { //its already down, so lets move it back up
                srvoDong_Right.setPosition(0.0);
                rightDongDown = false;
            } else { //rightDongDown is false, so make it true and move it down
                srvoDong_Right.setPosition(0.5);
                rightDongDown = true;
            }
        }*/

        if(gamepad2.right_trigger >= 0.1f) {
            srvoHang_1.setPosition(0.5f * (srvoHang_1.getPosition() + gamepad2.right_trigger));
        } else if(srvoHang_1.getPosition() >= 0.1) {
            srvoHang_1.setPosition(0);
        }

        if(gamepad2.left_trigger >= 0.1f) {
            srvoHang_2.setPosition(0.5f * (srvoHang_2.getPosition() + gamepad2.right_trigger));
        } else if(gamepad2.left_bumper) {
            srvoHang_2.setPosition(0);
        }


        //Hanging
        if (gamepad2.dpad_down) { //The top button is pressed, release tension
            hangMotor(-1.0f); //turn backward, release tension
        } else if (gamepad2.dpad_up) { //The lower button is pressed, tighten winch
            hangMotor(1.0f);//turn forward, tighten winch
        }

        //****************TELEMETRY****************//

        String tel_Bool_Reverse = (driveReverse)?"REVERSED":"normal";
        String tel_Bool_Slow = (driveSlow)?"SLOW":"fast";
        String tel_Bool_LeftDong = (leftDongDown)?" is down, now moving up":"is up, now moving down";
        String tel_Bool_RightDong = (rightDongDown)?" is down, now moving up":"is up, now moving down";

        telemetry.addData("Distance", sensorUltra_1.getValue());
        telemetry.addData("*****", "Important Booleans");
        telemetry.addData("", "Driving is " + tel_Bool_Reverse + " and " + tel_Bool_Slow);
        telemetry.addData("Left Dongler", "Driving is " + tel_Bool_LeftDong);
        telemetry.addData("Right Dongler", "Driving is " + tel_Bool_RightDong);


    }

    @Override
    public void stop() {

    }
}
