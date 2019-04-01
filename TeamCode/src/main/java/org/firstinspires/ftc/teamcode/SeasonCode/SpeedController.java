package org.firstinspires.ftc.teamcode.SeasonCode;


public class SpeedController {

    public double J;
    public double A;
    public double AMax;
    public double AMin;
    public double V;
    public double VMax;
    public double VMin;

    public double LastJ;

    public SpeedController()
    {
        J = 0.001;
        A = 0;
        AMax = 0.15;
        AMin = -0.15;
        V =0;
        VMax = 1;
        VMin = -1;
    }

    public double stopNow()
    {
        V = 0.0;
        A = 0.0;
        return 0.0;
    }

    public double setVelocity(double j)
    {
        double result = 0.0;

        if(LastJ !=j)
        {
            A = 0.0;
        }

        if (j!=0)
        {
            //What does this do??
            double workJ = (j> 0)?J:(0.0-J);

            A = A + workJ;

            if(workJ > 0)
            {
                if(A > AMax)
                {
                    A = AMax;
                }
            }
            else
            {
                if (A < AMin)
                {
                    A = AMin;
                }
            }

            V = V + A;

            if(workJ > 0)
            {
                if(V > VMax)
                {
                    V = VMax;
                }
            }
            else
            {
                if(V < VMin)
                {
                    V = VMin;
                }

            }

            result = V;
        }

        else
        {
            if (V != 0.0)
            {
                double workJ = (V > 0.0)? (0.0 - J) : J;

                A = A + workJ;

                if(workJ > 0)
                {
                    if(A < AMin)
                    {
                        A = AMin;
                    }
                    else
                    {
                        if(A > AMax)
                        {
                            A = AMax;
                        }
                    }

                    result = V + A;

                    if((result <= 0.0 && V >= 0.0) || (result >= 0.0 && V <= 0.0))
                    {
                        result = V = 0.0;
                        A = 0.0;
                    }

                    V = result;
                }
            }
        }

        LastJ = j;
        return result;

    }


}
