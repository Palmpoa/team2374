/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 *
 * @author robotics
 */
public class Drivetrain {
    Ultrasonic ultrasonicR;
    int pingChannelR; //variable for declaring ultrasonicR that needs verification
    int echoChannelR; //variable for declaring ultrasonicR that needs verification
    Ultrasonic ultrasonicL;
    int pingChannelL; //variable for declaring ultrasonicL that needs verification
    int echoChannelL; //variable for declaring ultrasonicL that needs verification
    Jaguar[] motors;
    double[] speeds;
    public Gyro gyro;
    public boolean gyroEnabled;
    public float a=1;
    public Drivetrain(){
        speeds=new double[4];
        motors=new Jaguar[4];
        for(int i=0; i<4; i++){
            motors[i]=new Jaguar(i+1);//ports 1,2,3,4
        }
        gyro=new Gyro(1);
        //gyro.setSensitivity(5/250);
        gyroEnabled=false;
        //ultrasonicR = new Ultrasonic(pingChannelR, echoChannelR, Ultrasonic.Unit.kInches);
        //ultrasonicL = new Ultrasonic(pingChannelL, echoChannelL, Ultrasonic.Unit.kInches);
    }
    public void move(double Vspeed, double Hspeed, double rspeed){
        double vspeed,hspeed,rad;
        rad=gyro.getAngle()*Math.PI/180;
        if(gyroEnabled){
            vspeed=Math.cos(rad)*Vspeed+Math.sin(rad)*Hspeed;
            hspeed=Math.cos(rad)*Hspeed-Math.sin(rad)*Vspeed;
        }
        else{
            vspeed=Vspeed;
            hspeed=Hspeed;
        }
        //vector maths
        speeds[lf]=vspeed+hspeed*a+rspeed;
        speeds[rf]=vspeed-hspeed*a-rspeed;
        speeds[lb]=vspeed-hspeed*a+rspeed;
        speeds[rb]=vspeed+hspeed*a-rspeed;
        clipSpeeds();
        for(int i=0; i<4; i++){
            //sets all the motors according to speeds[i]
            motors[i].set(speeds[i]);
        }
    }
    
    public boolean ReachedGoal(){
        /*
        if(ultrasonicR.getRangeInches() < 2 || ultrasonicL.getRangeInches() < 2){
            return true;
        }
        else
            return false;
        */
        if(true)return false;
        else if(ultrasonicR.getRangeInches() > 2 && ultrasonicL.getRangeInches() > 2 ){
            move(1, 0, 0);
        }
        return true;
    }
    
    public void clipSpeeds(){
        double max=1;
        for(int i=0; i<4; i++){
            //finds the maximum speed assigned to each wheel
            max=Math.max(Math.abs(speeds[i]), max);
        }
        for(int i=0; i<4; i++){
            //clips the maximum to 1, sets the rest to a proportional value
            speeds[i]/=max;
        }
    }
    public static final int lf=0;//port 1
    public static final int rf=1;//port 2
    public static final int lb=2;//port 3
    public static final int rb=3;//port 4
}
