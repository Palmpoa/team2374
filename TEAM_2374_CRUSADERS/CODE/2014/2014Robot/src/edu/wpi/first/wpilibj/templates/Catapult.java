/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author robotics
 */
public class Catapult {
    Jaguar arm1,arm2;
    DigitalInput softStop; 
    //boolean canShoot;
    double power;
    
    int state;
    public static final int IDLE=0;
    public static final int SHOOTING=1;
    public static final int RELOADING=2;
    public Catapult(){
        arm1=new Jaguar(6);
        arm2=new Jaguar(7);
        softStop=new DigitalInput(1); 
        state=IDLE;
        power=1;
    }
    public void update(){
        if(state==SHOOTING){
            if(softStop.get()){
                arm1.set(0);
                arm2.set(0);
                state=RELOADING;
            }
        }
    }
    public void shoot(){
        if(state==IDLE){
            arm1.set(power);
            arm2.set(power);
            state=SHOOTING;
        }
    }
    public void stop(){
        if(true||state==RELOADING){ //rationale of this?
            arm1.set(0);
            arm2.set(0);
            state=IDLE;
        }
    }
    public void reload(){
        arm1.set(-0.3);
        arm2.set(-0.3);
        state=RELOADING;
    }
    public void setPower(double power){
        this.power=Math.min(1,Math.max(0,power));
        
    }
}
