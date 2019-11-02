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

import android.content.Context;
import android.util.Log;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This OpMode uses the roger hardware class to define the devices on the roger.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the roger FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 *
 * https://www.vexforum.com/t/programming-mecanum-wheels/21606/11
 */

@TeleOp(name="Basic Teleop", group="Roger")
public class TeleOp_Basic extends LinearOpMode {

    /* Declare OpMode members. */
    private HardwareRoger roger = new HardwareRoger();      // Use Roger's Hardware
    private ElapsedTime runtime = new ElapsedTime();

    private String[] sounds = {"ss_alarm", "ss_light_saber"};

    // Flag to determine if sounds are playing
    private boolean soundPlaying    = false;

    private boolean isGrabberDown   = false;
    private boolean isHolderDown    = false;

    private static final double COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    private static final int POSITION_UP             = 0;
    private static final int POSITION_DOWN           = 1;

    @Override
    public void runOpMode() {
        double forward;
        double side;
        double rotation;

        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;

        int soundID = -1;
        Context myApp = hardwareMap.appContext;

        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        roger.init(hardwareMap);

        // Send telemetry message to signify roger waiting;
        telemetry.addData("Say", "Hello Driver"); // Roger is a friendly boi.
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set the power to the value of the left stick
            // Remember that the stick value is reversed (Up is -)
            forward         = -gamepad1.left_stick_y;
            side            = gamepad1.left_stick_x;
            rotation        = gamepad1.right_stick_x;

            // TODO: Normalize Values
            backLeftPower = forward - side + rotation;
            backRightPower = forward + side - rotation;
            frontRightPower = forward - side - rotation;
            frontLeftPower = forward + side + rotation;

            // Look for trigger to see if we should play sound
            // Only start a new sound if we are currently not playing one.
            if (gamepad1.right_bumper && !soundPlaying) {

                // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                if ((soundID = myApp.getResources().getIdentifier("ss_wookie", "raw", myApp.getPackageName())) != 0){

                    // Signal that the sound is now playing.
                    soundPlaying = true;

                    // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                    SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                            new Runnable() {
                                public void run() {
                                    soundPlaying = false;
                                }} );
                }
            }

            if (gamepad2.right_bumper) {
                roger.clawServo.setPower(HardwareRoger.CLAW_CLOSE_POWER);
                telemetry.addData("Claw", "Claw Closing!");
            } else if (gamepad2.left_bumper) {
                roger.clawServo.setPower(HardwareRoger.CLAW_STOP);
                telemetry.addData("Claw", "Claw Stopped!");
            }

            if (gamepad2.x) {
                if (isGrabberDown) {
                    moveGrabber(POSITION_UP);
                    isGrabberDown = false;
                } else {
                    moveGrabber(POSITION_DOWN);
                    isGrabberDown = true;
                }
            }

            if (gamepad2.y) {
                if (isHolderDown) {
                    roger.holderLeft.setPosition(HardwareRoger.LEFT_HOLDER_OPEN_POS);
                    roger.holderRight.setPosition(HardwareRoger.RIGHT_HOLDER_OPEN_POS);
                    isHolderDown = false;
                    sleep(100);
                } else {
                    roger.holderLeft.setPosition(HardwareRoger.LEFT_HOLDER_CLOSE_POS);
                    roger.holderRight.setPosition(HardwareRoger.RIGHT_HOLDER_CLOSE_POS);
                    isHolderDown = true;
                    sleep(100);
                }
            }

            // Output the safe vales to the motor drives.
            roger.frontLeft.setPower(frontLeftPower);
            roger.frontRight.setPower(frontRightPower);
            roger.backLeft.setPower(backLeftPower);
            roger.backRight.setPower(backRightPower);

            // Send telemetry message to signify roger running;
            telemetry.addData("frontLeft",  "%.2f", frontLeftPower);
            telemetry.addData("frontRight", "%.2f", frontRightPower);
            telemetry.addData("backLeft", "%.2f", backLeftPower);
            telemetry.addData("backRight", "%.2f", backRightPower);
            telemetry.addData("Grabber", isGrabberDown); 
            telemetry.update();
        }
    }

    private void moveGrabber(int position) {
        int newGrabberTarget;
        double rotations    = 0;
        double timeoutS     = 2;

        if (position == POSITION_UP){
            rotations = 0.4;
        } else if (position == POSITION_DOWN) {
            rotations = -0.4;
        } else {
            telemetry.addData("Position", "No position data found!");
        }

        if (opModeIsActive()) {
            newGrabberTarget = roger.grabber.getCurrentPosition() +
                    (int)(rotations * COUNTS_PER_MOTOR_REV);

            roger.grabber.setTargetPosition(newGrabberTarget);

            // Enable RUN_TO_POSITION
            roger.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Reset timeout time, start to move
            runtime.reset();

            roger.grabber.setPower(0.5);

            while (opModeIsActive() && (runtime.seconds() < timeoutS) &&
                    (roger.grabber.isBusy())) {

                // Display the current data for the driver
                telemetry.addData("Grabber", "Running to %2d, Currently %2d",
                        newGrabberTarget, roger.grabber.getCurrentPosition());
            }

            // After the moves are over, set motor power to 0
            roger.grabber.setPower(0);
            roger.grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }
}
