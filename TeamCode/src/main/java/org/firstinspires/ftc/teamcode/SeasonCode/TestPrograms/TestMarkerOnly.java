package org.firstinspires.ftc.teamcode.SeasonCode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SeasonCode.GGHardware;
import org.firstinspires.ftc.teamcode.SeasonCode.GGParameters;

@Autonomous(name = "TestMarkerAuto", group = "Autonomous")
public class TestMarkerOnly extends LinearOpMode
{


    GGHardware robot = new GGHardware();

    @Override


    public void runOpMode()
    {

        GGParameters ggparameters = new GGParameters(this);
        robot.init(ggparameters);
        //robot.initVuforia();
        //robot.initTfod();

        //Wait for drivers to press play>>
        waitForStart();

        //reset all encoders
        //robot.resetAndRunWithoutEncoders();

        while (opModeIsActive())
        {
            /*robot.initializeIMU();
            robot.Turn(0.25, 90, 5, "spinR");
            robot.Turn(0.25, 180,5,"spinL");
            sleep(2000);*/
            robot.markerUP();
            telemetry.addData("markerposition: ", robot.marker.getPosition());
            telemetry.update();
            sleep(2000);
            robot.markerDown();
            //robot.shoulder1.setPower(1.00);
            //sleep(2000);
            //robot.shoulder1.setPower(0.0);
            stop();
        }
    }
}
