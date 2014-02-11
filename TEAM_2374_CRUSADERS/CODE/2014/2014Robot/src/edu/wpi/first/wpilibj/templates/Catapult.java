/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author robotics
 */
public class Catapult {
    Jaguar arm1,arm2;
    DigitalInput softStop; 
    DigitalInput restPos;
    //boolean canShoot;
    Encoder armPos;
    double power;
    int stopValue;
    double armOffset;
    int state;
    public static final int IDLE=0;
    public static final int SHOOTING=1;
    public static final int RELOADING=2;
    public Catapult(){
        arm1=new Jaguar(5);
        arm2=new Jaguar(6);//MAKE SURE RED->RED AND BLACK->BLACK
        softStop=new DigitalInput(14); 
        restPos=new DigitalInput(10);
        armPos=new Encoder(1,2); //changing ports from 3,4 to 5,6 to 1,2 to find functional ports
        armPos.start();
        state=IDLE;
        armOffset=0;
        power=1;
        stopValue=245; //experimental value; more than slightly random
    }
    public void update(){
        if(state==SHOOTING){
            if(getArmPos()>stopValue){
                arm1.set(0);
                arm2.set(0);
                state=RELOADING;
            }
        }
        if(restPos.get()){
            //if the encoder starts working, uncomment the line below
            armOffset=armPos.getDistance();
            //then find the correct encoder value where the soft stop is
        }
    }
    public void shoot(){
        if(state==IDLE){
            arm1.set(power);
            arm2.set(-power);
            state=SHOOTING;
        }
    }
    public void stop(){
        arm1.set(0);
        arm2.set(0);
        state=IDLE;
    }
    public void reload(){
        if(restPos.get()){
            stop();
        }
        else{
            if(getArmPos()<100){
                arm1.set(-0.1);
                arm2.set(0.1);
            }
            else{
                arm1.set(-0.15);
                arm2.set(0.15);
            }

            state=RELOADING;
        }
    }
    public void setPower(double power){
        this.power=Math.min(1,Math.max(0,power));
    }
    public double getArmPos(){
        return Math.abs(armPos.getDistance()-armOffset);
    }
}
