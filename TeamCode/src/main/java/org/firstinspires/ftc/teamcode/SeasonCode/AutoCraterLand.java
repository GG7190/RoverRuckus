package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
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

        robot.pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        robot.blinkinLedDriver.setPattern(robot.pattern);

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
            robot.pattern = RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_RED;
            robot.blinkinLedDriver.setPattern(robot.pattern);
            //Find Gold Mineral
            String goldMineralLocation =  checkMinerals();

            //Print Location of mineral
            telemetry.addData("GoldMineral", goldMineralLocation);
            telemetry.update();

            //Turn lights gold if a location of the gold mineral was found
            if(goldMineralLocation != null)
            {
                robot.pattern = RevBlinkinLedDriver.BlinkinPattern.GOLD;
                robot.blinkinLedDriver.setPattern(robot.pattern);
            }

            //Initialize IMU
            robot.initializeIMU();

            //land
            robot.liftUpAuto();
            telemetry.addData("liftpulses: ", robot.hangLift.getCurrentPosition());
            telemetry.update();


            //Center robot in front of lander
            centerRobot();

            //Reset robot's angular position to 0 degrees
            //robot.turnTo(0.00);

            //Run sequence to knock of mineral depending on the location of the gold
            //knockOffMineral(goldMineralLocation);

            //turn lights off
            //robot.pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            //robot.blinkinLedDriver.setPattern(robot.pattern);

            //Find wall and line up parallel to it
            //alignToWall();

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
        robot.Drive(0.25, 4, 3, "driftR");
        robot.Drive(0.25, 5, 3, "forward");
        robot.Drive(0.25, 5, 3, "driftL");
        //robot.Drive(0.35, 9, 3, "forward");
    }

    public void goldMineralCenter()
    {
        robot.Drive(0.35, 17, 8, "forward");
        //placeMarker();
        robot.Drive(0.35, 11, 8, "backward");

    }

    public void goldMineralRight()
    {
        robot.Drive(0.25, 16, 8, "driftR");
        robot.Drive(0.25, 25,8, "forward" );
        //robot.Turn(0.25,35,3,"spinL");
        //placeMarker();
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
        //robot.Turn(0.35,45,3,"spinR");
        //robot.driveUsingDistanceSensor();
    }


    public void placeMarker()
    {

        robot.markerDown();

    }
}