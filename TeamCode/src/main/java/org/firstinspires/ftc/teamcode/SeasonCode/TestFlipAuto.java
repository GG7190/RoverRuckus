package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "TestFlipAuto", group = "Autonomous")
public class TestFlipAuto extends LinearOpMode
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
            if(gamepad2.y)
            {
                robot.flipArm(robot.p4);
                robot.wristPosition = 0.15;
                robot.wrist.setPosition(robot.wristPosition);
            }
            if(gamepad2.a)
            {
                robot.flipArm(robot.p1);
                robot.wristPosition = 0.75;
                robot.wrist.setPosition(robot.wristPosition);
            }
            if(gamepad2.b)
            {
                robot.flipArm(robot.p2);
                robot.wristPosition = 0.750;
                robot.wrist.setPosition(robot.wristPosition);

            }
            if(gamepad2.x)
            {
                robot.flipArm(robot.p3);
                robot.wristPosition = 0.6;
                robot.wrist.setPosition(robot.wristPosition);
            }

            if(robot.movingTopP4 || robot.movingToP3 || robot.movingToP2 || robot.movingToP1)
            {
                robot.adjustPowerValue();
            }


            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP1)
            {
                robot.flipper.setPower(0.00);
                robot.flipper2.setPower(0.00);
                robot.movingToP1 = false;
            }
            if(robot.flipper.getCurrentPosition() <  robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP2)
            {
                robot.flipper.setPower(-0.35);
                robot.flipper2.setPower(0.35);
                robot.movingToP2 = false;
            }
            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingToP3)
            {
                robot.flipper.setPower(-0.35);
                robot.flipper2.setPower(0.35);
                robot.movingToP3 = false;
            }

            if(robot.flipper.getCurrentPosition() < robot.targetHigh && robot.flipper.getCurrentPosition() > robot.targetLow && robot.movingTopP4)
            {
                robot.flipper.setPower(0.00);
                robot.flipper2.setPower(0.00);
                robot.movingTopP4 = false;
            }



        }
    }
}

