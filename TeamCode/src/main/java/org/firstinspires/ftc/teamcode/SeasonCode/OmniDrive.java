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
            robot.dumper.setPosition(0.12);
            boolean liftIsMovingUp = false;
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

            //Manual controls for lift
            /*if(gamepad2.a)
            {
                    robot.verticalLift.setPower(1.00);
            }
            else if(gamepad2.y)
            {
                robot.verticalLift.setPower(-1.0);
            }
            else
            {
                robot.verticalLift.setPower(-0.07);
            }*/

            //Preset for lift moving up
            if(gamepad2.dpad_left )
            {
                if(!liftIsMovingUp && robot.verticalLift.getCurrentPosition() > -4100)
                {
                    robot.verticalLift.setPower(-1.00);
                    liftIsMovingUp = true;
                }
            }
            if(robot.verticalLift.getCurrentPosition() <  -4100 && liftIsMovingUp == true)
            {
               robot.verticalLift.setPower(-0.07);
               liftIsMovingUp = false;
            }

            //Preset for lift moving down
            if(gamepad2.dpad_right)
            {
                if(!liftIsMovingDown && robot.digitalTouch.getState())
                {
                    robot.verticalLift.setPower(1.00);
                    liftIsMovingDown = true;
                }
            }
            if(!robot.digitalTouch.getState() && liftIsMovingDown == true)
            {
                robot.verticalLift.setPower(-0.07);
                liftIsMovingDown = false;
                robot.resetAndRunWithoutEncoders();
            }

            //Move dumper up
            if(gamepad2.dpad_up)
            {
                robot.dumper.setPosition(0.70);
            }

            //Move dumper down
            else if(gamepad2.dpad_down)
            {
                robot.dumper.setPosition(0.12);
            }
            else
            {

            }

            /*if(gamepad2.y)
            {
                while(robot.distanceSensor.getDistance(DistanceUnit.INCH )< 14.25)
                {
                    robot.verticalLift.setPower(-0.5);
                }
                robot.verticalLift.setPower(0.0);
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

            if (Math.abs(gamepad1LeftY) < robot.deadZone && Math.abs(gamepad1LeftX) < robot.deadZone && Math.abs(gamepad1RightX) < robot.deadZone )
            {
                robot.FLPower = 0;
                robot.FRPower = 0;
                robot.BRPower = 0;
                robot.BLPower = 0;
            }
        }

    }