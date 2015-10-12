package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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
    DcMotor motorRightTread;
    DcMotor motorLeftTread;
    DcMotor motorRightFoldableTread;
    DcMotor motorLeftFoldableTread;

    Servo srvoFoldingTracks; //We use the initial tracks, but then fold additional ones with this
    Servo srvoPlow;


    //For Block Manipulation Only
    DcMotor motorConveyor_1; //The system we use requires two
    DcMotor motorConveyor_2; //motors to bring blocks from the field

    Servo srvoSwitch; //this servo switches which side the blocks fall into
    Servo srvoScoreLeft; //opens and scores blocks on left
    Servo srvoScoreRight; //opens and scores blocks on right
    Servo srvoFoldLeft; //folds out scoring mechanism on the left
    Servo srvoFoldRight; //folds out scoring mechanism on the right


    //For Multiple Use or Other
    DcMotor motorHangingMech; //responsible for lifting the entire robot

    //****************OTHER DEFINITIONS****************//

    //The servo positions
    double foldingTracksPosition;
    double plowPosition;
    double switchPosition;
    double scoreRightPosition;
    double scoreLeftPosition;
    double foldRightPosition;
    double foldLeftPosition;


    //Servo Min's and Max's (to prevent the servo from extending too far in any direction
    final static double FOLDTRACK_MIN_RANGE  = 0.20;
    final static double FOLDTRACK_MAX_RANGE  = 0.90;
    final static double PLOW_MIN_RANGE  = 0.20;
    final static double PLOW_MAX_RANGE  = 0.90;
    final static double SWITCH_MIN_RANGE  = 0.20;
    final static double SWITCH_MAX_RANGE  = 0.90;
    final static double SCORERIGHT_MIN_RANGE  = 0.20;
    final static double SCORERIGHT_MAX_RANGE  = 0.90;
    final static double SCORELEFT_MIN_RANGE  = 0.20;
    final static double SCORELEFT_MAX_RANGE  = 0.90;
    final static double FOLDRIGHT_MIN_RANGE  = 0.20;
    final static double FOLDRIGHT_MAX_RANGE  = 0.90;
    final static double FOLDLEFT_MIN_RANGE  = 0.20;
    final static double FOLDLEFT_MAX_RANGE  = 0.90;


    /* This may not be needed, because a method can individually change a servo between set values.
    //Servo Delta changes (higher the value, the quicker it changes and the driver has to press less, but also lowers precision)
    double foldTrackDelta = 0.1;
    double plowDelta = 0.1;
    double switchDelta = 0.1;
    double scoreRightDelta = 0.1;
    double scoreLeftDelta = 0.1;
    double foldRightDelta = 0.1;
    double foldLeftDelta = 0.1;
    */

    //Bools and other important stuff
    boolean areTracksExtended = false; //when the servo folds open these tracks, declare true
    boolean isPlowDown = false; //at the start of the match, declare true and lower plow. When teleop starts, driver will recall it back up and declare false.


    //****************TELEOP METHODS****************//



    //****************AUTONOMOUS METHODS****************//

    public void GoForward (int time, int direction) {

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
