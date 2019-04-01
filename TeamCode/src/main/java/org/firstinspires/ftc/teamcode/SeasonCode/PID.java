package org.firstinspires.ftc.teamcode.SeasonCode;

public class PID {

    public double PK = 0.005;
    public double IK = 0.003;
    public double DK = -0.001;
    public double GK = 0.25;
    public double pid = 0;
    private double previousError = 0;
    private double sumError = 0;
    private double previousResult =  0;
    private double deadband = 10;


    public PID()
    {
    }

    public double compute(double setPoint, double currentValue)//Current value
    {
        double result = 0;

        double error = setPoint - currentValue;

        if(Math.abs(error) > deadband )
        {
            sumError += error;

            pid = (error * PK) + (IK * sumError) + (DK +(previousError-error));
            result = pid + Math.cos((currentValue - 325) * 3.6 * 57.29 * GK);

            if((previousError > 0 && error < 0) || (previousError < 0 && error > 0))
            {
                sumError = 0;
            }

            if (result < -1 )
            {
                result = -1;
                sumError = 0;
            }
            if(result > 1)
            {
                result = 1;
                sumError = 0;
            }
            previousResult = result;
        }
        else
        {
            result = previousResult;
        }


        previousError = error;
        return result;
    }
}
