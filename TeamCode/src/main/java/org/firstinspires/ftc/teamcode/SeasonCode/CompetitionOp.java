package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="CompetitionOp",group="TeleOp")

    public class CompetitionOp extends LinearOpMode {
    GGHardware robot = new GGHardware();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);

        waitForStart();

        //initialize servo after start is pressed

        robot.resetAndRunWithoutEncoders();
        robot.markerUP();
        robot.wrist.setPosition(0.75);
        robot.flipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (opModeIsActive()) {

            //print encoder values to driver station
            robot.getEncoderValues();
            telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
            telemetry.addData("Vertical Lift Pulses", robot.hangLift.getCurrentPosition());
            telemetry.addData("flipper pulses", robot.flipper.getCurrentPosition());
            telemetry.addData("Distnace: ", robot.distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.addData("Pk value:", robot.Pk);
            telemetry.addData("Wrist Position: ", robot.wrist.getPosition());
            telemetry.addData("Manual? ", robot.manual);
            telemetry.update();

            //recieve joystick values from controllers

            getJoyVals();


            // assign the power values to the motors
            robot.frontRight.setPower(robot.FRPower);
            robot.frontLeft.setPower(robot.FLPower);
            robot.backLeft.setPower(robot.BLPower);
            robot.backRight.setPower(robot.BRPower);


            //Preset for lift moving up
            if (gamepad2.dpad_up)
            {
                    robot.liftUp();

            }

            if (robot.hangLift.getCurrentPosition() < robot.hangLiftUp && robot.liftIsMovingUp == true)
            {
                robot.stopLift();
                robot.liftIsUp = true;
                robot.resetAndRunWithoutEncoders();
            }

            if (gamepad2.dpad_down)
            {
                robot.liftDown();
            }

            else if (!gamepad2.dpad_down && !robot.liftIsMovingUp)
            {
                robot.stopLift();

            }

            if (!robot.digitalTouch.getState())
            {
                robot.liftIsUp = false;
                robot.resetAndRunWithoutEncoders();
            }


            if(gamepad2.left_bumper)
            {
                robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }

            if(gamepad2.y)
            {
                robot.flipArm(robot.p4);
                robot.wristPosition = 0.15;
                robot.wrist.setPosition(robot.wristPosition);
            }
            if(gamepad2.a)
            {
                robot.flipArm(robot.p1);
                robot.wristPosition = 0.75;
                robot.wrist.setPosition(robot.wristPosition);
            }
            if(gamepad2.b)
            {
                robot.flipArm(robot.p2);
                robot.wristPosition = 0.750;
                robot.wrist.setPosition(robot.wristPosition);

            }
            if(gamepad2.x)
            {
                robot.flipArm(robot.p3);
                robot.wristPosition = 0.6;
                robot.wrist.setPosition(robot.wristPosition);
            }

            if(robot.movingTopP4 || robot.movingToP3 || robot.movingToP2 || robot.movingToP1)
            {
                robot.adjustPowerValue();
            }


            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP1)
            {
                robot.flipper.setPower(0.00);
                robot.flipper2.setPower(0.00);
                robot.movingToP1 = false;
            }
            if(robot.flipper.getCurrentPosition() <  robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP2)
            {
                robot.flipper.setPower(-0.35);
                robot.flipper2.setPower(0.35);
                robot.movingToP2 = false;
            }
            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP3)
            {
                robot.flipper.setPower(-0.35);
                robot.flipper2.setPower(0.35);
                robot.movingToP3 = false;
            }

            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingTopP4)
            {
                robot.flipper.setPower(0.00);
                robot.flipper2.setPower(0.00);
                robot.movingTopP4 = false;
            }


            //Spin Collector
               if(gamepad2.right_trigger > 0.05)
                {
                    robot.spinCollector("in");
                }

                else if(gamepad2.left_trigger > 0.05)
                {
                    robot.spinCollector("out");
                }
                else
                {
                    robot.stopCollector();
                }

                //switch between manual and automatic modes
                if(gamepad2.start)
                {
                    robot.manual = true;
                }

                if(gamepad2.back)
                {
                    robot.manual = false;
                }


                    if(gamepad2.right_stick_y > 0.5 && robot.wristPosition > 0)
                    {
                        robot.wristPosition -= 0.05;
                        robot.wrist.setPosition(robot.wristPosition);
                    }
                    if(gamepad2.right_stick_y < -0.5 && robot.wristPosition < 1)
                    {
                        robot.wristPosition += 0.05;
                        robot.wrist.setPosition(robot.wristPosition);
                    }

                    if(gamepad2.x)
                    {

                        robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    }







            //Methods for later??
            /*if(gamepad2.x)
            {
                robot.Pk = robot.Pk + 0.0005;
            }

            if(gamepad2.b)
            {
                robot.Pk = robot.Pk -0.0005;
            }*/

            /*if (gamepad1.y) {
                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }*/
            //Reset encoder values by pressing the y button on gamepad 1
            /*if(gamepad1.y)
            {
                robot.verticalLift.setPower(1.00);

                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }*/



        }
    }

            public void getJoyVals ()
            {
                float gamepad1LeftY = -gamepad1.left_stick_y;
                float gamepad1LeftX = gamepad1.left_stick_x;
                float gamepad1RightX = gamepad1.right_stick_x;

                robot.FLPower = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
                robot.FRPower = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
                robot.BRPower = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
                robot.BLPower = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

                if (Math.abs(gamepad1LeftY) < robot.deadZone && Math.abs(gamepad1LeftX) < robot.deadZone && Math.abs(gamepad1RightX) < robot.deadZone) {
                    robot.FLPower = 0;
                    robot.FRPower = 0;
                    robot.BRPower = 0;
                    robot.BLPower = 0;
                }
            }


        }

