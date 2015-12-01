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
 */
public abstract class ResQ_Library extends OpMode {
    //****************HARDWARE MAPPING DEFINITIONS****************//

    //For Driving Only
    DcMotor motorRightTread, motorLeftTread, motorRightSecondTread, motorLeftSecondTread;

    //Autonomous
    Servo srvoScoreClimbers, srvoPushButton;

    //Sensors
    AnalogInput sensorUltra_1, sensorUltra_2;
    ColorSensor sensorRGB_1, sensorRGB_2;

    int offsetRed, offsetGreen, offsetBlue;

    /*
        motorHangingMech: responsible for lifting entire robot
        srvoHang_1: servo closer to base(elbow)
        srvoHang_2: servo closer to the hook(wrist)
        srvoDong_Left: frees hanging climbers
        srvoDong_Right: ^
     */
    DcMotor motorHangingMech;
    Servo srvoHang_1, srvoHang_2, srvoDong_Left, srvoDong_Right;


    //****************OTHER DEFINITIONS****************//
    //Ultrasonic algorithm Constants
    final static double RIGHT_TARGET_DISTANCE = 27.0, LEFT_TARGET_DISTANCE = 27.0, STOP_CONST = 6.0;

    //Color Sensor Calibrations
    final static int COLOR_THRESHOLD = 220;

    //Constants that determine how strong the robot's speed and turning should be
    final static double SPEED_CONST = 0.005, LEFT_STEERING_CONST = 0.85, RIGHT_STEERING_CONST = 0.8;

    //Servo Min's and Max's (to prevent the servo from extending too far in any direction
    final static double HANG1_MIN_RANGE  = 0.8;
    final static double HANG1_MAX_RANGE  = 1.00;
    final static double HANG2_MIN_RANGE  = 0.00;
    final static double HANG2_MAX_RANGE  = 0.7;
    final static double DONG1_MIN_RANGE  = 0.20;
    final static double DONG1_MAX_RANGE  = 0.90;
    final static double DONG2_MIN_RANGE  = 0.20;
    final static double DONG2_MAX_RANGE  = 0.90;


    //Booleans
    boolean areTracksExtended = false; //are the Second tracks extended or not (they not at the beginning)

    //Bools and other important stuff
    boolean isPlowDown = false; //at the start of the match, declare true and lower plow. When teleop starts, driver will recall it back up and declare false.
    boolean driveReverse = false; //this reverses the drive so when the robot goes on the ramp, everything works out fine.
    boolean leftDongDown = false; //when dong is all the way down, release and press again to go back up automatically
    boolean rightDongDown = false; //same as above but with the right dongler
    boolean isConveyorMoving = false; //false if conveyor is not moving. changes if it is

    //Other
    int driveGear = 3; //3 is 100%, 2 is 50%, 1 is 25%
    double HangServoDelta = 0.1;

    public enum Team {
        RED, BLUE, UNKNOWN
    }

    Team teamWeAreOn = Team.UNKNOWN; //enum thats represent team

    public ResQ_Library() {

    }

    //****************INITIALIZE METHOD****************//
    public void initializeMapping() {
        //Debug statements to prevent color1 error
        telemetry.addData("Version", "Sensorless. COLOR ERROR SHOULD NOT SHOW UP!");
        //Driving Mapping
        motorLeftTread = hardwareMap.dcMotor.get("m1");
        motorRightTread = hardwareMap.dcMotor.get("m2");
        motorLeftSecondTread = hardwareMap.dcMotor.get("m3");
        motorRightSecondTread = hardwareMap.dcMotor.get("m4");

        //Sensors
        //sensorRGB_1 = hardwareMap.colorSensor.get("color");

        //Other Mapping
        motorHangingMech = hardwareMap.dcMotor.get("m5");
        srvoHang_1 = hardwareMap.servo.get("s1");
        srvoHang_2 = hardwareMap.servo.get("s2");
        /*srvoDong_Left = hardwareMap.servo.get("s3"); //The left servo
        srvoDong_Right = hardwareMap.servo.get("s4"); //The right servo
        srvoPushButton = hardwareMap.servo.get("s5");
        srvoScoreClimbers = hardwareMap.servo.get("s6");*/


        //set the direction of the motors
        motorRightTread.setDirection(DcMotor.Direction.REVERSE);
        motorRightSecondTread.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Version", "non autonomous. COLOR ERROR SHOULD NOT SHOW UP!");
    }

    //****************TELEOP METHODS****************//

