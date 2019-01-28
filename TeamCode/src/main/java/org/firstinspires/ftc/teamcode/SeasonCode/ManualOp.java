package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;


@TeleOp(name="ManualOp",group="TeleOp")
@Disabled
public class ManualOp extends LinearOpMode {
    GGHardware robot = new GGHardware();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);

        waitForStart();

        //initialize servo after start is pressed

        robot.resetAndRunWithoutEncoders();

        while (opModeIsActive()) {

            //print encoder values to driver station
            robot.getEncoderValues();
            telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
            telemetry.addData("Vertical Lift Pulses", robot.hangLift.getCurrentPosition());
            telemetry.update();

            //Reset encoder values by pressing the y button on gamepad 1
            if(gamepad1.y)
            {
                robot.verticalLift.setPower(1.00);

                robot.averageEncoderValue = 0;
                robot.resetAndRunWithoutEncoders();
                telemetry.addData("Encoder pulses: ", robot.averageEncoderValue);
                telemetry.update();
            }

            //recieve joystick values from controllers
            getJoyVals();

            if (gamepad1.y) {
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


            //Buttons to move lift up
            if (gamepad2.y)
            {
                 robot.hangLift.setPower(-1.00);

            }

            if (gamepad2.a)
            {
                robot.hangLift.setPower(1.00);
            }

            if(!gamepad2.y && ! gamepad2.a)
            {
                robot.hangLift.setPower(0.00);
            }


            if (!robot.digitalTouch.getState())
            {
                robot.hangLift.setPower(0.00);
            }


            //Spin Collector
               /*( if(gamepad2.right_trigger > 0.05)
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

                if(gamepad1.x)
                {
                    robot.markerUP();
                }
                if(gamepad1.b)
                {
                    robot.markerDown();
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

