package org.firstinspires.ftc.teamcode.SeasonCode.Autonomous;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "AutoCraterLand2", group = "Autonomous")
public class AutoCraterLand2 extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        //Init Everthing
        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.initVuforia();
        robot.initTfod();
        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        robot.blinkinLedDriver.setPattern(robot.pattern);
        robot.hangLift.setPower(0.05);
        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_RED;
        robot.blinkinLedDriver.setPattern(robot.pattern);

        //Init Tensor Flow
        if (robot.tfod != null)
        {
            robot.tfod.activate();
        }

        //Wait for drivers to press play>>
        //waitForStart();

        while(!opModeIsActive() && !isStopRequested() )
        {
            robot.sendMessage("Waiting for start command");
        }

        //reset all encoders
        robot.resetAndRunWithoutEncoders();
        robot.markerUP();

        while (opModeIsActive())
        {
            //Reset Encoders
            robot.extender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.shoulder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.shoulder1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.hangLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.hangLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //Find Gold Mineral//
            String goldMineralLocation =  checkMinerals();

            //Print Location of mineral
            telemetry.addData("GoldMineral", goldMineralLocation);
            telemetry.update();



            //Initialize IMU
            robot.initializeIMU();

            //Turn lights gold if a location of the gold mineral was found
            if(goldMineralLocation != null)
            {
                robot.pattern = RevBlinkinLedDriver.BlinkinPattern.GOLD;
                robot.blinkinLedDriver.setPattern(robot.pattern);
            }

            //land
            robot.shoulderUpAuto(150);
            robot.liftUpAuto();
            telemetry.addData("liftpulses: ", robot.hangLift.getCurrentPosition());
            telemetry.update();


            //Center robot in front of lander
            centerRobot();

            //Run sequence to knock of mineral depending on the location of the gold
            knockOffMineral(goldMineralLocation);

            //Find wall and line up parallel to it
            alignToWall();

            //Place marker using marker slide
            placeMarker();

            //Stop and end program
            robot.stopEverything();
            stop();



        }

    }

    public String checkMinerals()
    {
        //Decide where the gold Mineral is based on two in camera view
        List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
        if (updatedRecognitions == null) {

        }
        else
        {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            telemetry.update();
            if (updatedRecognitions.size() < 2) {
                return "center";
            }
            else {
                robot.forwBackw(0.00);
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;

                for (Recognition recognition : updatedRecognitions)
                {
                    telemetry.addData("Y value", recognition.getBottom());
                    telemetry.update();

                    if (recognition.getLabel().equals(robot.LABEL_GOLD_MINERAL))
                    {
                        goldMineralX = (int) recognition.getLeft();

                    } else if (silverMineral1X == -1)
                    {
                        silverMineral1X = (int) recognition.getLeft();
                    } else
                    {
                        silverMineral2X = (int) recognition.getLeft();
                    }
                }

                if (silverMineral1X == -1)
                {
                    telemetry.addData("Silver Mineral", "left");
                    telemetry.update();
                    if(goldMineralX < silverMineral2X)
                    {
                        return "center";
                    }
                    else
                    {
                        return "right";
                    }
                } else if (silverMineral2X == -1)
                {
                    telemetry.addData("Silver Mineral", "left");

                    if(goldMineralX < silverMineral1X)
                    {
                        return "center";
                    }
                    else
                    {
                        return "right";
                    }
                }
                else
                {
                    return "left";
                }

            }
        }
        return "center";
    }

    public void centerRobot()
    {
        //center robot in front of lander
        robot.Drive(0.5, 4, 3, "driftR");//2
        robot.Drive(0.5, 16, 3, "forward"); //16
        robot.Drive(0.5, 2, 3, "driftL");
    }

    public void collectMineralCenter()
    {
        //Knock off mineral in the center
        robot.extendOutAuto(950);
        telemetry.addData("extender pulses", robot.extender.getCurrentPosition());
        telemetry.update();
        robot.spinCollector("out");
        sleep(500);
        robot.stopCollector();
        robot.extendInAuto(0);
    }
    public void collectMineralLeft()
    {
        //Drift left and knock off left mineral
        robot.Drive(0.5, 16, 8, "driftL"); //16
        robot.extendOutAuto(950);
        telemetry.addData("extender pulses", robot.extender.getCurrentPosition());
        telemetry.update();
        robot.spinCollector("out");
        sleep(500);
        robot.stopCollector();
        robot.extendInAuto(0);
        robot.Drive(0.5, 16, 8, "driftR"); //16
    }
    public void collectMineralRight()
    {
        //Drift right and knock off right mineral
        telemetry.addData("ShoulderPulses: ", robot.shoulder1.getCurrentPosition());
        telemetry.update();
        robot.Drive(0.5, 14, 8, "driftR"); //14
        robot.extendOutAuto(950);
        robot.spinCollector("out");
        sleep(500);
        robot.stopCollector();
        robot.extendInAuto(0);
        robot.Drive(0.5, 14, 8, "driftL"); //14

    }



    public void knockOffMineral(String locationOfGold)
    {
        //Interpret what string the method locationOfGold returns based on what the camera sees
        if(locationOfGold == "right")
        {
            collectMineralRight();
        }
        else if(locationOfGold == "left")
        {
            collectMineralLeft();
        }
        else
        {
           collectMineralCenter();
        }

    }

    public void alignToWall()
    {
        //Align to be parallel with outside wall
        robot.Drive(0.55,40, 10, "driftL"); //40
        robot.Turn(0.25,45,3,"spinR");
        robot.driveUsingDistanceSensor(4,5);
    }


    public void placeMarker()
    {
        //Place marker by extending over minerals and dropping markers
        robot.DriveAdjustForAngle(0.5, 44, 10, "backward"); //44
        robot.markerDown();
        sleep(1000);
        robot.markerUP();
        robot.Drive(0.5, 60, 10, "forward"); //60
        robot.shoulderUpAuto(400);
        robot.extendOutAuto(2200);
        //robot.liftDownAuto();
        sleep(5000);

    }
}