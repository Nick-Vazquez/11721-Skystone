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

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the goat.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the goat FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot: Teleop POV", group="Pushbot")
@Disabled
public class PushbotTeleopPOV_Linear10139 extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot10139 goat = new HardwarePushbot10139();   // Use a Pushbot's hardware
    double          clawOffset      = 0;                       // Servo mid position
    final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo

    @Override
    public void runOpMode() {
        // Left and right are the values that are assigned to the
        // motors at the end of the program
        /** In this section **/
        double leftFront;
        double leftRear;
        double rightFront;
        double rightRear;
        double forward;
        double side;
        double rotation;
        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        goat.init(hardwareMap);

        // Send telemetry message to signify goat waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the goat fwd and back, the Right stick turns left and right.
            // This way it's also easy to just forward straight, or just turn.
            forward = -gamepad1.left_stick_y;
            side = gamepad1.left_stick_x;
            rotation = gamepad1. right_stick_x;

            // Combine forward and turn for blended motion.
            leftRear = forward - side + rotation;
            rightRear = forward + side - rotation;
            rightFront = forward - side - rotation;
            leftFront = forward + side + rotation;

            // Normalize the values so neither exceed +/- 1.0
            /** Nick doesn't understand this. I'll work on it in a bit **/
//            max = Math.max(Math.abs(leftRear), Math.abs(rightRear));
//            if (max > 1.0)
//            {
//                leftRear /= max;
//                rightRear /= max;
//            }

            // Output the safe vales to the motor drives.
            goat.leftFront.setPower(leftFront);
            goat.leftRear.setPower(leftRear);
            goat.rightFront.setPower(rightFront);
            goat.rightRear.setPower(rightRear);

            if (gamepad1.left_bumper && gamepad1.right_bumper) {
                // Do something if both bumpers are pressed down
            } else if (gamepad1.left_bumper) {
                // Do something if just the left bumper is pressed
            } else if (gamepad1.right_bumper) {
                // Do something if just the right bumper is pressed
            } else {
                // Do something if nothing is pressed
            }


            // Send telemetry message to signify goat running;
            telemetry.addData("left",  "%.2f", leftRear);
            telemetry.addData("right", "%.2f", rightRear);
            telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
            sleep(50);
        }
    }
}
