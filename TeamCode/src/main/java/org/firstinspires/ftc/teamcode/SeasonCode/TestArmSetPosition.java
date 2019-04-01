package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "TestArmSetPosition", group = "TeleOP")
    public class TestArmSetPosition extends LinearOpMode
    {


        GGHardware robot = new GGHardware();

        @Override


        public void runOpMode()
        {

            GGParameters ggparameters = new GGParameters(this);
            robot.init(ggparameters);
            robot.shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.shoulder1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            boolean keyPressed = false;
            boolean keyProcessing = false;
            boolean keyPressed2 = false;
            boolean keyProcessing2 = false;

            waitForStart();
            MyThreads t1 = new MyThreads(robot);
            t1.start();
            LeadThread t2 = new LeadThread(robot);
            t2.start();

            while(!opModeIsActive())
            {
                robot.collector1.setPower(0.00);
                robot.collector2.setPower(0.00);
                robot.forwBackw(0.0);
                robot.shoulder1.setPower(0.0);
                robot.shoulder2.setPower(0.0);
            }
            while (opModeIsActive())
            {
                //Up
                if (gamepad1.y)
                {
                    keyPressed = true;

                }
                else
                {
                    keyPressed = false;
                }
                if(keyPressed)
                {
                    if(!keyProcessing)
                    {
                        robot.armPosition =  robot.armPosition + 25;
                        keyProcessing = true;
                    }
                }
                else
                {
                    keyProcessing = false;
                }

                //Down
                if (gamepad1.a)
                {
                    keyPressed2 = true;
                }
                else
                {
                    keyPressed2 = false;
                }
                if(keyPressed2)
                {
                    if(!keyProcessing2)
                    {
                        robot.armPosition =  robot.armPosition - 2;
                        keyProcessing2 = true;
                    }
                }
                else
                {
                    keyProcessing2 = false;
                }
                /*if(gamepad1.right_stick_x > 0.2 || gamepad1.right_stick_x < -0.2)
                {
                    robot.armPosition = robot.armPosition + (gamepad1.right_stick_x * 0.5);
                }*/
                if(gamepad1.x)
                {
                    robot.sendMessage("Current Position: " + robot.shoulder1.getCurrentPosition() + "Joystick: " + gamepad1.right_stick_x);

                }
                if(gamepad1.b)
                {
                    robot.armPosition = 470;
                }

                if(gamepad1.dpad_up)
                {
                    robot.upDownAction = 1;
                }
                if(gamepad1.dpad_down)
                {
                    robot.upDownAction = -1;
                }
            }


        }
    }
