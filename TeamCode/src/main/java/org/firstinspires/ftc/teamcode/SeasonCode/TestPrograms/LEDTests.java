package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;


@Autonomous(name = "LEDTests", group = "Autonomous")

public class LEDTests extends LinearOpMode
{
    GGHardware robot = new GGHardware();



    @Override




    public void runOpMode()
    {
        GGParameters ggparameters = new GGParameters(this);
        robot.initLED(ggparameters);

        //Wait for drivers to press play>>
        waitForStart();

        while (opModeIsActive())
        {
            robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            sleep(2000);
            robot.setLEDPattern(RevBlinkinLedDriver.BlinkinPattern.GOLD);
            sleep(4000);
            stop();
        }

    }

}
