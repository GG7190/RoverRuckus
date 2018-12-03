package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;


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

    public DcMotor frontLeft, frontRight, backLeft, backRight, verticalLift,collector, hangLift;
    public Servo dumper, marker, tension;
    public DigitalChannel digitalTouch;
    //public DistanceSensor distanceSensor;
    public final double deadZone = 0.15;
    public float FRPower, FLPower, BRPower, BLPower;
    public double averageEncoderValue,currentDistance;
    boolean liftIsMovingUp = false;
    boolean liftIsUp = false;
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;
    public final String VUFORIA_KEY = " AelWwd//////AAAAGQDnHa68TEwbisDdlvJmnylYK2LsElZD9aL1bZpHc317BsOaJFu+XfN336gDBGhS+K1tbBSgoRSbghMFHhYrhwLv7QAm+cSJ1QdV/sWH4/j59cSO0Pc8XV0/TgSazwzWu3PZ+jJnas3IBOcFoI/s9GCDVUTM0GdIr1toNadpNn/MVGjFzD/unzP1A5OSlQpn3/hS33JyaLlWghEYjPoTV3qPI8mNhKry/pnPJm80Mu0a6V0kQBKKW8fSaApkYfzOtgCjCyGGDuYKAN7W/teQVcYuQHRsTzfW6i9YxfxvPwCnUu8/fVNDDppDjOyGjTPWIebISx9yJ1LvHYfHkvTvKXcW587pSW/aiDqThJH3HVh5";
    public final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public final String LABEL_SILVER_MINERAL = "Silver Mineral";
    BNO055IMU imu;
    Orientation lastAngles;
    Acceleration gravity;
    double globalAngle, power = .30, correction;


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
        hangLift = hardwareMap.get(DcMotor.class, "hl");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "ts");
        //distanceSensor = hardwareMap.get(DistanceSensor.class, "ds");
        imu = hardwareMap.get(BNO055IMU.class, "imu");




    }

    public void initDemo(GGParameters parameters)
    {
        hardwareMap = parameters.BaseOpMode.hardwareMap;
        _parameters = parameters;
        BaseOpMode = parameters.BaseOpMode;

        //Four wheels
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
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

    public void initializeIMU()
    {
        BNO055IMU.Parameters IMUParameters = new BNO055IMU.Parameters();
        IMUParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        IMUParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUParameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        IMUParameters.loggingEnabled      = true;
        IMUParameters.loggingTag          = "IMU";
        IMUParameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(IMUParameters);

    }

    public void getIMUValues()
    {
        lastAngles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity  = imu.getGravity();
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

    ////Drive Methods/////////////////
    public void forwBackw(double speed)
    {
        frontRight.setPower(speed);
        frontLeft.setPower(-speed);
        backRight.setPower(speed);
        backLeft.setPower(-speed);
    }

    public void driftRight(double speed)
    {
        frontRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
    }

    public void driftLeft(double speed)
    {
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(-speed);
        backLeft.setPower(-speed);
    }

    public void spinRight(double speed)
    {
        frontRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backRight.setPower(-speed);
        backLeft.setPower(-speed);
    }

    public void spinLeft(double speed)
    {
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
    }
    //////////////////////////

    ////Collector Methods//////////
    public void spinCollector(double speed)
    {
        collector.setPower(speed);
    }

    public void stopCollector()
    {
        collector.setPower(0.00);
    }

    public void collectSlow()
    {
        collector.setPower(.25);
    }

    public void collectFast()
    {
        collector.setPower(1);
    }
    //////////////////////////////


    ////Marker servo Methods//////
    public void  markerUP ()
    {
        marker.setPosition(0.00);
    }

    public void markerDown()
    {
        marker.setPosition(1.00);
    }
    //////////////////////////////

    ////Lift Up Preset///////////////
    public void liftUp()
    {
        if (!liftIsMovingUp && !liftIsUp && hangLift.getCurrentPosition() > -4100) {
            hangLift.setPower(-1.00);
            liftIsMovingUp = true;
        }
    }

    ////Stop Lift////////////////////
    public void stopLift()
    {
        hangLift.setPower(0.00);
        liftIsMovingUp = false;
        liftIsUp = true;
    }

    ////Lift Down////////////////////
    public void liftDown()
    {
        if(liftIsUp && liftIsMovingUp)
        {
            hangLift.setPower(1.00);
        }
    }

    public void Turn(double speed, double degrees, double timeouSeconds, String direction)
    {
        while(true) {
            getIMUValues();
            _parameters.BaseOpMode.telemetry.addData("Inside Method ", getAngle());
            _parameters.BaseOpMode.telemetry.update();
            while (getAngle() < degrees)
            {

                if (direction == "spinR")
                {
                    spinRight(speed);
                }
                if (direction == "spinL")
                {
                    spinLeft(speed);
                }
            }
            break;
        }
        forwBackw(0);
        resetAngle();
    }

    public void Drive(double speed, double targetDistance, double timeoutSeconds, String direction) {
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
                driftRight(speed);
            }
            else if (direction == "driftL")
            {
                driftLeft(speed);
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
                //_parameters.BaseOpMode.telemetry.addData("Path1", "Running to %7d :%7d", targetDistance, getEncoderValues());
                //_parameters.BaseOpMode.telemetry.update();
            }

            // Stop all motion;
            forwBackw(0);
            //Reset Encoders
            resetAndRunWithoutEncoders();
        }
    }


    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return Math.abs(globalAngle);
    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }


    void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //Uncomment if we are using the phone camera
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}




