package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@TeleOp(name = "TestFlipAuto", group = "TeleOP")
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
            if(gamepad1.right_stick_y > 0.1 )
            {
                robot.flipper.setPower(-gamepad1.right_stick_y * 0.25);
                robot.flipper2.setPower(gamepad1.right_stick_y * 0.25);
            }
            //UP
            else if (gamepad1.right_stick_y < -0.1)
            {
                telemetry.addData("up", robot.flipper.getCurrentPosition());
                telemetry.update();
                robot.flipper.setPower(-gamepad1.right_stick_y * 0.5);
                robot.flipper2.setPower(gamepad1.right_stick_y * 0.5);
            }
            else
            {
                robot.flipper.setPower(0.05);
                robot.flipper2.setPower(-0.05);
            }

            if(gamepad1.dpad_right)
            {
                robot.extender.setPower(0.55);
            }
            else if (gamepad1.dpad_left)
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

