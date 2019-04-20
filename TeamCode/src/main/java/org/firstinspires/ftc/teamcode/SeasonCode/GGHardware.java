package org.firstinspires.ftc.teamcode.SeasonCode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
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
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;


public class GGHardware {

    GGParameters _parameters = null;
    /* Public OP Mode Members*/
    public DcMotor motor1 = null;

    ColorSensor sensorColor;
    DistanceSensor distanceSensor;

    /* Local OP Mode Members*/
    public HardwareMap hardwareMap = null;
    public LinearOpMode BaseOpMode = null;
    private ElapsedTime runtime = new ElapsedTime();

    public DcMotor frontLeft, frontRight, backLeft, backRight, verticalLift, hangLift, shoulder1, shoulder2, extender;
    public Servo marker, gate;
    public CRServo collector1, collector2;
    public DigitalChannel digitalTouch;
    public RevBlinkinLedDriver blinkinLedDriver;
    public RevBlinkinLedDriver.BlinkinPattern pattern;

    public final double deadZone = 0.15;
    public float FRPower, FLPower, BRPower, BLPower;
    public double averageEncoderValue, flipperValue, eValue, armPosition;
    public double SHOULDERMAX = 1750;
    public double SHOULDERMIN = 200;
    public boolean liftIsMovingUp = false;
    public boolean liftIsUp = false;
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;
    public final String VUFORIA_KEY = "AQ2bBhr/////AAABmZ3h5slXFkFNsA2+9NNp7vgLITOJZHq695JcQC+JLV4eNMi5xm9bRVutz//N3w2HmZSHV+VH0sXNGstHLcn+8+RncGp0XNFhhj4xfRLIxpsj+nLydV/LStF4ayEFHhWeW3DCn5HZd+8utzjJXaoGAKnXIQ8w9SOviaPJBA5wXqllQNNowqyLBYMEhchuttTDQsGzw3ckVVbRC8c7q+z6M0lKqRj0CWi4EG6bFtqs96KTjBw2Aq+uemXXEsYcEfGO1lW8fkkya5HQaJJh9GNjy63vXnTmAMTBirPJFjSNb6NQm3Si92Prc/FscOML4lxxaTTwl/7Y96VH6a6FkfD9Ka19NVl61xbKI+Anycnvb+gl";
    public final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public final String LABEL_SILVER_MINERAL = "Silver Mineral";
    public final int hangLiftUp = -10700;
    public int targetHigh, targetLow, target;
    public int upDownAction = 0;
    public double Pk = 0.002;
    public boolean flipArmIsMovingUp = false;
    public boolean flipArmIsMovingDown = false;
    public boolean robotIsMoving = false;
    public boolean flipArmIsUp = false;

    public boolean movingToP1 = false;
    public boolean movingToP2 = false;
    public boolean movingToP3 = false;
    public boolean movingTopP4 = false;

    public boolean manual = false;
    final int p1 = 1200;
    final int p2 = 600;
    final int p3 = 300;
    final int p4 = 0;

    BNO055IMU imu;
    //UltrasonicSensor ultra;
    Orientation lastAngles;
    Acceleration gravity;
    double globalAngle, power = .30, correction;


    public GGHardware() {

    }

    public void init(GGParameters parameters) {

        hardwareMap = parameters.BaseOpMode.hardwareMap;
        _parameters = parameters;
        BaseOpMode = parameters.BaseOpMode;

        //Four wheels
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");

        forwBackw(0.00);

        //Other DC Motors
        hangLift = hardwareMap.get(DcMotor.class, "hl");
        shoulder1 = hardwareMap.get(DcMotor.class, "fp");
        shoulder2 = hardwareMap.get(DcMotor.class, "fp2");
        extender = hardwareMap.get(DcMotor.class, "ex");

        hangLift.setPower(0.0);
        shoulder1.setPower(0.0);
        shoulder2.setPower(0.0);
        extender.setPower(0.0);

        //Servos
        marker = hardwareMap.get(Servo.class, "mk");
        gate = hardwareMap.get(Servo.class, "gt");
        collector1 = hardwareMap.get(CRServo.class, "cl1");
        collector2 = hardwareMap.get(CRServo.class, "cl2");


        markerUP();
        gateMid();
        collector1.setPower(0.0);
        collector2.setPower(0.0);

        //Sensors
        digitalTouch = hardwareMap.get(DigitalChannel.class, "ts");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "ds");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        armPosition = 0;//More work here!!

