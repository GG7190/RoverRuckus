package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "TestCollector", group = "Autonomous")
public class TestCollectorOnly extends LinearOpMode
{


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);


        //Wait for drivers to press play>>
        robot.shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.shoulder1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
