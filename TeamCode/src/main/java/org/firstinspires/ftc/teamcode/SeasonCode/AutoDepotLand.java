package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "AutoDepotLand", group = "Autonomous")
public class AutoDepotLand extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

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
            //Find Location of gold Mineral
            String goldMineralLocation =  checkMinerals();

            //Print location of gold mineral
            telemetry.addData("GoldMineral", goldMineralLocation);
            telemetry.update();

            //If goldMineralLocation is not null turn the LEDs Gold
            if(goldMineralLocation != null)
            {
                robot.pattern = RevBlinkinLedDriver.BlinkinPattern.GOLD;
                robot.blinkinLedDriver.setPattern(robot.pattern);
            }

            //Initialize IMU
            robot.initializeIMU();

            //land
            robot.liftUpAuto();

            //center robot in front of lander
            centerRobot();

            //Reset angular position of robot to zero
            robot.turnTo(0.00);

            //Depending on Mineral location knock of the gold mineral
            knockOffMineral(goldMineralLocation);

            //turn lights off
            robot.pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            robot.blinkinLedDriver.setPattern(robot.pattern);

            //Stop and end program
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
        robot.Drive(0.25, 40, 8, "forward");
        placeMarker();
        //robot.Drive(0.25, 40, 8, "backward");

    }

    public void goldMineralRight()
    {
        robot.Drive(0.25, 15, 8, "driftR");
        robot.Drive(0.25, 40,8, "forward" );
        robot.Turn(0.25,35,3,"spinL");
        placeMarker();
        robot.Turn(0.5,35,3,"spinR");
        //robot.Drive(0.5, 10, 8, "backward");
        robot.Drive(0.5, 25, 8, "driftL");

    }

    public void goldMineralLeft()
    {
        robot.Drive(0.25, 15, 8, "driftL");
        robot.Drive(0.25, 40,8, "forward" );
        robot.Turn(0.25,35,3,"spinR");
        placeMarker();
        /*sleep(1000);
        robot.Turn(0.5,40,3,"spinL");
        robot.Drive(0.50, 35, 8, "backward");
        robot.Drive(0.50, 20, 8, "driftR");
        robot.Drive(0.5, 10,8,"backward");*/
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

    public void placeMarker()
    {

        robot.markerDown();

    }
}