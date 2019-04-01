package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;
import org.firstinspires.ftc.teamcode.SeasonCode.MyThreads;
import org.firstinspires.ftc.teamcode.SeasonCode.SpeedController;

@TeleOp(name = "TestSpeedController", group = "TeleOP")
public class TestSpeedController extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        int joystickPosition = 0;
        double speed = 0.0;
        SpeedController  sController = new SpeedController();

        //Wait for drivers to press play>>
        //waitForStart();
        //MyThreads t1 = new MyThreads(robot);
        //t1.start();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
        }

        while (opModeIsActive())
        {
            if(gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0 || gamepad1.right_stick_x != 0)
            {
                if (gamepad1.left_stick_y > 0) {
                    speed = sController.setVelocity((double) gamepad1.left_stick_y);
                    robot.forwBackw(speed);

                } else if (gamepad1.left_stick_y < 0) {
                    speed = sController.setVelocity((double) gamepad1.left_stick_y);
                    robot.forwBackw(speed);

                }

                if(gamepad1.left_stick_x > 0)
                {
                    speed = sController.setVelocity((double)gamepad1.left_stick_x);
                    robot.driftRight(speed);
                }
                else if(gamepad1.left_stick_x < 0)
                {
                    speed = sController.setVelocity((double)gamepad1.left_stick_x);
                    robot.driftLeft(-speed);
                }

                if(gamepad1.right_stick_x > 0)
                {
                    speed = sController.setVelocity((double)gamepad1.right_stick_x);
                    robot.spinRight(speed);
                }
                if(gamepad1.right_stick_x < 0)
                {
                    speed = sController.setVelocity((double)gamepad1.right_stick_x);
                    robot.spinLeft(-speed);
                }
            }
            else
            {
                speed = sController.stopNow();
                robot.forwBackw(speed);
            }

            //robot.sendMessage("speed: " + speed);
            //robot.BaseOpMode.sleep(3000);

        }

    }


}
