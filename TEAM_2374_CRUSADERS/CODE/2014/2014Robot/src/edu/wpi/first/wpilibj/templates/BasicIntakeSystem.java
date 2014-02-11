/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author robotics
 */
public class BasicIntakeSystem {
    SafeJaguar roller;
    Victor arm;
    //Jaguar arm;
    DigitalInput frontStop,backStop,ballDetector;
    long armTimer;
    public BasicIntakeSystem(){
        roller=new SafeJaguar(7);
        //arm=new Jaguar(8);
        arm=new Victor(8);
        frontStop=new DigitalInput(12);
        backStop=new DigitalInput(11);
        ballDetector=new DigitalInput(13);
        armTimer=System.currentTimeMillis();
    }
    public void setRoller(double speed){
        roller.set(speed); //positive = , negative = 
    }
    public void setArm(double speed){
        arm.set(speed);//positive = down, negative = up
    }
    public void pass(){
        if(backStop.get()){
            arm.set(0);
        }
        else{
            arm.set(-0.2);
        }
        roller.set(-1);
    }
    public void pickUp(){
        if(frontStop.get()){
            arm.set(0);
        }
        else{
            arm.set(0.2);
        }
        roller.set(1);
        resetArmTimer();
    }
    public void resetArm(){
        if(backStop.get()){
            arm.set(0);
            roller.set(0);
            resetArmTimer();
        }
        else{
            arm.set(-0.2);
            if(getArmTimer()>1000){
                roller.set(-0.2);
            }
            else{
                roller.set(0.5);
            }
        }
    }
    public boolean prepareToShoot(){
        roller.set(0.5);//keeps the ball in the catapult
        if(frontStop.get()){
            arm.set(0);
            return true;
        }
        else{
            arm.set(0.2);
            return false;
        }
    }
    long getArmTimer(){
        return System.currentTimeMillis()-armTimer;
    }
    void resetArmTimer(){
        armTimer=System.currentTimeMillis();
    }
}
