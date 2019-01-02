package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "AutoCraterLand", group = "Autonomous")
public class AutoCraterLand extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.initVuforia();
        robot.initTfod();

        if (robot.tfod != null)
        {
            robot.tfod.activate();
        }

        //Wait for drivers to press play>>
        waitForStart();

        //reset all encoders
        robot.resetAndRunWithoutEncoders();
        robot.markerUP();

        while (opModeIsActive())
        {

            String goldMineralLocation =  checkMinerals();

            telemetry.addData("GoldMineral", goldMineralLocation);
            telemetry.update();

            robot.initializeIMU();
            //land
            robot.liftUpAuto();

            centerRobot();

            robot.turnTo(0.00);

            knockOffMineral(goldMineralLocation);

            alignToWall();


            stop();





        }

    }

    public String checkMinerals()
    {
        List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
        if (updatedRecognitions == null) {

        }
        else
        {
            telemetry.addData("# Object Detected", updatedRecognitions.size());
            telemetry.update();
            if (updatedRecognitions.size() < 2) {
                goldMineralCenter();
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
                    telemetry.addData("Silver Mineral", "right");
                    telemetry.update();
                    if(goldMineralX < silverMineral2X)
                    {
                        return "left";
                    }
                    else
                    {
                        return "center";
                    }
                } else if (silverMineral2X == -1)
                {
                    telemetry.addData("Silver Mineral", "right");

                    if(goldMineralX < silverMineral1X)
                    {
                        return "left";
                    }
                    else
                    {
                        return "center";
                    }
                }
                else
                {
                    return "right";
                }

            }
        }
        return "center";
    }

    public void centerRobot()
    {
        //center robot in front of lander
        robot.Drive(0.25, 5, 1, "driftR");
        robot.Drive(0.25, 5, 3, "forward");
        robot.Drive(0.25, 8, 3, "driftL");
        robot.Drive(0.25, 9, 3, "forward");
    }

    public void goldMineralCenter()
    {
        robot.Drive(0.25, 17, 8, "forward");
        placeMarker();
        //robot.Drive(0.25, 40, 8, "backward");

    }

    public void goldMineralRight()
    {
        robot.Drive(0.25, 16, 8, "driftR");
        robot.Drive(0.25, 25,8, "forward" );
        //robot.Turn(0.25,35,3,"spinL");
        placeMarker();
        /*robot.Drive(0.25, 10, 8, "backward");
        robot.Drive(0.25, 25, 8, "driftL");*/

    }

    public void goldMineralLeft()
    {
        robot.Drive(0.25, 17, 8, "driftL");
        robot.Drive(0.25, 15,8, "forward" );
        robot.Drive(0.50, 15, 8, "backward");
        robot.Drive(0.50, 17, 8, "driftR");
        //robot.Drive(0.5, 10,8,"backward");
    }

    public void knockOffMineral(String locationOfGold)
    {
        if(locationOfGold == "right")
        {
            goldMineralRight();
        }
        else if(locationOfGold == "left")
        {
            goldMineralLeft();
        }
        else
        {
            goldMineralCenter();
        }

    }

    public void alignToWall()
    {
        robot.Drive(0.35,40, 10, "driftL");
        robot.Turn(0.35,45,3,"spinR");
        robot.driveUsingDistanceSensor();
    }


    public void placeMarker()
    {

        robot.markerDown();

    }
}