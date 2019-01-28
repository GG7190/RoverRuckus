package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

    @Autonomous(name = "TestFlipper2", group = "Autonomous")
    @Disabled
    public class TestFlipper2 extends LinearOpMode
    {



        GGHardware robot = new GGHardware();

        @Override


        public void runOpMode()
        {

            GGParameters ggparameters = new GGParameters(this);
            robot.init(ggparameters);


            //Wait for drivers to press play>>
            //robot.flipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //robot.flipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.pattern = RevBlinkinLedDriver.BlinkinPattern.CP1_2_SPARKLE_1_ON_2;
            robot.blinkinLedDriver.setPattern(robot.pattern);
            robot.wristPosition = 0.75;
            robot.wrist.setPosition(robot.wristPosition);
            waitForStart();

            //reset all encoders
            //robot.resetAndRunWithoutEncoders();

            while (opModeIsActive())
            {
                telemetry.addData("flipper pulses", robot.flipper.getCurrentPosition());
                telemetry.addData("Wrist Position: ", robot.wrist.getPosition());
                telemetry.update();
                //Down
                if(gamepad1.right_stick_y > 0.1)
                {
                    robot.flipper.setPower(-gamepad1.right_stick_y * 0.05);
                    robot.flipper2.setPower(gamepad1.right_stick_y * 0.05);
                }
                //UP
                else if (gamepad1.right_stick_y < -0.1)
                {
                    robot.flipper.setPower(-gamepad1.right_stick_y * 0.5);
                    robot.flipper2.setPower(gamepad1.right_stick_y * 0.5);
                }
                else
                {
                    robot.flipper.setPower(0.05);
                    robot.flipper2.setPower(-0.05);
                }

                if(gamepad1.left_stick_y > 0.5 && robot.wristPosition > 0)
                {
                    robot.wristPosition -= 0.0005;
                    robot.wrist.setPosition(robot.wristPosition);
                }
                if(gamepad1.left_stick_y < -0.5 && robot.wristPosition < 1)
                {
                    robot.wristPosition += 0.0005;
                    robot.wrist.setPosition(robot.wristPosition);
                }

                if(gamepad1.dpad_right)
                {
                    robot.extender.setPower(0.55);
                }
                else if (gamepad1.dpad_left)
                {
                    robot.extender.setPower(-0.35);
                }
                else
                {
                    robot.extender.setPower(0.05);
                }
            }
        }

    }
