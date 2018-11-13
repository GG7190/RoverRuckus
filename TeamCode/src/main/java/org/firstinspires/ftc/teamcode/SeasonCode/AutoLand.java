package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name = "AutoLand", group = "Autonomous")
public class AutoLand extends LinearOpMode {


    GGHardware robot = new GGHardware();
    @Override
    public void runOpMode() {

    GGParameters ggparameters = new GGParameters(this);
    robot.init(ggparameters);

    ColorSensor colorSensor;

        waitForStart();

        while (opModeIsActive())
        {
            robot.DriveMotorUsingEncoder(0.25, 24.0, 15, "forward");
            stop();
        }


    }


}
