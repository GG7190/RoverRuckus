package org.firstinspires.ftc.teamcode.SeasonCode.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.FtcGamepad;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@TeleOp(name = "StateOp", group = "TeleOp")
public class TeleOp_01_30_19 extends LinearOpMode
{

    GGHardware robot = new GGHardware();
    FtcGamepad ftcGamepad = new FtcGamepad();


    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.stopEverything();
        sleep(1000);


        //Wait for drivers to press play>>
        robot.shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.hangLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.shoulder1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.shoulder2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.hangLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        //waitForStart();

        while(!opModeIsActive() && !isStopRequested() )
        {
            robot.sendMessage("Waiting for start command");
        }

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            robot.sendMessage("OpMode Active");

            //GAMEPAD 1
           //GET JOYSTICK VALUES FROM CONTROLLER 1
            getJoyVals();
            // ASSIGN POWER VALUES TO MOTORS
            robot.frontRight.setPower(robot.FRPower);
            robot.frontLeft.setPower(robot.FLPower);
            robot.backLeft.setPower(robot.BLPower);
            robot.backRight.setPower(robot.BRPower);

            //Gamepad 2 Button Events
            switch(ftcGamepad.getButtons(gamepad2))
            {


                case FtcGamepad.GAMEPAD_A:
                    robot.liftDownTeleOpManual();
                    break;
                case FtcGamepad.GAMEPAD_Y:
                    robot.liftUpTeleOpAuto();
                    break;
                case FtcGamepad.GAMEPAD_DPAD_UP:
                    robot.liftUpTeleOpManual();
                    break;
                case FtcGamepad.GAMEPAD_DPAD_RIGHT:
                    robot.extender.setPower(1.0);
                    break;
                case FtcGamepad.GAMEPAD_DPAD_LEFT:
                    robot.extender.setPower(-0.85);
                    break;
                case FtcGamepad.GAMEPAD_B:
                    robot.gateOpen();
                    break;
                case FtcGamepad.GAMEPAD_X:
                    robot.gateClose();
                    break;
                case FtcGamepad.GAMEPAD_RBUMPER:
                    robot.lockHangLift();
                    break;
                case FtcGamepad.GAMEPAD_LBUMPER:
                    robot.unlockHangLift();
                    break;
            }
            //Gamepad 1 Button events
            switch(ftcGamepad.getButtons(gamepad1))
            {
                case FtcGamepad.GAMEPAD_X:
                    robot.markerUP();
                    telemetry.addData("markerUpFunction: ",robot.marker.getPosition());
                    telemetry.update();
                    break;
                case FtcGamepad.GAMEPAD_B:
                    robot.markerDown();
                    break;
                case FtcGamepad.GAMEPAD_A:
                    telemetry.addData("shoulder1 pulses", robot.shoulder1.getCurrentPosition());
                    telemetry.addData("extender pulses", robot.extender.getCurrentPosition());
                    telemetry.addData("hang lift pulses", robot.hangLift.getCurrentPosition());
                    telemetry.addData("wheelfr: ", robot.frontRight.getCurrentPosition());
                    telemetry.addData("wheelfl: ", robot.frontLeft.getCurrentPosition());
                    telemetry.addData("wheelbr: ", robot.backRight.getCurrentPosition());
                    telemetry.addData("wheelbl: ", robot.backLeft.getCurrentPosition());
                    telemetry.update();
                    break;
                case FtcGamepad.GAMEPAD_DPAD_DOWN:
                    robot.liftDownTeleOpAuto();
                    break;
            }
            //If cases for dpad_left and d_pad right are not met turn off extender
            if(!gamepad2.dpad_right && !gamepad2.dpad_left)
            {
                robot.extender.setPower(0.05);
            }

            if(!gamepad2.b && !gamepad2.x)
            {
                robot.gateMid();
            }

            robot.checkIfLiftDown();

            //IF D_PAD IS NOT ALREADY PRESSED AND HANG LIFT IS NOT MOVING UP STOP HANG LIFT
            if (!gamepad2.a && !robot.liftIsMovingUp)
            {
                robot.stopLift();

            }
            //IF THE TOUCH SENSOR IS  PRESSED THE HANG LIFT IS DOWN
            if (!robot.digitalTouch.getState())
            {
                robot.liftIsDown();
            }

            if(robot.collector2.getPower() > 0 || robot.collector2.getPower() < 0 )
            {
                robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_PARTY_PALETTE);
            }
            else if(robot.shoulder1.getCurrentPosition() > 1000)
            {
                robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE_GREEN);
            }

            else
            {
                robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }

            //GAMEPAD 2
            //WHEN JOYSTICK IS MOVED DOWNWARD
            if(gamepad2.right_stick_y > 0.2 )
            {
                robot.shoulder1.setPower(-gamepad2.right_stick_y * 0.25);
                robot.shoulder2.setPower(gamepad2.right_stick_y * 0.25);
            }
            //Shoulder Up
            else if (gamepad2.right_stick_y < -0.2)
            {
                telemetry.addData("up", robot.shoulder1.getCurrentPosition());
                telemetry.update();
                robot.shoulder1.setPower(-gamepad2.right_stick_y);
                robot.shoulder2.setPower(gamepad2.right_stick_y);
            }
            else
            {
                robot.shoulder1.setPower(0.05);
                robot.shoulder2.setPower(-0.05);
            }


            //SPIN COLLECTOR
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


        }

        //set all to zero
        robot.stopEverything();
        sleep(2000);

    }
    public void getJoyVals ()
    {
        float gamepad1LeftY = -gamepad1.left_stick_y;
        float gamepad1LeftX = gamepad1.left_stick_x * (float)(0.75);
        float gamepad1RightX = gamepad1.right_stick_x * (float)(0.5);

        robot.FLPower = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        robot.FRPower = (gamepad1LeftY - gamepad1LeftX - gamepad1RightX) + (float)0.05;
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
