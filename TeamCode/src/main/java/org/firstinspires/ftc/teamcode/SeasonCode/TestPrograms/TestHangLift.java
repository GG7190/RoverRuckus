package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "TestHangLift", group = "Autonomous")
public class TestHangLift extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);

        waitForStart();

        while (opModeIsActive())
        {
           if(gamepad1.y)
           {
               robot.hangLift.setPower(-1.00);
           }
           else if(gamepad1.a)
           {
               robot.hangLift.setPower(1.00);
           }
           else
           {
               robot.hangLift.setPower(0);
           }
        }

    }
}