        //LEDs
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "bk");


    }

    public void initDemo(GGParameters parameters) {
        hardwareMap = parameters.BaseOpMode.hardwareMap;
        _parameters = parameters;
        BaseOpMode = parameters.BaseOpMode;

        //Four wheels
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
    }

    public void initLED(GGParameters parameters) {
        hardwareMap = parameters.BaseOpMode.hardwareMap;
        _parameters = parameters;
        BaseOpMode = parameters.BaseOpMode;

        //LEDs
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "bk");
    }

    public double getEncoderValues() {
        double fREncoder = Math.abs(frontRight.getCurrentPosition());
        double fLEncoder = Math.abs(frontLeft.getCurrentPosition());
        double bREncoder = Math.abs(backRight.getCurrentPosition());
        double bLEncoder = Math.abs(frontRight.getCurrentPosition());

        averageEncoderValue = (fREncoder + fLEncoder + bREncoder + bLEncoder) / 4;
        return averageEncoderValue;

    }

    public void initializeIMU() {
        BNO055IMU.Parameters IMUParameters = new BNO055IMU.Parameters();
        IMUParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        IMUParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUParameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        IMUParameters.loggingEnabled = true;
        IMUParameters.loggingTag = "IMU";
        IMUParameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(IMUParameters);
        resetAngle();

    }

    public void getIMUValues() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();
    }

    public void resetAndRunWithoutEncoders() {
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //hangLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        hangLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }


    public double convertInchesToPulses(double inches) {
        final int PPR = 750;
        final int largeWheelDiameter = 4;
        double ppi = (int) (PPR / (largeWheelDiameter * Math.PI));
        ppi = (ppi * inches);
        return ppi;
    }

    ////Drive Methods/////////////////
    public void forwBackw(double speed) {
        frontRight.setPower(-speed);
        frontLeft.setPower(speed);
        backRight.setPower(-speed);
        backLeft.setPower(speed);
    }

    public void forwBackw(double fR, double fL, double bR, double bL) {
        frontRight.setPower(-fR);
        frontLeft.setPower(fL);
        backRight.setPower(-bR);
        backLeft.setPower(bL);
    }


    public void driftRight(double speed) {
        frontRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
    }

    public void driftLeft(double speed) {
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(-speed);
        backLeft.setPower(-speed);
    }

    public void spinRight(double speed) {
        frontRight.setPower(-speed);
        frontLeft.setPower(-speed);
        backRight.setPower(-speed);
        backLeft.setPower(-speed);
    }

    public void spinLeft(double speed) {
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
    }
    //////////////////////////


    ////Collector Methods//////////
    //Don't change 0.7 servo cannot handle more than that
    public void spinCollector(String direction) {
        if (direction == "in") {
            collector1.setPower(0.7);
            collector2.setPower(-0.7);
        } else {
            collector1.setPower(-0.7);
            collector2.setPower(0.7);
        }

    }

    public void stopCollector()
    {
        collector1.setPower(0.00);
        collector2.setPower(0.00);
    }





    ////Marker servo Methods//////
    public void markerUP() {
        marker.setPosition(0.07);
    }

    public void markerDown() {
        marker.setPosition(0.75);
    }
    //////////////////////////////

    ////Gate servo Methods///////
    public void gateOpen()
    {
        gate.setPosition(0.5);
    }
    public void gateMid()
    {
        gate.setPosition(.76);
    }
    public void gateClose()
    {
        gate.setPosition(1.00);
    }
    /////////////////////////////

    ////Lift Up Preset///////////////
    public void liftUpTeleOpAuto()
    {
        if (!liftIsUp && hangLift.getCurrentPosition() > hangLiftUp)
        {
            hangLift.setPower(-1.00);
            liftIsMovingUp = true;
        }

    }

    ////Lift Up Manual////////////////
    public void liftUpTeleOpManual()
    {
        if(hangLift.getCurrentPosition() > -12000)
        {
            hangLift.setPower(-1.00);
        }
    }

    public void liftDownAuto()
    {
        while(digitalTouch.getState())
        {
            hangLift.setPower(1.00);
        }
        hangLift.setPower(0.00);
    }

    ////Lift Down Preset//////////////
    public void liftDownTeleOpAuto()
    {
        if (!liftIsUp && hangLift.getCurrentPosition() > hangLiftUp)
        {
            hangLift.setPower(1.00);
            liftIsMovingUp = true;
        }

    }

    ////Lift Down////////////////////
    public void liftDownTeleOpManual()
    {
        if (digitalTouch.getState())
        {
            hangLift.setPower(1.00);

        }

    }


    public void liftUpAuto() {
        while (hangLift.getCurrentPosition() > hangLiftUp) {
            hangLift.setPower(-1.00);
            liftIsMovingUp = true;

        }
        stopLift();
    }

    ////Stop Lift////////////////////
    public void stopLift() {
        hangLift.setPower(0.00);
        liftIsMovingUp = false;
        //liftIsUp = true;
    }



    public void checkIfLiftDown()
    {
        if (hangLift.getCurrentPosition() < hangLiftUp && liftIsMovingUp == true)
        {
            stopLift();
            liftIsUp = true;
            resetAndRunWithoutEncoders();
        }
    }

    public void liftIsDown()
    {
        liftIsUp = false;
        stopLift();
        hangLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void lockHangLift()
    {
        hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void unlockHangLift()
    {
        hangLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }



    public void shoulderUpAuto(int pulses)
    {
        shoulder1.setPower(0.55);
        shoulder2.setPower(-0.55);
        while(shoulder1.getCurrentPosition() < pulses)
        {

        }
        shoulder1.setPower(0.05);
        shoulder2.setPower(-0.05);
    }

    public void shoulderDownAuto(int pulses)
    {
        shoulder1.setPower(-0.55);
        shoulder2.setPower(0.55);
        while(shoulder1.getCurrentPosition() > pulses)
        {

        }
        shoulder1.setPower(0.05);
        shoulder2.setPower(-0.05);
    }

    public void shoulderUpFarAuto(int pulses)
    {
        shoulder1.setPower(0.55);
        shoulder2.setPower(-0.55);
        while(shoulder1.getCurrentPosition() < pulses)
        {

        }
        shoulder1.setPower(0.1);
        shoulder2.setPower(-0.1);
    }

    public void extendOutAuto(int pulses)
    {
        extender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        while(extender.getCurrentPosition() < pulses)
        {
            extender.setPower(1);

        }
        extender.setPower(0.05);
        extender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }



    public void extendInAuto(int pulses)
    {
        extender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        while(extender.getCurrentPosition() > pulses)
        {
            extender.setPower(-0.85);

        }
        extender.setPower(0.05);
        extender.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }



    public void flipArm(int position)
    {
        if(position == p1)
        {
            movingToP1 = true;
            setTarget(0);

        }
        else if (position == p2)
        {
            movingToP2 = true;
            setTarget(300);
        }
        else if (position == p3)
        {
            movingToP3 = true;
            setTarget(600);
        }
        else
        {
            movingTopP4 = true;
            setTarget(1200);
        }

    }

    public double determinePower()
    {
        flipperValue = shoulder1.getCurrentPosition();
        eValue = target - flipperValue;
        double power = Pk *(eValue);

        if(power >= 1)
        {
            return 1;
        }

        else if(power <= -1)
        {
            return -0.5;
        }

        else
        {
            return power;
        }

    }

    public void setTarget(int input)
    {
        target = input;
        targetHigh = input + 10;
        targetLow = input - 10;
    }

    public void adjustPowerValue()
    {
        shoulder1.setPower(determinePower());
        shoulder2.setPower(-determinePower());
    }



    //Turns specific amount of degrees
    public void Turn(double speed, double degrees, double timeouSeconds, String direction)
    {
        runtime.reset();
        while(true) {
            getIMUValues();
            _parameters.BaseOpMode.telemetry.addData("Inside Method ", getAbsoluteAngle());
            _parameters.BaseOpMode.telemetry.update();
            while (getAbsoluteAngle() < degrees && runtime.seconds() < timeouSeconds )
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

    //Turns to a specific Euclidean degree
    public void turnTo(double heading)
    {
        double currentAngle = getAngle();
        double headingMax = heading + 1;
        double headingMin = heading - 1;

        if(currentAngle < heading)
        {
            while(getAngle() < heading)
            {
                spinLeft(0.35);
            }

            forwBackw(0.00);

        }

        else
        {
            while(getAngle() > heading)
            {
                spinRight(0.35);
            }

            forwBackw(0.00);
        }
    }

    //Drive specific amount of inches
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
                forwBackw(-speed);
            }
            else if (direction == "backward")
            {
                forwBackw(speed);
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

    public void DriveAdjustForAngle(double speed, double targetDistance, double timeoutSeconds, String direction) {
        // Ensure that the opmode is still active
        if (_parameters.BaseOpMode.opModeIsActive()) {
            _parameters.BaseOpMode.telemetry.addData("ENCODER VAlUE: ", getEncoderValues());
            _parameters.BaseOpMode.telemetry.update();

            resetAngle();

            //Set Target Position
            targetDistance = convertInchesToPulses(targetDistance);
            //Reset Encoders and Run Without Encoders
            resetAndRunWithoutEncoders();
            runtime.reset();
            if (direction == "forward")
            {
                forwBackw(-speed);
            }
            else
            {
                forwBackw(speed);
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
                double currentAngle = getAngle();
                if(currentAngle > 0)
                {
                    forwBackw(speed - 0.005, speed, speed - 0.005, speed);
                }
                 if( currentAngle < 0)
                {


                    forwBackw(speed, speed - 0.005, speed, speed - 0.005);
                }
                /*else
                {
                    forwBackw(speed);
                }*/
                sendMessage("" + frontRight.getPower() + " " + frontLeft.getPower() );

            }

            // Stop all motion;
            forwBackw(0);
            //Reset Encoders
            resetAndRunWithoutEncoders();
        }
    }



    public void driveUsingDistanceSensor(int distance, int timeoutSeconds)
    {
        driftLeft(0.35);
        robotIsMoving = true;


        while(distanceSensor.getDistance(DistanceUnit.INCH) > distance && runtime.seconds() < timeoutSeconds)
        {
            _parameters.BaseOpMode.telemetry.addData("distance ", distanceSensor.getDistance(DistanceUnit.INCH) );
            _parameters.BaseOpMode.telemetry.update();
        }

        forwBackw(0.00);
    }


    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAbsoluteAngle()
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

    //Gets current heading from IMU
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

        return globalAngle;
    }

    public void setLEDPattern(RevBlinkinLedDriver.BlinkinPattern patternName)
    {
        pattern = patternName;
        blinkinLedDriver.setPattern(patternName);
    }


    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    public void stopEverything()
    {
        sendMessage("Inside Stop Everything");
        forwBackw(0);
        hangLift.setPower(0);
        collector1.setPower(0);
        collector2.setPower(0);
        shoulder1.setPower(0);
        shoulder2.setPower(0);
    }

    //Initializes vuforia parameters
    public void initVuforia()
    {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //Uncomment if we are using the phone camera
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    //Initialize tensor flow capabilities
    public void initTfod()
    {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void sendMessage(String text)
    {
        BaseOpMode.telemetry.addData("", text);
        BaseOpMode.telemetry.update();
    }
}




