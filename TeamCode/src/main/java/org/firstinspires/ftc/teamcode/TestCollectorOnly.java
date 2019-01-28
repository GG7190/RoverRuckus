package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "TestCollector", group = "Autonomous")
@Disabled
public class TestCollectorOnly extends LinearOpMode
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
        //robot.pattern = RevBlinkinLedDriver.BlinkinPattern.CP1_2_SPARKLE_1_ON_2;
        //robot.blinkinLedDriver.setPattern(robot.pattern);
        //robot.wristPosition = 0.75;
        //robot.wrist.setPosition(robot.wristPosition);
        waitForStart();

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            //Spin Collector
            if(gamepad1.right_trigger > 0.05)
            {
                robot.spinCollector("in");
            }

            else if(gamepad1.left_trigger > 0.05)
            {
                robot.spinCollector("out");
            }
            else
            {
                robot.stopCollector();
            }
        }
    }
}
