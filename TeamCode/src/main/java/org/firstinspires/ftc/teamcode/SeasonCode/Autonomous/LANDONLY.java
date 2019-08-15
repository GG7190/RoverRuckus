package org.firstinspires.ftc.teamcode.SeasonCode.Autonomous;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "LANDONLY", group = "Autonomous")
public class LANDONLY extends LinearOpMode {
    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        //Init Everything
        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.initVuforia();
        robot.initTfod();
        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        robot.blinkinLedDriver.setPattern(robot.pattern);
        robot.hangLift.setPower(0.05);
        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        robot.blinkinLedDriver.setPattern(robot.pattern);

        //Activate Tensor Flow
        if (robot.tfod != null) {
            robot.tfod.activate();
        }

        //Wait for drivers to press play>>
        //waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            robot.sendMessage("Waiting for start command");
        }

        //reset all encoders
        robot.resetAndRunWithoutEncoders();
        robot.markerUP();

        while (opModeIsActive()) {

            robot.liftDownAuto();
            stop();
        }
    }
}