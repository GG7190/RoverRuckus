package org.firstinspires.ftc.teamcode.SeasonCode;

public class PIDG  {
    public double Pk = 0.0;
    public double Ik = 0.0;
    public double Dk = 0.0;
    public double Gk = 0.0;

    public double DeadBand = 0.0;

    public double Pout = 0.0;
    public double Iout = 0.0;
    public double Dout = 0.0;
    public double Gout = 0.0;

    public double Error = 0.0;
    public double SumError = 0.0;

    public double LastError = 0.0;

    public boolean ErrorSign = false;
    public boolean LastErrorSign = false;

    public double AbsError = 0.0;
    public double HorizontialProcessValue = 0.0;

    public double thetaDegrees = 0.0;
    public double thetaRadians = 0.0;

    public double LastSetpoint = 0.0;


    public PIDG(double pk, double ik, double dk, double gk, double horizontialProcessValue, double deadBand)
    {
        Pk = pk;
        Ik = ik;
        Dk = dk;
        Gk = gk;
        HorizontialProcessValue = horizontialProcessValue;
        DeadBand = deadBand;
    }


    public double Compute1(double setPoint, double processValue)
    {
        double output = 0.0;

        if (setPoint != LastSetpoint)
        {
            SumError = 0.0;
            Iout = 0.0;
        }

        Error = setPoint - processValue;
        AbsError = Math.abs(Error);

        ErrorSign = (Error > 0.0);

        Pout = Pk * Error;
        Dout = Dk * (LastError - Error);

        thetaDegrees = (processValue - HorizontialProcessValue) * 0.10465;
        thetaRadians = thetaDegrees * 0.0174532;

        Gout = Gk * Math.cos(thetaRadians);

        if ((ErrorSign && !LastErrorSign) || (!ErrorSign && LastErrorSign))
        {
            SumError = 0.0;
            Iout = 0.0;
        }

       if(Error < 0)
       {
           Gout = 0.6;
       }
        output = Pout + Iout + Dout + Gout;

        if (AbsError > DeadBand)
        {
            Iout = Ik * SumError;

            if (Math.abs(output) < 1.0)
            {
                SumError += Error;
            }
        }
        else
        {
            SumError = 0.0;
            Iout = 0.0;
        }

        if(output > 1.0)
        {
            output = 1.0;
        }
        else
        if(output < -1.0)
        {
            output = -1;
        }

        //----------------------------------------------------------------------------------------------
        LastError = Error;
        LastErrorSign = ErrorSign;
        LastSetpoint = setPoint;

        return output;
    }

}
