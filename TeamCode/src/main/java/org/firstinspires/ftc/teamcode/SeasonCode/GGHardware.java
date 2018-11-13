package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;


public class GGHardware {

    GGParameters _parameters = null;
    /* Public OP Mode Members*/
    public DcMotor motor1 = null;

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    /* Local OP Mode Members*/
    public HardwareMap hardwareMap  = null;
    public LinearOpMode BaseOpMode = null;
    private ElapsedTime runtime = new ElapsedTime();

    public DcMotor frontLeft, frontRight, backLeft, backRight, verticalLift;
    public Servo dumper;
    public DigitalChannel digitalTouch;
    //public DistanceSensor distanceSensor;
    public final double deadZone = 0.3;
    public float FRPower, FLPower, BRPower, BLPower;
    public double averageEncoderValue,currentDistance;
    public boolean reachedTargetPosition;


    public GGHardware()
    {

    }

    public void init(GGParameters parameters)
    {

        hardwareMap = parameters.BaseOpMode.hardwareMap;
        _parameters = parameters;
        BaseOpMode = parameters.BaseOpMode;
        //Four wheels
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        verticalLift = hardwareMap.get(DcMotor.class, "vl");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "ts");
        //distanceSensor = hardwareMap.get(DistanceSensor.class, "ds");
        dumper = hardwareMap.get(Servo.class, "dump");

        //frontRight.setDirection(DcMotor.Direction.REVERSE);


    }

    public double getEncoderValues()
    {
       double fREncoder = Math.abs(frontRight.getCurrentPosition());
       double fLEncoder = Math.abs(frontLeft.getCurrentPosition());
       double bREncoder = Math.abs(backRight.getCurrentPosition());
       double bLEncoder = Math.abs(frontRight.getCurrentPosition());

       averageEncoderValue = (fREncoder + fLEncoder + bREncoder + bLEncoder)/4;
       return averageEncoderValue;

    }

    public void resetAndRunWithoutEncoders()
    {
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        verticalLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        verticalLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public double convertInchesToPulses(double inches)
    {
        final int PPR = 750;
        final int largeWheelDiameter = 4;
        double ppi = (int) (PPR / (largeWheelDiameter * Math.PI));
        ppi = (ppi * inches);
        return ppi;
    }

    public void forwBackw(double speed)
    {
        frontRight.setPower(speed);
        frontLeft.setPower(-speed);
        backRight.setPower(speed);
        backLeft.setPower(-speed);
    }

    public void driftRight()
    {
        frontRight.setPower(-1);
        frontLeft.setPower(1);
        backRight.setPower(1);
        backLeft.setPower(-1);
    }
    /*
     * Robot drifts to the left at a speed of 1.0
     *
     */

    public void driftLeft()
    {
        frontRight.setPower(1);
        frontLeft.setPower(-1);
        backRight.setPower(-1);
        backLeft.setPower(1);
    }
    /*
     * Robot turns to the right at a speed of 1.0
     */
    public void spinRight()
    {
        frontRight.setPower(-1);
        frontLeft.setPower(1);
        backRight.setPower(-1);
        backLeft.setPower(1);
    }

    public void spinLeft()
    {
        frontRight.setPower(1);
        frontLeft.setPower(-1);
        backRight.setPower(1);
        backLeft.setPower(-1);
    }



    public void DriveMotorUsingEncoder(double speed, double targetDistance, double timeoutSeconds, String direction) {
        // Ensure that the opmode is still active
        if (_parameters.BaseOpMode.opModeIsActive()) {
            _parameters.BaseOpMode.telemetry.addData("ENCODER VAlUE: ", getEncoderValues());
            _parameters.BaseOpMode.telemetry.update();

            //Set Target Position
            targetDistance = convertInchesToPulses(targetDistance);
            //Reset Encoders and Run Without Encoders
            resetAndRunWithoutEncoders();
            runtime.reset();
            if (direction == "forward")
            {
                forwBackw(speed);
            }
            else if (direction == "backward")
            {
                forwBackw(-speed);
            }
            else if (direction == "driftR")
            {
                driftRight();
            }
            else if (direction == "driftL")
            {
                driftLeft();
            }
            else if (direction == "spinR")
            {
                spinRight();
            }
            else
            {
                spinLeft();
            }


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (//_parameters.BaseOpMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutSeconds) &&
                            getEncoderValues() < targetDistance)
            {
                // Display it for the driver.
                _parameters.BaseOpMode.telemetry.addData("Path1", "Running to %7d :%7d", targetDistance, getEncoderValues());
                _parameters.BaseOpMode.telemetry.update();
            }

            // Stop all motion;
            forwBackw(0);
            //Reset Encoders
            resetAndRunWithoutEncoders();
        }
    }


}
