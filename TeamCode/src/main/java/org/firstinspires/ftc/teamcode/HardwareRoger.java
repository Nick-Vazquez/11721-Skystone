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

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is Roger.
 *
 * This hardware class assumes the following device names have been configured on roger:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Front Left  drive motor:     "frontLeft"
 * Motor channel:  Front Right drive motor:     "frontRight"
 * Motor channel:  Back  Right drive motor:     "backRight"
 * Motor channel:  Back  Left  drive motor:     "backLeft"
 */
class HardwareRoger
{
    /* Public OpMode members. */
    DcMotor frontLeft   = null;
    DcMotor frontRight  = null;
    DcMotor backLeft    = null;
    DcMotor backRight   = null;
    DcMotor foundation  = null;
    DcMotor slideRotater = null;
    DcMotor slideExtender = null;

    CRServo clawServo   = null;

    ColorSensor sensorColor = null;

    static final double CLAW_OPEN_POWER      =  1;
    static final double CLAW_CLOSE_POWER     = -1;
    static final double CLAW_STOP            = 0;

    /* local OpMode members. */
    private HardwareMap hwMap           =  null;

    /* Constructor */
    HardwareRoger(){
    }

    /* Initialize standard Hardware interfaces */
    void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontLeft   = hwMap.get(DcMotor.class, "frontLeft");
        frontRight  = hwMap.get(DcMotor.class,"frontRight");
        backLeft    = hwMap.get(DcMotor.class, "backLeft");
        backRight   = hwMap.get(DcMotor.class, "backRight");
        foundation  = hwMap.get(DcMotor.class, "foundation");
        slideRotater = hwMap.get(DcMotor.class,"slideRotater");
        slideExtender = hwMap.get(DcMotor.class, "slideExtender");

        /*
         * Set the direction of the motors so that a full forward command
         * will move the bot forward
         */
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        foundation.setDirection(DcMotor.Direction.REVERSE);
        slideRotater.setDirection(DcMotor.Direction.FORWARD);
        slideExtender.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        foundation.setPower(0);
        slideRotater.setPower(0);
        slideExtender.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        foundation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRotater.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideExtender.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        clawServo   = hwMap.get(CRServo.class, "claw");
        clawServo.setPower(CLAW_STOP);

        sensorColor = hwMap.get(ColorSensor.class, "color");
    }
 }

