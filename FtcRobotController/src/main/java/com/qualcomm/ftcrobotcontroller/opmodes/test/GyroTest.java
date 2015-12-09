package com.qualcomm.ftcrobotcontroller.opmodes.test;

import com.qualcomm.ftcrobotcontroller.opmodes.test.AdafruitIMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.I2cDevice;
import java.util.concurrent.locks.Lock;

/**
 * Created by Admin on 11/6/2015.
 */
public class GyroTest extends OpMode {
    AdafruitIMU gyro;
    String gyroName = "gyro";

    final static double RIGHT_ROTATION_CONST = 0.0027;
    final static double LEFT_ROTATION_CONST = 0.0027;
    final static double ROTATION_OFFSET = 0.1;

    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];

    @Override
    public void init() {
        telemetry.addData("init", "We initialized");
        try {
            gyro = new AdafruitIMU(hardwareMap, gyroName, (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2), (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch(RobotCoreException rce) {
            telemetry.addData("RobotCoreException", rce.getMessage());
        }
    }

    @Override
    public void start() {
        gyro.startIMU();
    }

    @Override
    public void loop() {
        gyro.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
        telemetry.addData("Rotation Roll", rollAngle[0] + ", " + rollAngle[1]);
        telemetry.addData("Rotation Pitch", pitchAngle[0] + ", " + pitchAngle[1]);
        telemetry.addData("Rotation Yaw", yawAngle[0] + ", " + yawAngle[1]);
    }

    /*public void driveStraight(double millis) {
        /*
         * This algorithm assumes gyro.getRotation() returns
         * values between 0—359 or -180—179. The actual
         * return value is not documented, therefore it has
         * to be tested.
         *
        gyro.calibrate();
        while(gyro.isCalibrating()) {
            telemetry.addData("Calibrating: ", "gyro is calibrating...");
        }
        telemetry.clearData();

        double startDir = gyro.getRotation();
        double startTime = System.currentTimeMillis();
        double currentTime = 0.0;

        double rSpeed = 1.0f;
        double lSpeed = 1.0f;

        while(currentTime - startTime < millis) {
            rSpeed = (180 + gyro.getRotation()) * RIGHT_ROTATION_CONST + ROTATION_OFFSET;
            lSpeed = (180 - gyro.getRotation()) * LEFT_ROTATION_CONST + ROTATION_OFFSET;

            //round any values <0 or >1 to 0 or 1.
            rSpeed = Math.max(0, Math.min(1.0, rSpeed));
            lSpeed = Math.max(0, Math.min(1.0, lSpeed));

            drive((float) lSpeed, (float) rSpeed);
            currentTime = System.currentTimeMillis();
            //we need a wait function
        }
    }

    /*
     * empty function meant to simulate the drive function so
     * moving test functions over to the library will be easy
     *
    public void drive(float left, float right){

    }*/

}

