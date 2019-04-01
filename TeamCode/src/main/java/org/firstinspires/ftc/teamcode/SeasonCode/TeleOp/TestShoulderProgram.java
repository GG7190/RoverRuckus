package org.firstinspires.ftc.teamcode.SeasonCode.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.FtcGamepad;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;


@TeleOp(name = "testShoulderProgram", group = "TeleOp")
public class TestShoulderProgram extends LinearOpMode {

    GGHardware robot = new GGHardware();
    FtcGamepad ftcGamepad = new FtcGamepad();


    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);


        //Wait for drivers to press play>>
        robot.shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.hangLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.shoulder1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.shoulder2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.hangLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //robot.setWristPosition(0.25);
        robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);

        //waitForStart();

        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
        }

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            if(gamepad2.right_stick_y > 0.1 && robot.shoulder1.getCurrentPosition() < robot.SHOULDERMAX)
            {
                robot.flipArmIsMovingUp = true;
                robot.shoulder1.setPower(0.5);
                robot.shoulder2.setPower(-0.5);
            }
            if(gamepad2.right_stick_y < 0.1 && gamepad2.right_stick_x > -0.1)
            {
                robot.flipArmIsMovingUp = false;
                robot.flipArmIsMovingDown = false;
            }
            if(gamepad2.right_stick_y < 0.1 && robot.shoulder1.getCurrentPosition() > robot.SHOULDERMIN)
            {
                robot.flipArmIsMovingDown = true;
                robot.shoulder1.setPower(0.25);
            }

        }
    }
}

