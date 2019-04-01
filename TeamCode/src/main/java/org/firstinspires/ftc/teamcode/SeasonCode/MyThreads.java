package org.firstinspires.ftc.teamcode.SeasonCode;


public class MyThreads extends Thread {

    public MyThreads(GGHardware robot)
    {
        _robot = robot;
    }

    private GGHardware _robot = null; //Change the time to match what is in the code

    PIDG _pid = new PIDG(0.0075,0.0002,0.0045,0,0,0.0);
    private boolean doStop = false;

    public synchronized void Stop()
    {
        this.doStop = true;
    }

    public void run()
    {
        String threadName = Thread.currentThread().getName();
        int count = 0;

        _robot.armPosition = _robot.shoulder1.getCurrentPosition();//Initializing arm position to current arm position

        while(_robot.BaseOpMode.opModeIsActive())
        {
            if(_robot !=  null)
            {
                double currentValue = _robot.shoulder1.getCurrentPosition();
                double armPosition = _robot.armPosition;
                double motorPower = _pid.Compute1(_robot.armPosition, currentValue);
                //_robot.sendMessage("Arm Position: " + armPosition + " Current Arm Value: " + currentValue + " Motor Power: " + motorPower);
                _robot.shoulder1.setPower(motorPower);
                _robot.shoulder2.setPower(-motorPower);
                //_robot.shoulder1.setPower(0);
                //_robot.shoulder2.setPower(0);

                _robot.sendMessage("" + motorPower);
            }

        _robot.BaseOpMode.sleep(10);
        }
        if(_robot != null )
        {
            _robot.sendMessage( "-Terminating...");//change the function to match the code
        }
    }


}
