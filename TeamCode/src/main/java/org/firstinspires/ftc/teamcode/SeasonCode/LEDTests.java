package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;


@Autonomous(name = "LEDTests", group = "Autonomous")
@Disabled
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
            robot.pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
            robot.blinkinLedDriver.setPattern(robot.pattern);
            sleep(2000);
            robot.pattern = RevBlinkinLedDriver.BlinkinPattern.GOLD;
            robot.blinkinLedDriver.setPattern(robot.pattern);
            sleep(4000);
            stop();
        }

    }

}