    public void drive(float left, float right) {
        // Drives
        if (driveReverse) { //we've reversed the drive in order to climb the ramp
            right = -right;
            left = -left;
        } //if we haven't just leave it be

        if (driveGear == 3) { //highest 100% setting, essentially don't change it
            left = 1f * left;
            right = 1f * right;
        } else if (driveGear == 2) { //medium 50% setting
            left = 0.5f * left;
            right = 0.5f * right;
        } else if (driveGear == 1) { //lowest 25% setting
            left = 0.25f * left;
            right = 0.25f * right;
        } //if there's a bug and it's not 1, 2 or 3, default to max drive

        motorRightTread.setPower(right);
        motorLeftTread.setPower(left);
        motorRightSecondTread.setPower(right);
        motorLeftSecondTread.setPower(left);
    }

    public void setDriveGear(int gear) {
        driveGear = normalizeForGear(gear);
    }

    /*
     * Function to automatically move the hanging arm.
     * Should be called in a loop.
     * @return returns true when the arm is in the process of moving.
     */
    public boolean hangingAutomation() {
        double hang1Position = Math.max(srvoHang_1.getPosition() - HangServoDelta, HANG1_MIN_RANGE);

        srvoHang_1.setPosition(hang1Position);
        //when big servo is midway through it's travel from min to max...
        if(srvoHang_1.getPosition() < (HANG1_MAX_RANGE+HANG1_MIN_RANGE)/2) {
            //tell the big servo to go to it's max range (as fast as possible)
            srvoHang_1.setPosition(HANG1_MAX_RANGE);
            //start the small servo to create a lunging effect
            srvoHang_2.setPosition(HANG2_MAX_RANGE);
            return  false;
        }
        return true;
    }

    //****************SENSOR METHODS****************//
    public void moveToClosestObject() {
        double ultraRight, ultraLeft;
        double rightSpeed, leftSpeed;

        while (true) {
            ultraRight = sensorUltra_1.getValue(); //set these values to sensor readout
            ultraLeft = sensorUltra_2.getValue();

            rightSpeed = SPEED_CONST * (ultraRight - RIGHT_TARGET_DISTANCE) + -RIGHT_STEERING_CONST * (ultraLeft - ultraRight); //speedl = kpd * (dl - tl) + kps * (dl - dr)
            leftSpeed = SPEED_CONST * (ultraLeft - LEFT_TARGET_DISTANCE) + -LEFT_STEERING_CONST * (ultraRight - ultraLeft);

            drive((float) rightSpeed, (float) leftSpeed);

            if (Math.abs(ultraRight - RIGHT_TARGET_DISTANCE) + Math.abs(ultraLeft - LEFT_TARGET_DISTANCE) < STOP_CONST) {
                break;
            }

            //wait(100);
        }
    }

    //****************NUMBER MANIPULATION METHODS****************//

    float ProcessToMotorFromJoy(float input) { //This is used in any case where joystick input is to be converted to a motor
        float output = 0.0f;

        // clip the power values so that the values never exceed +/- 1
        output = Range.clip(input, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        output = (float) scaleInput(output);

        return output;
    }

    double scaleInput(double dVal) {
        /*
         * This method scales the joystick input so for low joystick values, the
      * scaled value is less than linear.  This is to make it easier to drive
      * the robot more precisely at slower speeds.
      */
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

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

    public void calibrateColors() {
        offsetRed = sensorRGB_1.red() + 200;
        offsetGreen = sensorRGB_1.green();
        offsetBlue = sensorRGB_1.blue();
    }

    public Team getColor() {
        int r = sensorRGB_1.red() - offsetRed, b = sensorRGB_1.blue() - offsetBlue;
        if (b > r && b > COLOR_THRESHOLD) {
            return Team.BLUE;
        } else if (r > b && r > COLOR_THRESHOLD) {
            return Team.RED;
        } else {
            return Team.UNKNOWN;
        }
    }

    int normalizeForGear(int gear) {
        if (gear > 3) gear = 3;
        if (gear < 1) gear = 1;
        return gear;
    }

    //****************MISC METHODS****************//
    public void sleep(int millis) {
        /*ElapsedTime timer = new ElapsedTime();
        double startTime = timer.time();
        double currentTime = 0.0;
        while (currentTime - startTime < millis) {
            currentTime = timer.time();
        }
    }*/
        try {
            Thread.sleep(millis);
        } catch (Exception err) {
            telemetry.addData("ERROR", "UR THREADS SUCK HEE HEE");
        }
    }
}