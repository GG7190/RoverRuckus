package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "TestWristManual", group = "Autonomous")
public class TestTurnManual extends LinearOpMode
{


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);

        //Wait for drivers to press play>>
        waitForStart();

        robot.wristPosition = 0.35;
        robot.wrist.setPosition(0.35);


        while (opModeIsActive())
        {
           telemetry.addData("wrist Position: ", robot.wristPosition);
           telemetry.update();

            if(gamepad2.right_stick_y > 0.5 && robot.wristPosition > 0)
            {
                robot.wristPosition -= 0.0005;
                robot.wrist.setPosition(robot.wristPosition);
                //sleep(250);
            }
            if(gamepad2.right_stick_y < -0.5 && robot.wristPosition < 1)
            {
                robot.wristPosition += 0.0005;
                robot.wrist.setPosition(robot.wristPosition);
                //sleep(250);
            }


        }
    }
}
