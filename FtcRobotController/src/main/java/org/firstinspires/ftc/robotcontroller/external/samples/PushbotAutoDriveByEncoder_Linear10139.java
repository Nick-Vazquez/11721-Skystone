///* Copyright (c) 2017 FIRST. All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without modification,
// * are permitted (subject to the limitations in the disclaimer below) provided that
// * the following conditions are met:
// *
// * Redistributions of source code must retain the above copyright notice, this list
// * of conditions and the following disclaimer.
// *
// * Redistributions in binary form must reproduce the above copyright notice, this
// * list of conditions and the following disclaimer in the documentation and/or
// * other materials provided with the distribution.
// *
// * Neither the name of FIRST nor the names of its contributors may be used to endorse or
// * promote products derived from this software without specific prior written permission.
// *
// * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
// * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
// * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//
//package org.firstinspires.ftc.robotcontroller.external.samples;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
///**
// * This file illustrates the concept of driving a path based on encoder counts.
// * It uses the common Pushbot hardware class to define the drive on the goat.
// * The code is structured as a LinearOpMode
// *
// * The code REQUIRES that you DO have encoders on the wheels,
// *   otherwise you would use: PushbotAutoDriveByTime;
// *
// *  This code ALSO requires that the drive Motors have been configured such that a positive
// *  power command moves them forwards, and causes the encoders to count UP.
// *
// *   The desired path in this example is:
// *   - Drive forward for 48 inches
// *   - Spin right for 12 Inches
// *   - Drive Backwards for 24 inches
// *   - Stop and close the claw.
// *
// *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
// *  that performs the actual movement.
// *  This methods assumes that each movement is relative to the last stopping place.
// *  There are other ways to perform encoder based moves, but this method is probably the simplest.
// *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
// *
// * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
// * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
// */
//
//@Autonomous(name="Pushbot: Auto Drive By Encoder", group="Pushbot")
//@Disabled
//public class PushbotAutoDriveByEncoder_Linear10139 extends LinearOpMode {
//
//    /* Declare OpMode members. */
//    HardwarePushbot10139    goat = new HardwarePushbot10139();   // Use a Pushbot's hardware
//    private ElapsedTime     runtime = new ElapsedTime();
//
//    static final double     COUNTS_PER_MOTOR_REV    = 2240 ;    // eg: TETRIX Motor Encoder
//    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
//    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
//    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
//                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
//    static final double     DRIVE_SPEED             = 0.6;
//    static final double     TURN_SPEED              = 0.5;
//
//    @Override
//    public void runOpMode() {
//
//        /*
//         * Initialize the drive system variables.
//         * The init() method of the hardware class does all the work here
//         */
//        goat.init(hardwareMap);
//
//        // Send telemetry message to signify goat waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();
//
//        goat.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        goat.leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        goat.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        goat.rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        goat.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        goat.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        goat.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        goat.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Path0",  "Starting at %2d :%2d :%2d :%2d",
//                          goat.leftFront.getCurrentPosition(),
//                          goat.rightFront.getCurrentPosition(),
//                          goat.leftRear.getCurrentPosition(),
//                          goat.rightRear.getCurrentPosition());
//
//        telemetry.update();
//
//        // Wait for the game to start (driver presses PLAY)
//        waitForStart();
//
//        // Step through each leg of the path,
//        // Note: Reverse movement is obtained by setting a negative distance (not speed)
//        encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
//        encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
//        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout
//
//        goat.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
//        goat.rightClaw.setPosition(0.0);
//        sleep(1000);     // pause for servos to move
//
//        telemetry.addData("Path", "Complete");
//        telemetry.update();
//    }
//
//    /*
//     *  Method to perfmorm a relative move, based on encoder counts.
//     *  Encoders are not reset as the move is based on the current position.
//     *  Move will stop if any of three conditions occur:
//     *  1) Move gets to the desired position
//     *  2) Move runs out of time
//     *  3) Driver stops the opmode running.
//     */
//
//    /**
//     * TODO: Hopefully this delightful color will make you read this... lol
//     * NOTES FROM NICK:
//     *
//     * As you can see, there's a lot of red in this next method. What I did for the "mechFwdRev"
//     * method below was copy this, and then move some things around to make it work.
//     * What should we do you ask?
//     * What I would like you to do is to create a new function that will move the robot left and
//     * right based on the distance parameter. Once you are done doing that, you can delete this
//     * "encoderDrive" method or comment it out, and then deal with the errors that are created
//     * by doing so. When you are done with the other method I would like you guys to call each
//     * method at least once (Look at how it's done in lines 118-120).
//     *
//     * After you figure that out, I'd like you guys to think about some conceptual questions that
//     * can improve the look and function of a program...
//     * 1. What can I make into a function? - Anything you do more than once is something that you
//     *    might want to consider making into a function.
//     * 2. How can I make this code easier to read? - Eventually your code is going to do something
//     *    wacky that you don't want it to do. If you don't want to go though a butt ton of code,
//     *    your best bet would be to add comments above code that does things. So everywhere.
//     * 3. Have some fun! Your robot can do most everything you want it to do (Within reason)...
//     *    If you have some wacky idea to add a feature unique to your robot, try it out! But make
//     *    sure to remember the changes you make! I'll teach you guys about Git later.
//     *
//     *          _____                    _____                    _____                    _____                    _____
//     *          /\    \                  /\    \                  /\    \                  /\    \                  /\    \
//     *         /::\    \                /::\    \                /::\    \                /::\    \                /::\    \
//     *        /::::\    \              /::::\    \              /::::\    \              /::::\    \              /::::\    \
//     *       /::::::\    \            /::::::\    \            /::::::\    \            /::::::\    \            /::::::\    \
//     *      /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \
//     *     /:::/__\:::\    \        /:::/__\:::\    \        /:::/__\:::\    \        /:::/  \:::\    \        /:::/__\:::\    \
//     *    /::::\   \:::\    \      /::::\   \:::\    \      /::::\   \:::\    \      /:::/    \:::\    \      /::::\   \:::\    \
//     *   /::::::\   \:::\    \    /::::::\   \:::\    \    /::::::\   \:::\    \    /:::/    / \:::\    \    /::::::\   \:::\    \
//     *  /:::/\:::\   \:::\____\  /:::/\:::\   \:::\    \  /:::/\:::\   \:::\    \  /:::/    /   \:::\    \  /:::/\:::\   \:::\    \
//     * /:::/  \:::\   \:::|    |/:::/__\:::\   \:::\____\/:::/  \:::\   \:::\____\/:::/____/     \:::\____\/:::/__\:::\   \:::\____\
//     * \::/    \:::\  /:::|____|\:::\   \:::\   \::/    /\::/    \:::\  /:::/    /\:::\    \      \::/    /\:::\   \:::\   \::/    /
//     *  \/_____/\:::\/:::/    /  \:::\   \:::\   \/____/  \/____/ \:::\/:::/    /  \:::\    \      \/____/  \:::\   \:::\   \/____/
//     *           \::::::/    /    \:::\   \:::\    \               \::::::/    /    \:::\    \               \:::\   \:::\    \
//     *            \::::/    /      \:::\   \:::\____\               \::::/    /      \:::\    \               \:::\   \:::\____\
//     *             \::/____/        \:::\   \::/    /               /:::/    /        \:::\    \               \:::\   \::/    /
//     *              ~~               \:::\   \/____/               /:::/    /          \:::\    \               \:::\   \/____/
//     *                                \:::\    \                  /:::/    /            \:::\    \               \:::\    \
//     *                                 \:::\____\                /:::/    /              \:::\____\               \:::\____\
//     *                                  \::/    /                \::/    /                \::/    /                \::/    /
//     *                                   \/____/                  \/____/                  \/____/                  \/____/
//     *
//     * @param speed
//     * @param leftInches
//     * @param rightInches
//     * @param timeoutS
//     */
//    public void encoderDrive(double speed,
//                             double leftInches, double rightInches,
//                             double timeoutS) {
//        int newLeftTarget;
//        int newRightTarget;
//
//        // Ensure that the opmode is still active
//        if (opModeIsActive()) {
//
//            // Determine new target position, and pass to motor controller
//            newLeftTarget = goat.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
//            newRightTarget = goat.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
//            goat.leftDrive.setTargetPosition(newLeftTarget);
//            goat.rightDrive.setTargetPosition(newRightTarget);
//
//            // Turn On RUN_TO_POSITION
//            goat.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            goat.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            // reset the timeout time and start motion.
//            runtime.reset();
//            goat.leftDrive.setPower(Math.abs(speed));
//            goat.rightDrive.setPower(Math.abs(speed));
//
//            // keep looping while we are still active, and there is time left, and both motors are running.
//            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
//            // its target position, the motion will stop.  This is "safer" in the event that the goat will
//            // always end the motion as soon as possible.
//            // However, if you require that BOTH motors have finished their moves before the goat continues
//            // onto the next step, use (isBusy() || isBusy()) in the loop test.
//            while (opModeIsActive() &&
//                   (runtime.seconds() < timeoutS) &&
//                   (goat.leftDrive.isBusy() && goat.rightDrive.isBusy())) {
//
//                // Display it for the driver.
//                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
//                telemetry.addData("Path2",  "Running at %7d :%7d",
//                                            goat.leftDrive.getCurrentPosition(),
//                                            goat.rightDrive.getCurrentPosition());
//                telemetry.update();
//            }
//
//            // Stop all motion;
//            goat.leftDrive.setPower(0);
//            goat.rightDrive.setPower(0);
//
//            // Turn off RUN_TO_POSITION
//            goat.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            goat.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//            //  sleep(250);   // optional pause after each move
//        }
//    }
//
//    private void mechFwdRev(double distance, double speed, double timeout) {
//        int newLFTarget;
//        int newLRTarget;
//        int newRFTarget;
//        int newRRTarget;
//
//        if (opModeIsActive()) {
//
//            // Determine new target position, and pass to motor controller
//            newLFTarget = goat.leftFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
//            newLRTarget = goat.leftRear.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
//            newRFTarget = goat.rightFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
//            newRRTarget = goat.rightRear.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
//
//            goat.leftFront.setTargetPosition(newLFTarget);
//            goat.leftRear.setTargetPosition(newLRTarget);
//            goat.rightFront.setTargetPosition(newRFTarget);
//            goat.rightRear.setTargetPosition(newRRTarget);
//
//            // Turn On RUN_TO_POSITION
//            goat.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            goat.leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            goat.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            goat.rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            // reset the timeout time and start motion.
//            runtime.reset();
//            goat.leftFront.setPower(Math.abs(speed));
//            goat.leftRear.setPower(Math.abs(speed));
//            goat.rightFront.setPower(Math.abs(speed));
//            goat.rightRear.setPower(Math.abs(speed));
//
//            // keep looping while we are still active, and there is time left, and both motors are running.
//            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
//            // its target position, the motion will stop.  This is "safer" in the event that the goat will
//            // always end the motion as soon as possible.
//            // However, if you require that BOTH motors have finished their moves before the goat continues
//            // onto the next step, use (isBusy() || isBusy()) in the loop test.
//            while (opModeIsActive() &&
//                    (runtime.seconds() < timeout) &&
//                    (goat.leftFront.isBusy() && goat.leftRear.isBusy() && goat.rightFront.isBusy()
//                            && goat.rightRear.isBusy())) {
//
//                // Display it for the driver.
//                telemetry.addData("Path1",  "Running to %2d :%2d :%2d :%2d",
//                        newLFTarget, newLRTarget, newRFTarget, newRRTarget);
//                telemetry.addData("Path2",  "Running at %2d :%2d :%2d :%2d",
//                        goat.leftFront.getCurrentPosition(),
//                        goat.leftRear.getCurrentPosition(),
//                        goat.rightFront.getCurrentPosition(),
//                        goat.rightRear.getCurrentPosition());
//                telemetry.update();
//            }
//
//            // Stop all motion;
//            goat.leftFront.setPower(0);
//            goat.leftRear.setPower(0);
//            goat.rightFront.setPower(0);
//            goat.rightRear.setPower(0);
//
//            // Turn off RUN_TO_POSITION
//            goat.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            goat.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            goat.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            goat.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//            //  sleep(250);   // optional pause after each move
//        }
//    }
//}
