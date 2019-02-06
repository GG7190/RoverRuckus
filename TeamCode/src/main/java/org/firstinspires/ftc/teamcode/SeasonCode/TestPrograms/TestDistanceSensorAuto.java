package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "TestDistanceSensorAuto", group = "Autonomous")
public class TestDistanceSensorAuto extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);

        waitForStart();

        while (opModeIsActive())
        {
            robot.driveUsingDistanceSensor();
            stop();
        }

    }
}


