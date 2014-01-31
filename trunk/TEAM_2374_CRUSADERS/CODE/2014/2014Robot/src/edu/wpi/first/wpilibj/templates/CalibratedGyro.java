/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author robotics
 */
public class CalibratedGyro{
    private final Gyro gyro;
    private long time,duration;
    private double offset;
    public CalibratedGyro(int port){
        gyro=new Gyro(port);
        time=System.currentTimeMillis();
        duration=100;
        offset=0;
    }
    public void calibrate(long duration){
        gyro.reset();
        this.duration=duration;
        time=System.currentTimeMillis();
        while(System.currentTimeMillis()<time+duration){
            
        }
        offset=gyro.getAngle();
    }
    public void reset(){
        gyro.reset();
        duration=100;
        time=System.currentTimeMillis();
        offset=0;
    }
    public double getAngle(){
        double angle=gyro.getAngle();
        angle-=(offset*(System.currentTimeMillis()-time))/(double)duration;
        return angle;
    }
}