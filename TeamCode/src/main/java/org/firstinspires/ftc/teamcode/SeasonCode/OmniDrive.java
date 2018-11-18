package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;


@TeleOp(name="OmniDrive",group="TeleOp")
    public class OmniDrive extends LinearOpMode
    {
        GGHardware robot = new GGHardware();
        private ElapsedTime  runtime= new ElapsedTime();


        @Override
        public void runOpMode() {

            GGParameters ggparameters = new GGParameters(this);
            robot.init(ggparameters);

            waitForStart();

            //initialize servo after start is pressed
            robot.dumper.setPosition(0.09);
            robot.tension.setPosition(0.00);
            boolean liftIsMovingUp = false;
            boolean liftIsUp = false;
            boolean liftIsMovingDown = false;
            robot.resetAndRunWithoutEncoders();

            while (opModeIsActive()) {

            //print encoder values to driver station
            robot.getEncoderValues();
            telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
            telemetry.addData("Vertical Lift Pulses", robot.verticalLift.getCurrentPosition());
            telemetry.update();

            //Reset encoder values by pressing the y button on gamepad 1
            if(gamepad1.y)
            {
                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }

            //recieve joystick values from controllers
            getJoyVals();

            if(gamepad1.y)
            {
                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }

            // assign the power values to the motors
            robot.frontRight.setPower(robot.FRPower);
            robot.frontLeft.setPower(robot.FLPower);
            robot.backLeft.setPower(robot.BLPower);
            robot.backRight.setPower(robot.BRPower);


            //Preset for lift moving up
            if(gamepad2.y )
            {
                if(!liftIsMovingUp && !liftIsUp && robot.verticalLift.getCurrentPosition() > -4100)
                {
                    robot.verticalLift.setPower(-1.00);
                    robot.tension.setPosition(1.00);
                    liftIsMovingUp = true;
                }
            }
            if(robot.verticalLift.getCurrentPosition() <  -4100 && liftIsMovingUp == true)
            {
               robot.verticalLift.setPower(-0.07);
               liftIsMovingUp = false;
               liftIsUp = true;
            }


            if(gamepad2.a)
            {
                if(liftIsUp && !liftIsMovingUp)
                {
                        robot.verticalLift.setPower(1.00);
                        robot.dumper.setPosition(0.09);
                        robot.tension.setPosition(0.00);
                }
            }
            else if(!gamepad2.a && !liftIsMovingUp)
            {
                robot.verticalLift.setPower(-0.07);
            }

            else
            {

            }

            if (!robot.digitalTouch.getState())
            {
                liftIsUp = false;
            }





            /*if(gamepad2.a)
            {
                robot.verticalLift.setPower(1.00);
            }
            else if ((!gamepad2.a && liftIsMovingUp ==  false) || !robot.digitalTouch.getState() )
            {
                robot.verticalLift.setPower(-0.07);
                robot.resetAndRunWithoutEncoders();
            }
            else
            {

            } */



            //Preset for lift moving down
            /*if(gamepad2.a)
            {
                if(!liftIsMovingDown && robot.digitalTouch.getState())
                {
                    robot.verticalLift.setPower(1.00);
                    liftIsMovingDown = true;
                }
            }
            /*if(robot.verticalLift.getCurrentPosition() > 100)
            {

            }
            if(!robot.digitalTouch.getState() && liftIsMovingDown == true)
            {
                robot.verticalLift.setPower(-0.07);
                liftIsMovingDown = false;
                robot.resetAndRunWithoutEncoders();
            }*/

            //Move dumper up
            if(gamepad2.b)
            {
                robot.dumper.setPosition(0.70);
            }

            //Move dumper down
            else if(gamepad2.x)
            {
                robot.dumper.setPosition(0.09);
            }
            else
            {

            }

            //run 256:1 motor to latch and deploy robot from lander
            if(gamepad2.dpad_up)
            {
                robot.hangLift.setPower(1.00);
                robot.tension.setPosition(1.00);
            }
            else if(gamepad2.dpad_down)
            {
                robot.hangLift.setPower(-1.00);
            }
            else
            {
                robot.hangLift.setPower(0.00);
            }


            //Move horizontal lift out and in
            /*if(gamepad2.b)
            {
                robot.hortizontalR.setPower(1.00);
            }
            else if(gamepad2.x)
            {
                robot.hortizontalL.setPower(1.00);;
            }
            else
            {
                robot.hortizontalL.setPower(-0.05);
                robot.hortizontalR.setPower(0.05);
            }*/

            //Spin Collector
                if(gamepad2.right_trigger > 0.05)
                {
                    robot.collector.setPower(-gamepad2.right_trigger);
                }

                else if(gamepad2.left_trigger > 0.05)
                {
                    robot.collector.setPower(gamepad2.left_trigger);
                }
                else
                {
                    robot.collector.setPower(0);
                }
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

            if (Math.abs(gamepad1LeftY) < robot.deadZone && Math.abs(gamepad1LeftX) < robot.deadZone && Math.abs(gamepad1RightX) < robot.deadZone )
            {
                robot.FLPower = 0;
                robot.FRPower = 0;
                robot.BRPower = 0;
                robot.BLPower = 0;
            }
        }




    }