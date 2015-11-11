package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * The Library responsible for every definition and method. All opmodes will inherit methods from here.
 * To learn inheritance: https://www.youtube.com/watch?v=9JpNY-XAseg
 *
 */
public abstract class ResQ_Library extends OpMode {

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
    UltrasonicSensor sanicSensor;
    AnalogInput sensorUltra_1;
    AnalogInput sanicSensor_2;
    ColorSensor sensorRGB;

    //For Multiple Use or Other
    DcMotor motorHangingMech; //responsible for lifting the entire robot
    Servo srvoHang_1;
    Servo srvoHang_2;
    Servo srvoDong_Left;
    Servo srvoDong_Right;


    //****************OTHER DEFINITIONS****************//
    //Ultrasonic algorithm Constants
    final static double RIGHT_TARGET_DISTANCE = 27.0;
    final static double LEFT_TARGET_DISTANCE = 27.0;
    final static double STOP_CONST = 6.0;

    //Color Sensor Calibrations
    final static int COLOR_THRESHOLD = 900;

    //Constants that determine how strong the robot's speed and turning should be
    final static double SPEED_CONST = 0.005;
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
    boolean driveReverse = false; //this reverses the drive so when the robot goes on the ramp, everything works out fine.
    boolean driveSlow = false; //slows drive to make easy turns. if false, full motion, otherwise slow
    boolean leftDongDown = false; //when dong is all the way down, release and press again to go back up automatically
    boolean rightDongDown = false; //same as above but with the right dongler

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

    public void singleStickDrive(float x, float y) {
        drive(y + x, y - x);
    }

    public void hangMotor (float direction){
        motorHangingMech.setPower(direction);
    }

    //****************AUTONOMOUS METHODS****************//

    public void GoForward (int time, int direction) {

    }

    //****************SENSOR METHODS****************//
    public double getDistance() {
        return sensorUltra_1.getValue();
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

    float ProcessDriveInput(float input){ //This calls ProcessToMotorFromJoy but also has drive modification checks
        float output = ProcessToMotorFromJoy(input);
        //At this point, the float should be between a 1 and -1 value that accurately sends to motors
        //Boolean check to ensure full drive control
        output = (driveReverse == true)?-output:output; //if we're supposed to reverse the drive
        output = (driveReverse == true)?0.5f*output:output; //if we're supposed to make the drive slow
        return output;
    }

    float ProcessToMotorFromJoy(float input){ //This is used in any case where joystick input is to be converted to a motor
        float output = 0.0f;

        // clip the power values so that the values never exceed +/- 1
        output = Range.clip(input, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        output = (float)scaleInput(output);

        return output;
    }

    double scaleInput(double dVal)  {
        /*
	     * This method scales the joystick input so for low joystick values, the
	     * scaled value is less than linear.  This is to make it easier to drive
	     * the robot more precisely at slower speeds.
	     */
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

    public String getScaledColor(int r, int g, int b){
        if(r > COLOR_THRESHOLD || g > COLOR_THRESHOLD || b > COLOR_THRESHOLD){
            if(r > g + b){
                return "RED";
            }
            else if(b > r + g){
                return "BLUE";
            }
            else if(g > r + b){
                return "GREEN";
            }
            else return "TOO STRONG";
        }
        else {
            return "GREY";
        }
    }

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        ElapsedTime timer = new ElapsedTime();
        double startTime = timer.time();
        double currentTime = 0.0;
        while(currentTime - startTime < millis) {
            currentTime = timer.time();
        }
    }
}
