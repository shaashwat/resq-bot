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

<<<<<<< HEAD
        //Do the map thing
        initializeMapping();
=======
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
>>>>>>> origin/master
    }


    @Override
    public void loop() {

        float right = ProcessToMotorFromJoy(-gamepad1.right_stick_y); //Used with tracks
        float left = ProcessToMotorFromJoy(-gamepad1.left_stick_y);
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

        //Drive modifications
        if (gamepad1.x) {
            //Track speed 100%
            setDriveGear(1);
        }
        if (gamepad1.y) {
            //Track speed 50%
            setDriveGear(1);
        }
        if (gamepad1.b) {
            //Track speed 25%
            setDriveGear(1);
        }
        if (gamepad1.a) {
            //reverse drive
            if(driveReverse) driveReverse = false; //if true, make false
            else driveReverse = true; //is false, make true
        }


        //****************BLOCK MANIPULATION****************//

        if (gamepad2.x) {
            //toggle block intake
            HangingAutomation();
        }
        if (gamepad2.a) {
            //Toggle conveyor movement
            if(isConveyorMoving){ //it's already moving, stop it

            } else { //its not, so start it up

            }
        }
        if (gamepad2.b) {
            //have the servo switch which side the blocks fall into
            //create an enum code to know which side the servo is already facing by default so we can change
        }

        //****************OTHER****************//

        //Donglers
        //Left Dongler System
<<<<<<< HEAD
        if (gamepad2.left_bumper) { //left bumper means left dongler
=======
        //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
        //README: I am using gamepad2.right_trigger/bumper for the hanging code please re-map the dongler controls!
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        /*if (gamepad2.right_trigger >= 0.5f) { //lower right trigger means Left Dongler
>>>>>>> origin/master
            if (leftDongDown) { //its already down, so lets move it back up
                srvoDong_Left.setPosition(0.0);
                leftDongDown = false;
            } else { //leftDongDown is false, so make it true and move it down
                srvoDong_Left.setPosition(0.5);
                leftDongDown = true;
            }
        }
        //Right Dongler System
        if (gamepad2.right_bumper) { //right bumper means right dongler
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
<<<<<<< HEAD
        if (gamepad2.right_trigger >= 0.5f) { //The top button is pressed, release tension
=======
        if (gamepad2.dpad_down) { //The top button is pressed, release tension
>>>>>>> origin/master
            hangMotor(-1.0f); //turn backward, release tension
        } else if (gamepad2.dpad_up) { //The lower button is pressed, tighten winch
            hangMotor(1.0f);//turn forward, tighten winch
        }

        //servo hanging
        float srvoHang1JoyCheck = ProcessToMotorFromJoy(-gamepad2.left_stick_y);
        float srvoHang2JoyCheck = ProcessToMotorFromJoy(-gamepad2.right_stick_y);

        if (gamepad2.y) {
            //Hanging automation procedure
            HangingAutomation();
        }


        //****************TELEMETRY****************//

        String tel_Bool_Reverse = (driveReverse)?"REVERSED":"normal";
        String tel_Bool_Speed = "error speed";
        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            tel_Bool_Speed = "at 100% speed";
        } else if (driveGear == 2) { //medium 50% setting
            tel_Bool_Speed = "at 50% speed";
        } else if (driveGear == 1) { //lowest 25% setting
            tel_Bool_Speed = "at 25% speed";
        }
        String tel_Bool_LeftDong = (leftDongDown)?" is down, now moving up":"is up, now moving down";
        String tel_Bool_RightDong = (rightDongDown)?" is down, now moving up":"is up, now moving down";

<<<<<<< HEAD
=======
        telemetry.addData("Distance", sensorUltra_1.getValue());
>>>>>>> origin/master
        telemetry.addData("*****", "Important Booleans");
        telemetry.addData("", "Driving is " + tel_Bool_Reverse + " and " + tel_Bool_Speed);
        telemetry.addData("Left Dongler", "Driving is " + tel_Bool_LeftDong);
        telemetry.addData("Right Dongler", "Driving is " + tel_Bool_RightDong);


    }

    @Override
    public void stop() {

    }
}
