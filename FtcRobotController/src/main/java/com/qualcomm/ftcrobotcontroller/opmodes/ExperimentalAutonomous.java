package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
/**
 * This program is an experimental backup to the autonomous program if triangulation is unfinished.
 * It will follow the line until it is ___ away from the wall (room for error).
 * then it will right if it followed the blue line or left if it followed the red line.
 * It will keep on turning while the ultrasonic values detect a decrease in distance from the wall.
 * As soon as there is an increase, it is known that the robot is aligned with the wall.
 * After it is aligned it will turn 90 degrees (in the same direction) and go forward until...
 * the color sensor detects white.
 * Then, it will go a little past and then turn until it is aligned with the line
 * It is now aligned to press a button.
 **/

@SuppressWarnings("all")
public class ExperimentalAutonomous extends ResQ_Library {
    public enum Team {
        RED, BLUE, UNKNOWN
    }

    ColorSensor sensorRGB;



    float leftPower;
    float rightPower;
    double currentTimeCatch;
    Team teamWeAreOn = Team.UNKNOWN; //enum thats represent team

    /**
     * Blue Team Information:
     * 		- team is on the right side of the map, line is on robot's left and ramp on robot's right
     * 		- ramp to climb is far from starting, close to the beacon, turn right to access
     * 		- if going to ramp across the line, ramp is closer to starting position and not the beacon, turn left to access
     *
     * Red Team Information:
     * 		- team is on the left side of the map, line is on robot's right and ramp on robot's left
     * 		- ramp to climb is far from starting, close to the beacon, turn left to access
     * 		- if going to ramp across the line, ramp is closer to starting position and not the beacon, turn right to access
     */

    @Override
    public void init() {

        //Driving Mapping
        /*motorLeftFront = hardwareMap.dcMotor.get("motor_1");
        motorLeftMid = hardwareMap.dcMotor.get("motor_2");
        motorLeftBack = hardwareMap.dcMotor.get("motor_3");
        motorRightFront = hardwareMap.dcMotor.get("motor_4");
        motorRightMid = hardwareMap.dcMotor.get("motor_5");
        motorRightBack = hardwareMap.dcMotor.get("motor_6");*/

        leftPower = 1.0f;
        rightPower = 1.0f;
        drive(leftPower, rightPower);
        moveTillLine();
    }

    public void colorCheck(){
        int red = sensorRGB.red();
        int blue = sensorRGB.blue();
        int green = sensorRGB.green();
        telemetry.addData("blue", blue);
        telemetry.addData("red", red);
        //telemetry.addData("assumed", getScaledColor(red, blue, green));
        if(red > COLOR_THRESHOLD || blue > COLOR_THRESHOLD){
            if(blue > red) teamWeAreOn = Team.BLUE;
            else if(red > blue) teamWeAreOn = Team.RED;
        }
    }

    @Override
    public void loop() {
        //telemetry.addData("Time", "elapsed time: " + Double.toString(this.time));
    }

    @Override
    public void stop() {

    }

    public void moveTillLine () {
        boolean foundLine = false;
        while(!foundLine) { //keep looping until sensor finds a color
            //stop movement immediately
            leftPower = 0.0f;
            rightPower = 0.0f;
            drive(leftPower, rightPower);

            colorCheck();
            //Determine what color it is to see what team we're on
            if(teamWeAreOn == Team.RED) { //color is red
                followLine();
            }
            else if (teamWeAreOn == Team.BLUE) { //color is blue
                followLine();
            }
            else { //color is none of the above, go back a couple of steps and try again
                currentTimeCatch = this.time; //collect current time
                double timeToStopGoingBack = currentTimeCatch + 4.0; //time of confusion plus 4 seconds
                if (this.time <= timeToStopGoingBack) {
                    // go backwards until time to stop going back
                    leftPower = -0.5f;
                    rightPower = -0.5f;
                    drive(leftPower, rightPower);
                } else if (this.time > timeToStopGoingBack) {
                    // move forward again until it finds the line
                    moveTillLine();
                }

            }
        }
    }

        public void followLine() {
            float alpha = sensorRGB.alpha();
            float r = sensorRGB.red();
            float g = sensorRGB.green();
            float b = sensorRGB.blue();

        }

        public void TurnToBeacon() { //(turn to bacon)
        //If we're red, turn left 70 degrees
        //Do some compass thing in order to stop our turning
        if(teamWeAreOn == Team.RED){ //false so we're on red team
            if (1!=1){ //If compass detects that we're finished turning
                //Drive straight
                drive(1.0f, 1.0f);
            } else { //we're not finished turning fam
                drive(-0.5f, 0.5f);
            }
        }

        else if(teamWeAreOn == Team.BLUE){ //true, so we're on blue team
            //If we're blue, turn right 70 degrees
            if (1!=1){ //If compass detects that we're finished turning
                //Drive straight
                drive(1.0f, 1.0f);
            } else { //we're not finished turning fam
                drive(0.5f, -0.5f);
            }
        }
    }
}

