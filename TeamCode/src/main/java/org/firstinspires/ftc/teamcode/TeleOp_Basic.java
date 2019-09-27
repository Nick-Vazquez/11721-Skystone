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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
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
    private HardwareRoger roger             = new HardwareRoger();      // Use Roger's Hardware

    @Override
    public void runOpMode() {
        String direction;
        double forward;
        double side;
        double rotation;

        double frontLeftPower;
        double frontRightPower;
        double backLeftPower;
        double backRightPower;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        roger.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
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
            // TODO: Change back signs if robot moves idiotically
            backLeftPower = forward - side + rotation;
            backRightPower = forward + side - rotation;
            frontRightPower = forward - side - rotation;
            frontLeftPower = forward + side + rotation;

            if (forward > 0) {
                direction = "Forward";
            } else if (forward < 0) {
                direction = "Backward";
            } else if (side > 0) {
                direction = "Right";
            } else if (side < 0) {
                direction = "Left";
            } else {
                direction = "Stopped";
            }

            // Output the safe vales to the motor drives.
            roger.frontLeft.setPower(frontLeftPower);
            roger.frontRight.setPower(frontRightPower);
            roger.backLeft.setPower(backLeftPower);
            roger.backRight.setPower(backRightPower);

            // Send telemetry message to signify robot running;
            telemetry.addData("frontLeft",  "%.2f", frontLeftPower);
            telemetry.addData("frontRight", "%.2f", frontRightPower);
            telemetry.addData("backLeft", "%.2f", backLeftPower);
            telemetry.addData("backRight", "%.2f", backRightPower);
            telemetry.addData("Direction", direction);
            telemetry.update();
        }
    }
}
