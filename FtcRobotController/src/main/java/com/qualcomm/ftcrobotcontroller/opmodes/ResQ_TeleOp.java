package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
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

		/*
		 * There are 7 motors
		 *   "motor_1" connects to the main tread motor on the right side
		 *   "motor_2" connects to the main tread motor on the left side (motor is reversed)
		 *   "motor_3" connects to the foldable tread motor on the right side
		 *   "motor_4" connects to the foldable tread motor on the left side (motor is reversed)
		 *   "motor_5" is the first conveyor motor to draw the balls in
		 *   "motor_6" is the second conveyor motor to draw the balls in. (motor is reversed)
		 *   "motor_7" is the motor responsible for lifting up the robot
		 *
		 * There are 7 servos
		 *    "servo_1" folds out additional tracks at the beginning of the match
		 *    "servo_2" fold out a plow to clear the path during autonomous
		 *    "servo_3" controls a switch that changes which side blocks fall into once in
		 *    "servo_4" controls a mech to open and score the balls on the right side
		 *    "servo_5" controls a mech to open and score the balls on the left side
		 *    "servo_6" folds open the scoring mech on the left side
		 *    "servo_7" folds open the scoring mech on the left side
		 */

        //Driving Mapping
        motorRightTread = hardwareMap.dcMotor.get("motor_1");
        motorLeftTread = hardwareMap.dcMotor.get("motor_2");
        motorRightFoldableTread = hardwareMap.dcMotor.get("motor_3");
        motorLeftFoldableTread = hardwareMap.dcMotor.get("motor_4");
        motorLeftTread.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFoldableTread.setDirection(DcMotor.Direction.REVERSE);
        srvoFoldingTracks = hardwareMap.servo.get("servo_1");
        srvoPlow = hardwareMap.servo.get("servo_2");

        //Block Manipulation Mapping
        motorConveyor_1 = hardwareMap.dcMotor.get("motor_5");
        motorConveyor_2 = hardwareMap.dcMotor.get("motor_6");
        motorConveyor_2.setDirection(DcMotor.Direction.REVERSE);
        srvoSwitch = hardwareMap.servo.get("servo_3");
        srvoScoreRight = hardwareMap.servo.get("servo_4");
        srvoScoreLeft = hardwareMap.servo.get("servo_5");
        srvoFoldRight = hardwareMap.servo.get("servo_6");
        srvoFoldLeft = hardwareMap.servo.get("servo_7");

        //Other Mapping
        motorHangingMech = hardwareMap.dcMotor.get("motor_7");

        // assign the starting position of all the servos
        reset();
    }

    public void reset(){
        foldingTracksPosition = 0.2;
        plowPosition = 0.2;
        switchPosition = 0.2;
        scoreRightPosition = 0.2;
        scoreLeftPosition = 0.2;
        foldRightPosition = 0.2;
        foldLeftPosition = 0.2;
    }

    public void plowCheck(){
        //Deals with the plow
        if (gamepad1.y) {
            if(isPlowDown){ //plow is down, move back up
                srvoPlow.setPosition(1.0);
            } else { //plow is up, but for some reason, we want it back down
                srvoPlow.setPosition(0.0);
            }
        }
    }

    public void conveyerCheck(float right){
        //So over here, first have the control for the conveyer belts. I guess its an on off system so button

        if (gamepad2.x) {
            if(isConveyorMoving){ //plow is down, move back up
                motorRightTread.setPower(right);
            } else { //plow is up, but for some reason, we want it back down
                srvoPlow.setPosition(0.0);
            }
        }
    }

    public void drive(float left, float right){

        // Drives
        motorRightTread.setPower(right);
        motorLeftTread.setPower(left);
        if(areTracksExtended){
            motorRightFoldableTread.setPower(right);
            motorLeftFoldableTread.setPower(left);
        }
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

        // note that if y equal -1 then joystick is pushed all of the way forward.
        drive(left, right);

        //****************BLOCK MANIPULATION****************//

        conveyerCheck(right);

        //****************OTHER****************//

        plowCheck();

        //****************TELEMETRY****************//

        telemetry.addData("turn up", "lol");
    }

    @Override
    public void stop() {

    }
}
