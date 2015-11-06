/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.Minibot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class MinibotTeleOp extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the turn servo approaches 0, the turn position moves up (away from the floor).
     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
     */
    // TETRIX VALUES.
    final static double TURN_MIN_RANGE  = 0.20;
    final static double TURN_MAX_RANGE  = 0.7;

    // position of the turn servo.
    double turnPosition;

    // amount to change the turn servo position.
    double turnDelta = 0.1;
    
    DcMotor motorPower;
    Servo turn;

    ColorSensor sensorRGB;

    /**
     * Constructor
     */
    public MinibotTeleOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_2" is on the Power side of the bot and reversed.
		 *   
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the turn joint of the manipulator.
		 */
        /*motorPower = hardwareMap.dcMotor.get("motor_1");

        turn = hardwareMap.servo.get("servo_1");

        // assign the starting position of the wrist and claw
        turnPosition = 0.5;
        */

        sensorRGB = hardwareMap.colorSensor.get("lady");
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the Power stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: Power_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: Power_stick_x ranges from -1 to 1, where -1 is full Power
        // and 1 is full right
        /*float throttle = -gamepad1.left_stick_y;
        float Power = throttle;

        // clip the power values so that the values never exceed +/- 1
        Power = Range.clip(Power, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        Power =  (float)scaleInput(Power);

        // write the values to the motors
       // motorPower.setPower(Power);

        // update the position of the turn.
        if (gamepad1.a || gamepad1.right_stick_x > 0.0f) {
            // if the A button or joystick right is pushed on gamepad1, increment the position of
            // the turn servo.
            turnPosition += turnDelta;
        }

        if (gamepad1.y || gamepad1.right_stick_x < 0.0f) {
            // if the Y button or joystick right is pushed on gamepad1, decrease the position of
            // the turn servo.
            turnPosition -= turnDelta;
        }*/
        

        // clip the position values so that they never exceed their allowed range.
        //turnPosition = Range.clip(turnPosition, TURN_MIN_RANGE, TURN_MAX_RANGE);

        // write position values to the wrist and claw servo
        //turn.setPosition(turnPosition);

        /*telemetry.addData("Red  ", sensorRGB.red());
        telemetry.addData("Green", sensorRGB.green());
        telemetry.addData("Blue ", sensorRGB.blue());*/

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        //telemetry.addData("Text", "*** Robot Data***");
        //telemetry.addData("turn", "turn:  " + String.format("%.2f", turnPosition));
        //telemetry.addData("Power tgt pwr",  "Power  pwr: " + String.format("%.2f", Power));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     */
    @Override
    public void stop() {

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
