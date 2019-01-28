package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

    @Autonomous(name = "TestExtender", group = "Autonomous")
    @Disabled
    public class TestExtender extends LinearOpMode
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

                telemetry.addData("wrist Position: ", robot.wristPosition);
                telemetry.update();

                if(gamepad1.right_stick_y > 0.5 && robot.wristPosition > 0)
                {
                    robot.wristPosition -= 0.0005;
                    robot.wrist.setPosition(robot.wristPosition);
                    //sleep(250);
                }
                if(gamepad1.right_stick_y < -0.5 && robot.wristPosition < 1)
                {
                    robot.wristPosition += 0.0005;
                    robot.wrist.setPosition(robot.wristPosition);
                    //sleep(250);
                }

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



