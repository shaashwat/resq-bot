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
	public enum Team {
		RED, BLUE, UNKNOWN
	}

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
		leftPower = 1.0f;
		rightPower = 1.0f;
		drive(leftPower, rightPower);
		moveTillLine();
	}

	public void ColorCheck(){

	}

	@Override
	public void loop() {

	}

	@Override
	public void stop() {

	}

	public void moveTillLine () {
		while(1!=2) { //keep looping until sensor finds a color
			//stop movement immediately
			leftPower = 0.0f;
			rightPower = 0.0f;
			drive(leftPower, rightPower);

			//Determine what color it is to see what team we're on
			if(2==2) { //color is red
				teamWeAreOn = Team.RED;
				TurnToBeacon();
			}
			else if (2==2) { //color is blue
				teamWeAreOn = Team.BLUE;
				TurnToBeacon();
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
