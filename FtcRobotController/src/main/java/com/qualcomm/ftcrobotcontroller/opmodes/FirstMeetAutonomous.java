package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Autonomous program - for first meet use only
 * This autonomous program is for the first robotics competition on November 14/21 ONLY
 *
 * It assumes that other robots barely have an autonomous program at all, and its not dynamic to alliance
 * For best use, place robot in starting position closest to center line
 * Program of movement:
 * 		- Move forward until hits line (ultrasonic to stop movement if alliance robot is moving in front)
 * 		- We detect what color the line is to determine what team we're on, sepreate codes for each.
 * 		- turn X amount of degrees to face beacon in general direction
 * 		- some IMU sensor to keep straight path
 * 		- ultrasonic to detect wall/beacon and stop a short distance ahead
 * 		- sliding autonomous mechanisms deploy and start doing their thing
 * 		- we back up a bit and turn Y amount to face ramp, then choose which side of ramp to climb
 * 		- go to ramp and climb it as much as possible, stop motors once we've been turning for 5 seconds but no movement has been made (aka we're stuck)
 * 		- deploy hanging mech when gyroscope detects rotation change in order to stop us from falling off of ramp, touch sensor maybe to detect we've reached the end and we should stop.
 */
@SuppressWarnings("all")
public class FirstMeetAutonomous extends ResQ_Library {

	ColorSensor sensorRGB;

	float leftPower;
	float rightPower;
	double currentTimeCatch;

	boolean foundLine = false;
	boolean robotFirstTurn = false; //when we get to the line, turn in the general direction of the beacon


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
		//Do the map thing
		initializeMapping();
		driveGear = 3;
		calibrateColors();
	}

	@Override
	public void loop() {
		if(!foundLine) {
			moveTillLine();
		}
		else if (!robotFirstTurn){
			//Turn();
			if(teamWeAreOn == Team.RED) telemetry.addData("On team:", "RED");
			if(teamWeAreOn == Team.BLUE) telemetry.addData("On team:", "BLUE");
		}
	}

	@Override
	public void stop() {

	}

	public void moveTillLine() {
		teamWeAreOn = getColor();
		if(teamWeAreOn == Team.UNKNOWN) {
			goForward();
		}
		else {
			stopMoving();
			foundLine = true;
		}
	}

	public void TurnToBeacon() { //(turn to beacon)
		//Simplified (DAMN JACOB)
		if (1!=1) { //robotFirstTurn
			robotFirstTurn = true;
			drive(1,1);
		} else {
			int m = teamWeAreOn == Team.RED ? 1 : -1;
			drive(-.5f * m, .5f * m);
		}


		//If we're red, turn left 70 degrees
		//Do some compass thing in order to stop our turning
		/*if(teamWeAreOn == Team.RED){ //false so we're on red team
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
		}*/
	}

	public void goForward(){
		leftPower = -1.0f;
		rightPower = -1.0f;
		drive(leftPower, rightPower);
	}
	public void stopMoving(){
		leftPower = 0.0f;
		rightPower = 0.0f;
		drive(leftPower, rightPower);
	}
}