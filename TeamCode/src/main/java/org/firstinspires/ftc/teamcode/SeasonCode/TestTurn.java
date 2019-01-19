package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "TestWristAuto", group = "Autonomous")
public class TestTurn extends LinearOpMode
{


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        //robot.initVuforia();
        //robot.initTfod();

        //Wait for drivers to press play>>
        waitForStart();

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            /*robot.initializeIMU();
            robot.Turn(0.25, 90, 5, "spinR");
            robot.Turn(0.25, 180,5,"spinL");
            sleep(2000);*/
            robot.wrist.setPosition(0.35);
            telemetry.addData("wristposition: ", robot.wrist.getPosition());
            telemetry.update();
            sleep(50000);
            stop();
        }
    }
}
