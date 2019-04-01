package org.firstinspires.ftc.teamcode.SeasonCode;

public class LeadThread extends Thread {

    public LeadThread(GGHardware robot)
    {
        _robot = robot;
    }

    private GGHardware _robot = null;
    private boolean doStop = false;

    public synchronized void Stop()
    {
        this.doStop = true;
    }

    public void run()
    {
        double setPoint = 0;
        double error = 0;
        while (_robot.BaseOpMode.opModeIsActive())
        {


            if (_robot != null)
            {
                switch(_robot.upDownAction)
                {
                    case 1: {
                        error = (_robot.SHOULDERMAX - _robot.armPosition);
                        double powerUp = Math.abs(error * 0.0015);
                        if (powerUp < 0) {
                            powerUp = 0;
                        }
                        if (powerUp > 1) {
                            powerUp = 1;
                        }
                        if (_robot.armPosition < _robot.SHOULDERMAX) {
                            _robot.armPosition += 25 * powerUp;
                            if (_robot.armPosition > _robot.SHOULDERMAX) {
                                _robot.armPosition = _robot.SHOULDERMAX;
                                _robot.upDownAction = 0;
                            }
                        }
                        _robot.BaseOpMode.sleep(10);
                    }
                        break;


                    case 0:
                        {
                            _robot.BaseOpMode.sleep(10);
                        }
                        break;

                    case -1:
                        {
                            error = (_robot.armPosition - _robot.SHOULDERMIN);
                            double powerDown = Math.abs(error * 0.00075);
                            double dampening = (Math.abs(error) * .00045);
                            if (powerDown < 0) {
                                powerDown = 0;
                            }
                            if (powerDown > 1) {
                                powerDown = 1;
                            }
                            if (dampening < 0) {
                                dampening = 0;
                            }
                            if (dampening > 14) {
                                dampening = 14;
                            }
                            if (_robot.armPosition > _robot.SHOULDERMIN) {
                                _robot.armPosition = 200 /** powerDown) - dampening*/;
                                _robot.upDownAction = 0;
                                if (_robot.armPosition < _robot.SHOULDERMIN) {
                                    _robot.armPosition = _robot.SHOULDERMIN;
                                    _robot.upDownAction = 0;
                                }
                            }
                            _robot.BaseOpMode.sleep(5);
                        }
                        break;
                    }

                    _robot.sendMessage("setPoint: " + setPoint);
                    //_robot.BaseOpMode.sleep(10);
                }
            }
            if (_robot != null) {
                _robot.sendMessage("-Terminating...");//change the function to match the code
            }
        }

    }


