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

    double srvoHang1Position;
    double srvoHang2Position;

    boolean autoMoveArm;

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

        //****************BLOCK MANIPULATION****************//

        if (gamepad2.x) {
            //toggle block intake
        }
        if (gamepad2.a) {
            //Toggle conveyor movement
            if (isConveyorMoving) { //it's already moving, stop it

                isConveyorMoving = !isConveyorMoving;
            } else { //its not, so start it up

            }
        }
        if (gamepad2.b) {
            //have the servo switch which side the blocks fall into
            //create an enum code to know which side the servo is already facing by default so we can change
        }

        //****************OTHER****************//

        //Hanging Winch
        if (gamepad2.y) {
            //Hanging automation procedure
            //HangingAutomation();
        }

        if (gamepad2.right_trigger >= 0.5f) {
            //release tension by letting go of string
            motorHangingMech.setPower(-1.0f);
        } else if (gamepad2.left_trigger >= 0.5f) {
            //pull string and add tension
            motorHangingMech.setPower(1.0f);
        } else {
            motorHangingMech.setPower(0);
        }

        //Hanging Servos
        float srvoHang1JoyCheck = ProcessToMotorFromJoy(-gamepad2.left_stick_y);
        float srvoHang2JoyCheck = ProcessToMotorFromJoy(-gamepad2.right_stick_y);
        //Position based hanging
        if(srvoHang1JoyCheck > 0.05) { //big servo
            srvoHang_1.setPosition(0.8f); //move out
        } else if(srvoHang1JoyCheck < -0.05) {
            srvoHang_1.setPosition(1.0f); //move in
        }

        if(srvoHang2JoyCheck > 0.05) { //small servo
            srvoHang_2.setPosition(0.0f); //move in
        } else if(srvoHang2JoyCheck < -0.05) {
            srvoHang_2.setPosition(0.7f); //move out
        }

        //automatic arm hanging
        if(gamepad2.dpad_down) {
            autoMoveArm = true;
        }

        if(autoMoveArm) {
            autoMoveArm = hangingAutomation();
        }

        /*if(srvoHang1JoyCheck > 0.05) {
            srvoHang1Position += HangServoDelta;
        } else if (srvoHang1JoyCheck < -0.05) {
            srvoHang1Position -= HangServoDelta;
        }
        if(srvoHang2JoyCheck > 0.05) {
            srvoHang2Position += HangServoDelta;
        } else if (srvoHang2JoyCheck < -0.05) {
            srvoHang2Position -= HangServoDelta;
        }

        srvoHang1Position = Range.clip(srvoHang1Position, HANG1_MIN_RANGE, HANG1_MAX_RANGE);
        srvoHang2Position = Range.clip(srvoHang2Position, HANG2_MIN_RANGE, HANG2_MAX_RANGE);
        srvoHang_1.setPosition(srvoHang1Position);
        srvoHang_2.setPosition(srvoHang2Position);
        telemetry.addData("Shoulder Servo", ""+srvoHang1Position);
        telemetry.addData("Elbow Servo", ""+srvoHang2Position);*/



        //****************TELEMETRY****************//

        String tel_Bool_Reverse = (driveReverse) ? "REVERSED" : "normal";
        String tel_Bool_Speed = "error speed";
        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            tel_Bool_Speed = "at 100% speed";
        } else if (driveGear == 2) { //medium 50% setting
            tel_Bool_Speed = "at 50% speed";
        } else if (driveGear == 1) { //lowest 25% setting
            tel_Bool_Speed = "at 25% speed";
        }
        telemetry.addData("", "Driving is " + tel_Bool_Reverse + " and " + tel_Bool_Speed);

    }
}