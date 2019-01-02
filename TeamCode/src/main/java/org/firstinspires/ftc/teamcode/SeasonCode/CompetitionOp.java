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
        robot.flipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (opModeIsActive()) {



            //print encoder values to driver station
            robot.getEncoderValues();
            telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
            telemetry.addData("Vertical Lift Pulses", robot.hangLift.getCurrentPosition());
            telemetry.addData("flipper pulses", robot.flipper.getCurrentPosition());
            telemetry.addData("Distnace: ", robot.distanceSensor.getDistance(DistanceUnit.INCH));
            telemetry.addData("Pk value:", robot.Pk);
            telemetry.update();

            //Reset encoder values by pressing the y button on gamepad 1
            /*if(gamepad1.y)
            {
                robot.verticalLift.setPower(1.00);

                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }*/

            //recieve joystick values from controllers

            getJoyVals();

            /*if (gamepad1.y) {
                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }*/

            // assign the power values to the motors
            robot.frontRight.setPower(robot.FRPower);
            robot.frontLeft.setPower(robot.FLPower);
            robot.backLeft.setPower(robot.BLPower);
            robot.backRight.setPower(robot.BRPower);


            //Preset for lift moving up
            if (gamepad2.y)
            {
                    robot.liftUp();

            }

            if (robot.hangLift.getCurrentPosition() < -8000 && robot.liftIsMovingUp == true)
            {
                robot.stopLift();
                robot.liftIsUp = true;
                robot.resetAndRunWithoutEncoders();
            }

            if (gamepad2.a)
            {
                robot.liftDown();
            }

            else if (!gamepad2.a && !robot.liftIsMovingUp)
            {
                robot.stopLift();

            }

            if (!robot.digitalTouch.getState())
            {
                robot.liftIsUp = false;
                robot.resetAndRunWithoutEncoders();
            }

            if(gamepad2.right_bumper)
            {

                robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            if(gamepad2.left_bumper)
            {
                robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }

            if(gamepad2.dpad_up)
            {
                robot.setTarget(1200);
                robot.flipArm("up");
            }

            if(gamepad2.dpad_down)
            {
                robot.setTarget(0);
                robot.flipArm("down");
                robot.flipArmIsMovingDown = true;
            }

            if(robot.flipArmIsMovingUp || robot.flipArmIsMovingDown)
            {
                robot.adjustPowerValue();
            }

            if(robot.flipper.getCurrentPosition() >  robot.target && robot.flipArmIsMovingUp)
            {
                robot.flipper.setPower(-0.05);
                robot.flipper2.setPower(0.05);
                robot.flipArmIsMovingUp = false;
            }

            if(robot.flipper.getCurrentPosition() < robot.target && robot.flipArmIsMovingDown)
            {
                robot.flipper.setPower(0.00);
                robot.flipper2.setPower(0.00);
                robot.flipArmIsMovingDown = false;
            }

            /*if(gamepad2.x)
            {
                robot.Pk = robot.Pk + 0.0005;
            }

            if(gamepad2.b)
            {
                robot.Pk = robot.Pk -0.0005;
            }*/








            //Spin Collector
             /*   if(gamepad2.right_trigger > 0.05)
                {
                    robot.spinCollector(-gamepad2.right_trigger);
                }

                else if(gamepad2.left_trigger > 0.05)
                {
                    robot.spinCollector(gamepad2.left_trigger);
                }
                else
                {
                    robot.stopCollector();
                }

                if(gamepad1.x)
                {
                    robot.markerUP();
                }
                if(gamepad1.b)
                {
                    robot.markerDown();
                }
            }*/

            /*if(gamepad2.dpad_up)
             {
                 robot.flipper.setPower(0.45);
                 robot.flipper2.setPower(-0.45);
             }
             else if(gamepad2.dpad_down)
             {
                 robot.flipper.setPower(-0.30);
                 robot.flipper2.setPower(0.30);
             }
             else
             {
                 robot.flipper.setPower(0.00);
                 robot.flipper2.setPower(0.00);


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

