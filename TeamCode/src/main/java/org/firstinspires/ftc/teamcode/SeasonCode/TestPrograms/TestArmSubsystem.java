package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@TeleOp(name = "TestArmSubsystem", group = "TeleOP")
public class TestArmSubsystem extends LinearOpMode
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
            //Shoulder Down
            if(gamepad1.right_stick_y > 0.1 )
            {
                robot.shoulder1.setPower(-gamepad1.right_stick_y * 0.25);
                robot.shoulder2.setPower(gamepad1.right_stick_y * 0.25);
            }
            //Shoulder Up
            else if (gamepad1.right_stick_y < -0.1)
            {
                telemetry.addData("up", robot.shoulder1.getCurrentPosition());
                telemetry.update();
                robot.shoulder1.setPower(-gamepad1.right_stick_y * 0.5);
                robot.shoulder2.setPower(gamepad1.right_stick_y * 0.5);
            }
            else
            {
                robot.shoulder1.setPower(0.05);
                robot.shoulder2.setPower(-0.05);
            }

            //Move Extender
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

            telemetry.addData("wrist Position: ", robot.wristPosition);
            telemetry.update();

            //Move Wrist
            if(gamepad1.right_stick_y > 0.5 && robot.wristPosition > 0)
            {
                robot.setWristPosition(robot.wristPosition -= 0.0005);
            }
            if(gamepad1.right_stick_y < -0.5 && robot.wristPosition < 1)
            {
                robot.wrist.setPosition(robot.wristPosition += 0.0005 );
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

