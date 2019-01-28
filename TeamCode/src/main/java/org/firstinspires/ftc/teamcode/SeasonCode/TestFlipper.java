package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "TestFlipper", group = "Autonomous")
public class TestFlipper extends LinearOpMode
{


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);


        //Wait for drivers to press play>>
        robot.flipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flipper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.CP1_2_SPARKLE_1_ON_2;
        robot.blinkinLedDriver.setPattern(robot.pattern);
        robot.wristPosition = 0.75;
        robot.wrist.setPosition(robot.wristPosition);
        waitForStart();

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            //PRINTED VALUES
            telemetry.addData("flipper pulses", robot.flipper.getCurrentPosition());
            telemetry.addData("Wrist Position: ", robot.wrist.getPosition());
            telemetry.update();

            //GAMEPAD 1
           //GET JOYSTICK VALUES FROM CONTROLLER 1
            getJoyVals();
            // ASSIGN POWER VALUES TO MOTORS
            robot.frontRight.setPower(robot.FRPower);
            robot.frontLeft.setPower(robot.FLPower);
            robot.backLeft.setPower(robot.BLPower);
            robot.backRight.setPower(robot.BRPower);

            //MARKER
            //MARKER UP
            if(gamepad1.right_bumper)
            {
                robot.markerUP();
            }
            //MARKER DOWN
            if(gamepad1.left_bumper)
            {
                robot.markerDown();
            }


            //GAMEPAD 2
            //WHEN JOYSTICK IS MOVED DOWNWARD
            if(gamepad2.right_stick_y > 0.1 && robot.flipper.getCurrentPosition() > 1000)
            {
                robot.flipper.setPower(-gamepad2.right_stick_y * 0.5);
                robot.flipper2.setPower(gamepad2.right_stick_y * 0.5);
            }
            //WHEN JOYSTICK IS MOVED UPWARD
            else if (gamepad2.right_stick_y < -0.1  )
            {
                telemetry.addData("up", robot.flipper.getCurrentPosition());
                telemetry.update();
                robot.flipper.setPower(-gamepad2.right_stick_y * 0.5);
                robot.flipper2.setPower(gamepad2.right_stick_y * 0.5);
            }
            //BACK POWER WHEN ARM IS COMING DOWN LOWER THAN 1000 PULSES
            else if (gamepad2.right_stick_y > 0.1 && robot.flipper.getCurrentPosition() < 1000)
            {
                robot.flipper.setPower(0.003);
                robot.flipper2.setPower(-0.003);
            }

            //WHEN JOYSTICK IS NOT MOVED
            else
            {
                    robot.flipper.setPower(0.05);
                    robot.flipper2.setPower(-0.05);
            }

            //WRIST ADJUSTMENTS FOR X SHOULDER VALUES
            if(robot.flipper.getCurrentPosition() > 1000)
            {
                robot.wrist.setPosition(0.47);
            }
            if(robot.flipper.getCurrentPosition() > 750 && robot.flipper.getCurrentPosition() < 900)
            {
                robot.wrist.setPosition(.49);
            }

            //WRIST
            if(gamepad2.left_stick_y > 0.5 && robot.wristPosition > 0)
            {
                robot.wristPosition -= 0.005;
                robot.wrist.setPosition(robot.wristPosition);
            }
            if(gamepad2.left_stick_y < -0.5 && robot.wristPosition < 1)
            {
                robot.wristPosition += 0.005;
                robot.wrist.setPosition(robot.wristPosition);
            }

            //EXTENDER
            if(gamepad2.dpad_right)
            {
                robot.extender.setPower(0.55);
            }
            else if (gamepad2.dpad_left)
            {
                robot.extender.setPower(-0.35);
            }
            else
            {
                robot.extender.setPower(0.05);
            }

            //SPIN COLLECTOR
            if(gamepad2.right_trigger > 0.05)
            {
                robot.spinCollector("out");
            }

            else if(gamepad2.left_trigger > 0.05)
            {
                robot.spinCollector("in");
            }
            else
            {
                robot.stopCollector();
            }

            //HANG LIFT METHODS//
            //PRESET BUTTON FOR MOVING LIFT UP
            if (gamepad2.y)
            {
                robot.liftUp();

            }
            //STOP HANG LIFT IF IT HAS GONE PAST THE CORRECT ENCODER VALUE
            if (robot.hangLift.getCurrentPosition() < robot.hangLiftUp && robot.liftIsMovingUp == true)
            {
                robot.stopLift();
                robot.liftIsUp = true;
                robot.resetAndRunWithoutEncoders();
            }
            //BRING HANG LIFT DOWN MANUALLY
            if (gamepad2.a)
            {
                robot.liftDown();
            }
            //IF D_PAD IS NOT ALREADY PRESSED AND HANG LIFT IS NOT MOVING UP STOP HANG LIFT
            else if (!gamepad2.a && !robot.liftIsMovingUp)
            {
                robot.stopLift();

            }
            //IF THE TOUCH SENSOR IS  PRESSED THE HANG LIFT IS DOWN
            if (!robot.digitalTouch.getState())
            {
                robot.liftIsUp = false;
                robot.resetAndRunWithoutEncoders();
            }
            //LOCK HANG LIFT FOR FINAL HANG
            if(gamepad2.x)
            {

                robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            //UNLOCKS HANG LIFT (JUST IN CASE)
            if(gamepad2.b)
            {
                robot.hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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

        if (Math.abs(gamepad1LeftY) < robot.deadZone && Math.abs(gamepad1LeftX) < robot.deadZone && Math.abs(gamepad1RightX) < robot.deadZone) {
            robot.FLPower = 0;
            robot.FRPower = 0;
            robot.BRPower = 0;
            robot.BLPower = 0;
        }
    }
}
