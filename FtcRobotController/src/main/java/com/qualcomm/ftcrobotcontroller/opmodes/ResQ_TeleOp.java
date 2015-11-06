package com.qualcomm.ftcrobotcontroller.opmodes;

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
        srvoDong_1 = hardwareMap.servo.get("servo_3"); //The left servo
        srvoDong_2 = hardwareMap.servo.get("servo_4"); //The right servo
        srvoPushButton = hardwareMap.servo.get("servo_5");
        srvoScoreClimbers = hardwareMap.servo.get("servo_6");;


        //set the direciton of the motors
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftMid.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        //set the direction of the servos
        srvoDong_1.setDirection(Servo.Direction.FORWARD);
        srvoDong_2.setDirection(Servo.Direction.FORWARD);
        srvoHang_1.setDirection(Servo.Direction.FORWARD);
        srvoHang_1.setDirection(Servo.Direction.FORWARD);
    }


    @Override
    public void loop() {

        float right = ProcessMotorInput(-gamepad1.right_stick_y);
        float left = ProcessMotorInput(-gamepad1.left_stick_y);
		/*
		 * Gamepad 1:
		 * Left joystick moves the left track, and the right joystick moves the right track
		 */

        //****************DRIVING****************//

        //drive(left, right);
        motorLeftFront.setPower(left);
        motorLeftMid.setPower(left);
        motorLeftBack.setPower(left);
        motorRightFront.setPower(right);
        motorRightMid.setPower(right);
        motorRightBack.setPower(right);

        //****************BLOCK MANIPULATION****************//


        //****************OTHER****************//
        
        //srvoDong_1.setPosition(0.5);

        //****************TELEMETRY****************//

        telemetry.addData("", "******************");

    }

    @Override
    public void stop() {

    }
}
