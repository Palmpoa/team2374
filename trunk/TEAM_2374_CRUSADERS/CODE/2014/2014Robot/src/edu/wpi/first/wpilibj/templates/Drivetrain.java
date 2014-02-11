/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author robotics
 */
public class Drivetrain {
    SafeJaguar[] motors;
    CalibratedGyro gyro;
    double heading;
    public Drivetrain(){
        motors=new SafeJaguar[]{new SafeJaguar(1),new SafeJaguar(2), 
                                new SafeJaguar(3),new SafeJaguar(4)}; //L,L,R,R
        gyro=new CalibratedGyro(1);
        heading=0;
        //MAKE SURE RED->RED AND BLACK->BLACK
        //(software accounts for reversal)
        
    }
    public void set(double leftSpeed, double rightSpeed){
        motors[0].set(-leftSpeed);
        motors[1].set(-leftSpeed);
        motors[2].set(rightSpeed);
        motors[3].set(rightSpeed);
    }
    public void setDirect(double leftSpeed, double rightSpeed){
        motors[0].setDirect(-leftSpeed);
        motors[1].setDirect(-leftSpeed);
        motors[2].setDirect(rightSpeed);
        motors[3].setDirect(rightSpeed);
    }
    public void calibrateGyro(){
        gyro.calibrate(250);
        heading=0;
    }
    public void driveForwards(double speed){
        if(gyro.getAngle()<heading-1){
            set(-speed/2,-speed);
        }
        else if(gyro.getAngle()>heading+1){
            set(-speed,-speed/2);
        }
        else{
            set(-speed,-speed);
        }
    }
    public void driveBackwards(double speed){
        if(gyro.getAngle()<heading-1){
            set(speed,speed/2);
        }
        else if(gyro.getAngle()>heading+1){
            set(speed/2,speed);
        }
        else{
            set(speed,speed);
        }
    }
    public void turnToHeading(double targetHeading){
        double speed=0.4;
        double slow=0.2;
        heading=targetHeading;
        if(gyro.getAngle()<heading){
            //turn right
            set(-speed,speed);
            while(gyro.getAngle()<targetHeading-2); //slow down motors sooner
            stop();
            sleep(50);
            //rotate back to correct angle
            set(slow,-slow);
            while(gyro.getAngle()>targetHeading+2);
        }
        else{
            //turn left
            set(speed,-speed);
            while(gyro.getAngle()>targetHeading+2);
            stop();
            sleep(50);
            //rotate back to correct angle
            set(-slow,slow);
            while(gyro.getAngle()<targetHeading-2);
        }
        stop();
    }
    public void stop(){
        setDirect(0,0);//instantly stop
    }
    public void sleep(long millis){
        long time=System.currentTimeMillis();
        while(System.currentTimeMillis()<time+millis);
    }
    
    
}
