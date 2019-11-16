/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the roger.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Go to Tape: Red Forward", group="Roger")
public class DriveByEncoderRedTapeFwd extends LinearOpMode {

    /* Declare OpMode members. */
    // TODO: You know what to do...
    private HardwareRoger           roger   = new HardwareRoger();   // Use roger's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    // TODO: If this breaks, remove the privates
    private static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    private static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double     DRIVE_SPEED             = 0.8;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        roger.init(hardwareMap);

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float[] hsvValues = {0F, 0F, 0F};

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attenuate the measured values.
        final double SCALE_FACTOR = 255;

        // Send telemetry message to signify roger waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        ColorSensor sensorColor = roger.sensorColor;

        allMotorsStopResetEncoder();

        allMotorsRunUsingEncoder();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %2d :%2d :%2d :%2d",
                roger.frontLeft.getCurrentPosition(),
                roger.frontRight.getCurrentPosition(),
                roger.backLeft.getCurrentPosition(),
                roger.backRight.getCurrentPosition());

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // FwdRev moves bot forward to avoid obstacles
         mechFwdRev(DRIVE_SPEED, 24, 3);

        while (opModeIsActive() && hsvValues[0] > 50) {
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            roger.frontRight.setPower(DRIVE_SPEED);
            roger.frontLeft.setPower(-DRIVE_SPEED);
            roger.backRight.setPower(-DRIVE_SPEED);
            roger.backLeft.setPower(DRIVE_SPEED);
        }

        allMotorsOff();

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /**
     * Method to drive Roger forward or backward based on:
     * @param power Power (-1 - 1) for motors to drive
     * @param distance Distance to be travelled by Roger (Positive value indicates forward travel)
     * @param timeoutS timeout(seconds) for command to be completed
     */
    private void mechFwdRev(double power, double distance, double timeoutS) {
        int newFLTarget;
        int newFRTarget;
        int newBLTarget;
        int newBRTarget;

        if (opModeIsActive()) {
            newFLTarget = roger.frontLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newFRTarget = roger.frontRight.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newBLTarget = roger.backLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newBRTarget = roger.backRight.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);

            setTargetPosition(newFLTarget, newFRTarget, newBLTarget, newBRTarget);

            // Enable RUN_TO_POSITION
            allMotorsRunToPosition();

            // Reset timeout time, start to move
            runtime.reset();
            allMotorsPower(power);

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (roger.frontLeft.isBusy()) || roger.frontRight.isBusy()
                    || roger.backLeft.isBusy() || roger.backRight.isBusy()) {

                // Display the current data for the driver
                telemetry.addData("FL", "Running to %2d, Currently %2d",
                        newFLTarget, roger.frontLeft.getCurrentPosition());
                telemetry.addData("FR", "Running to %2d, Currently %2d",
                        newFRTarget, roger.frontRight.getCurrentPosition());
                telemetry.addData("BL", "Running to %2d, Currently %2d",
                        newBLTarget, roger.backLeft.getCurrentPosition());
                telemetry.addData("BR", "Running to %2d, Currently %2d",
                        newBRTarget, roger.backRight.getCurrentPosition());
                telemetry.update();
            }

            // After the moves are over, set motor power to 0
            allMotorsOff();
            allMotorsRunUsingEncoder();

            // sleep(250); // Optional sleep after each move
        }
    }

    /**
     * Method to drive Roger right or left based on:
     * @param power Power (-1 - 1) for motors to drive
     * @param distance Distance to be travelled by Roger (Positive value indicates right travel)
     * @param timeoutS timeout(seconds) for command to be completed
     */
    // TODO: You know what to do...
    private void mechLeftRight(double power, double distance, double timeoutS) {
        int newFLTarget;
        int newFRTarget;
        int newBLTarget;
        int newBRTarget;

        if (opModeIsActive()) {
            newFLTarget = roger.frontLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newFRTarget = roger.frontLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newBLTarget = roger.frontLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);
            newBRTarget = roger.frontLeft.getCurrentPosition() +
                    (int)(distance * COUNTS_PER_INCH);

            if (distance > 0) {
                roger.frontLeft.setTargetPosition(newFLTarget);
                roger.frontRight.setTargetPosition(-newFRTarget);
                roger.backLeft.setTargetPosition(-newBLTarget);
                roger.backRight.setTargetPosition(newBRTarget);
            } else {
                roger.frontLeft.setTargetPosition(-newFLTarget);
                roger.frontRight.setTargetPosition(newFRTarget);
                roger.backLeft.setTargetPosition(newBLTarget);
                roger.backRight.setTargetPosition(-newBRTarget);
            }

            // Enable RUN_TO_POSITION
            allMotorsRunToPosition();

            // Reset timeout time, start to move
            runtime.reset();
            allMotorsPower(power);

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (roger.frontLeft.isBusy()) || roger.frontRight.isBusy()
                    || roger.backLeft.isBusy() || roger.backRight.isBusy()) {

                // Display the current data for the driver
                telemetry.addData("FL", "Running to %2d, Currently %2d",
                        newFLTarget, roger.frontLeft.getCurrentPosition());
                telemetry.addData("FR", "Running to %2d, Currently %2d",
                        newFRTarget, roger.frontRight.getCurrentPosition());
                telemetry.addData("BL", "Running to %2d, Currently %2d",
                        newBLTarget, roger.backLeft.getCurrentPosition());
                telemetry.addData("BR", "Running to %2d, Currently %2d",
                        newBRTarget, roger.backRight.getCurrentPosition());
                telemetry.update();
            }

            // After the moves are over, set motor power to 0, reset encoders
            allMotorsOff();
            allMotorsRunUsingEncoder();

            // sleep(250); // Optional sleep after each move
        }
    }

    private void allMotorsOff() {
        roger.frontLeft.setPower(0);
        roger.frontRight.setPower(0);
        roger.backLeft.setPower(0);
        roger.backRight.setPower(0);
    }

    private void allMotorsPower(double power) {
        roger.frontLeft.setPower(Math.abs(power));
        roger.frontRight.setPower(Math.abs(power));
        roger.backLeft.setPower(Math.abs(power));
        roger.backRight.setPower(Math.abs(power));
    }

    private void allMotorsRunToPosition() {
        roger.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        roger.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        roger.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        roger.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void allMotorsRunUsingEncoder() {
        roger.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        roger.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        roger.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        roger.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void allMotorsStopResetEncoder() {
        roger.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        roger.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        roger.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        roger.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void setTargetPosition(int newFLTarget, int newFRTarget,
                                   int newBLTarget, int newBRTarget) {

        roger.frontLeft.setTargetPosition(newFLTarget);
        roger.frontRight.setTargetPosition(newFRTarget);
        roger.backLeft.setTargetPosition(newBLTarget);
        roger.backRight.setTargetPosition(newBRTarget);
    }
}
