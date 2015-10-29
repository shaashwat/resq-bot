package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 *
 */
public abstract class ResQ_Library extends OpMode {

    /**
     * Constructor
     */
    public ResQ_Library() {

    }

    //****************HARDWARE MAPPING DEFINITIONS****************//

    //For Driving Only
    DcMotor motorLeftFront;
    DcMotor motorLeftMid;
    DcMotor motorLeftBack;
    DcMotor motorRightFront;
    DcMotor motorRightMid;
    DcMotor motorRightBack;

    //Autonomous
    Servo srvoScoreClimbers;
    Servo srvoPushButton;

    //Sensors
    UltrasonicSensor sanic;

    //For Multiple Use or Other
    DcMotor motorHangingMech; //responsible for lifting the entire robot
    Servo srvoHang_1;
    Servo srvoHang_2;
    Servo srvoDong_1;
    Servo srvoDong_2;


    //****************OTHER DEFINITIONS****************//
    //Ultrasonic algorithm Constants
    final static double RIGHT_TARGET_DISTANCE = 27.0;
    final static double LEFT_TARGET_DISTANCE = 27.0;
    final static double STOP_CONST = 6.0;

    //Constants that determine how strong the robot's speed and turning should be
    final static float SPEED_CONST = 1.55f;
    final static double LEFT_STEERING_CONST = 0.85;
    final static double RIGHT_STEERING_CONST = 0.8;



    //Servo Min's and Max's (to prevent the servo from extending too far in any direction
    final static double HANG1_MIN_RANGE  = 0.20;
    final static double HANG1_MAX_RANGE  = 0.90;
    final static double HANG2_MIN_RANGE  = 0.20;
    final static double HANG2_MAX_RANGE  = 0.90;
    final static double DONG1_MIN_RANGE  = 0.20;
    final static double DONG1_MAX_RANGE  = 0.90;
    final static double DONG2_MIN_RANGE  = 0.20;
    final static double DONG2_MAX_RANGE  = 0.90;


    //Bools and other important stuff
    boolean isPlowDown = false; //at the start of the match, declare true and lower plow. When teleop starts, driver will recall it back up and declare false.

    //****************TELEOP METHODS****************//

    public void drive(float left, float right){
        // Drives
        motorLeftFront.setPower(left);
        motorLeftMid.setPower(left);
        motorLeftBack.setPower(left);
        motorRightFront.setPower(right);
        motorRightMid.setPower(right);
        motorRightBack.setPower(right);
    }

    //****************AUTONOMOUS METHODS****************//

    public void GoForward (int time, int direction) {

    }

    //****************SENSOR METHODS****************//
    public double getDistance() {
        return sanic.getUltrasonicLevel();
    }

    public void moveToClosestObject() {
        double ultraRight;
        double ultraLeft;

        double rightSpeed;
        double leftSpeed;

        while(true) {
            ultraRight = 0; //set these values to sensor readout
            ultraLeft = 0;

            rightSpeed = SPEED_CONST * (ultraRight - RIGHT_TARGET_DISTANCE) + -RIGHT_STEERING_CONST * (ultraLeft - ultraRight); //speedl = kpd * (dl - tl) + kps * (dl - dr)
            leftSpeed = SPEED_CONST * (ultraLeft - LEFT_TARGET_DISTANCE) + -LEFT_STEERING_CONST * (ultraRight - ultraLeft);

            drive((float)rightSpeed, (float)leftSpeed);

            if(Math.abs(ultraRight - RIGHT_TARGET_DISTANCE) + Math.abs(ultraLeft - LEFT_TARGET_DISTANCE) < STOP_CONST) {
                break;
            }

            //wait(100);
        }
    }

    //****************NUMBER MANIPULATION METHODS****************//

    float ProcessMotorInput(float input){
        float output = 0.0f;

        // clip the power values so that the values never exceed +/- 1
        output = Range.clip(input, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        output = (float)scaleInput(output);

        return output;
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
        index = Math.abs(index);

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
