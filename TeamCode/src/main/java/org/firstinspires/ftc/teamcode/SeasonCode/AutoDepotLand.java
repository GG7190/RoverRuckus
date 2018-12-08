package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "AutoDepotLand", group = "Autonomous")
public class AutoDepotLand extends LinearOpMode {


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.initVuforia();
        robot.initTfod();
        robot.forwBackw(0.00);
        robot.tension.setPosition(1.00);

        if (robot.tfod != null)
        {
            robot.tfod.activate();
        }


        //Wait for drivers to press play>>
        waitForStart();


        //reset all encoders
        robot.resetAndRunWithoutEncoders();

        while (opModeIsActive()) {

            robot.marker.setPosition(0.50);


            String position = findGoldMineral();
            telemetry.addData("Gold Mineral is: ", position);
            telemetry.update();
            if(position == "center")
            {
                goldMineralCenter();
            }
            else if(position == "left")
            {
                goldMineralLeft();
            }
            else if (position == "right")
            {
                goldMineralRight();
            }
            sleep(3000);
            stop();

        }

    }


    public String findGoldMineral() {
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
                    sleep(2000);

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
        return "not found";
    }

    public void goldMineralCenter ()
    {
        robot.resetAndRunWithoutEncoders();

        //turn harvestor toward minerals
        robot.Drive(0.25, 8, 10, "driftL");
        robot.Drive(0.25, 16, 20, "spinL");
        //robot.collector.setPower(-1.00);
        robot.Drive(0.25, 25, 10, "forward");
        sleep(2000);
        //robot.collector.setPower(0.00);
        //robot.Drive(0.25, 10, 16, "spinR");
        //robot.Drive(0.25, 10, 10,"backward");
        /*robot.Drive(0.25, 5, 10, "backward");
        //Drive Forward to left mineral
        //robot.Drive(0.25, 40, 10, "driftL");
        robot.Drive(1.0, 72, 10, "driftL");
        robot.Drive(0.25,3, 10, "spinR" );
        robot.Drive(0.25, 30,10,"backward");
        robot.marker.setPosition(1.00);
        //robot.Drive(1.00, 75, 10, "forward");*/

    }

    public void goldMineralRight ()
    {
        robot.resetAndRunWithoutEncoders();

        robot.Drive(0.25, 8, 10, "driftL");
        robot.Drive(0.25, 10, 20, "spinL");
        //robot.collector.setPower(-1.00);
        robot.Drive(0.25, 32, 10, "forward");
        sleep(2000);
        //robot.collector.setPower(0.00);
        /*robot.Drive(0.25, 22, 10, "backward");
        robot.Drive(0.5,60, 10, "driftL");
        robot.Drive(0.25,2, 10, "spinR" );
        robot.Drive(0.25, 30,10,"backward");
        robot.marker.setPosition(1.00);
        //robot.Drive(1.00, 75, 10, "forward"*/

    }

    public void goldMineralLeft ()
    {
        robot.Drive(0.25, 8, 10, "driftL");
        robot.Drive(0.25, 24, 20, "spinL");
        //robot.collector.setPower(-1.00);
        robot.Drive(0.25, 35, 10, "forward");
        sleep(2000);
        //robot.collector.setPower(0.00);
        /*robot.Drive(0.25, 15, 10, "backward");
        // spin left toward saftey zone
        robot.Drive(0.25, 4, 10, "spinR");
        robot.Drive(0.5, 50, 10, "driftL");
        robot.Drive(0.25,3, 10, "spinR" );
        robot.Drive(0.25, 30,10,"backward");
        robot.marker.setPosition(1.00);
        //robot.Drive(1.00, 75, 10, "forward");*/
    }

    public void dumpMarker ()
    {
        //drive forward toward safe zone
        robot.Drive(0.25, 12, 10, "forward");
        //spin to be in line with the zone
        robot.Drive(0.25, 6, 10, "spin");
        //drive forward into safety zone
        robot.Drive(0.25, 12, 10, "forward");
        //spin so hopper faces safety zone
        robot.Drive(0.25, 16, 10, "spinR");


        //extend harvestor
        //robot.hortizontalR.setPower(0.25);


        sleep(700);
        //dump marker
        robot.dumper.setPosition(0.7);
        sleep(200);
        //dumper back in starting position
        robot.dumper.setPosition(0.12);
        robot.Drive(0.25, 36, 15, "forward");
    }
}

