package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

    @Autonomous(name = "TestExtenderOnly", group = "Autonomous")
    @Disabled
    public class TestExtenderOnly extends LinearOpMode
    {


        GGHardware robot = new GGHardware();

        @Override


        public void runOpMode()
        {

            GGParameters ggparameters = new GGParameters(this);
            robot.init(ggparameters);

            //Wait for drivers to press play>>
            waitForStart();


            robot.wristPosition = 0.75;
            robot.wrist.setPosition(robot.wristPosition);


            while (opModeIsActive())
            {

                   if(gamepad1.y)
                   {
                       robot.extender.setPower(0.55);
                   }
                   else if (gamepad1.a)
                   {
                       robot.extender.setPower(-0.35);
                   }
                   else
                   {
                       robot.extender.setPower(0.05);
                   }



            }
        }
    }



