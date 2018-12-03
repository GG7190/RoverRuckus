package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

@Autonomous(name = "AutoCrater", group = "Autonomous")
public class AutoCrater extends LinearOpMode {


    GGHardware robot = new GGHardware();
    @Override






    public void runOpMode() {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        robot.initVuforia();
        robot.initTfod();

        //Wait for drivers to press play>>
        waitForStart();

        //reset all encoders
        robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
           /* if(!robot.digitalTouch.getState())
            {
                while (robot.verticalLift.getCurrentPosition() > -4100) {
                    robot.verticalLift.setPower(-1.00);
                }
                robot.verticalLift.setPower(-0.07);
                robot.Drive(0.25, 2, 8, "driftL");
            }

            while (robot.digitalTouch.getState())
            {
                robot.verticalLift.setPower(1.00);
            }
            robot.verticalLift.setPower(-0.07);*/

            robot.Drive(0.25, 8, 8, "spinR");
            if (robot.tfod != null) {
                robot.tfod.activate();
            }

            while(true)
            {
                robot.driftLeft(0.25);

                List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
                if(updatedRecognitions == null)
                {
                    continue;
                }
                else
                {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.update();
                    if (updatedRecognitions.size() != 3) {
                        robot.driftLeft(0.25);
                    } else {
                        robot.forwBackw(0.00);
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(robot.LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1)
                        {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X)
                            {
                                telemetry.addData("Gold Mineral Position", "Left");
                                //gold mineral on the left call left collection function
                                goldMineralLeft();

                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X)
                            {
                                telemetry.addData("Gold Mineral Position", "Right");
                                //gold mineral on the right call right collection function
                                goldMineralRight();

                            } else
                            {
                                telemetry.addData("Gold Mineral Position", "Center");
                                //gold mineral on the right call right collection function
                                goldMineralCenter();
                            }
                            telemetry.update();
                        }
                        break;
                    }
                }

            }

            dumpMarker();




            stop();
        }


    }

    public void goldMineralCenter()
    {
        robot.resetAndRunWithoutEncoders();

        //turn harvestor toward minerals
        robot.Drive(0.25, 8, 10, "spinL");
        //drive forward towards blocks with harvestor spinning inward
        robot.Drive(0.25, 8,10, "forward");
       /* while(double targetDistance = robot.getEncoderValues() < 8)
        {

        }*/
        //drive backward away from minerals
        robot.Drive(0.25, 8, 10, "backward");
        // spin left toward saftey zone
        robot.Drive(0.25, 8, 10, "spinL");
        //Drive Forward to left mineral
        robot.Drive(0.25,4,10, "forward");

    }

    public void goldMineralRight()
    {
        robot.resetAndRunWithoutEncoders();

        //turn harvestor toward minerals
        robot.Drive(0.25, 8, 10, "spinL");
        //drift right towards gold mineral
        robot.Drive(0.25, 8, 10, "driftR");
        //drive forward towards blocks with harvestor spinning inward
        robot.Drive(0.25, 8,10, "forward");
        //drive backward away from minerals
        robot.Drive(0.25, 8, 10, "backward");
        // spin left toward saftey zone
        robot.Drive(0.25, 8, 10, "spinL");
        //Drive Forward to left mineral
        robot.Drive(0.25,8,10, "forward");

    }

    public void goldMineralLeft()
    {
        //turn harvestor toward minerals
        robot.Drive(0.25, 8, 10, "spinL");
        //drift right towards gold mineral
        robot.Drive(0.25, 8, 10, "driftL");
        //drive forward towards blocks with harvestor spinning inward
        robot.Drive(0.25, 8,10, "forward");
        //drive backward away from minerals
        robot.Drive(0.25, 8, 10, "backward");
        // spin left toward saftey zone
        robot.Drive(0.25, 8, 10, "spinL");
    }

    public void dumpMarker()
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
