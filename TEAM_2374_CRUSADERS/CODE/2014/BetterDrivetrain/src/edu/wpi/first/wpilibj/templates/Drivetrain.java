/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author robotics
 */
public class Drivetrain {
    Jaguar L1,L2;
    Jaguar R1,R2;
    double lspeed,rspeed;
    long time;
    public static final int PID_RATE=150;//ms required to go from stopped to full speed
    public Drivetrain(){
        L1=new Jaguar(1);
        L2=new Jaguar(2);
        R1=new Jaguar(3);
        R2=new Jaguar(4);
        lspeed=rspeed=0;
        time=System.currentTimeMillis();
    }
    public void update(double ltarget,double rtarget){
        long dt=System.currentTimeMillis()-time;//difference in time since last update
        time=System.currentTimeMillis();
        double amt=(double)dt/(double)PID_RATE;
        if(lspeed<ltarget){
            lspeed+=amt;
            if(lspeed>ltarget)lspeed=ltarget; //prevent overshooting; same applies for right
        }
        if(lspeed>ltarget){
            lspeed-=amt;
            if(lspeed<ltarget)lspeed=ltarget; //prevent undershooting; same applies for right
        }
        if(rspeed<rtarget){
            rspeed+=amt;
            if(rspeed>rtarget)rspeed=rtarget;
        }
        if(rspeed>rtarget){
            rspeed-=amt;
            if(rspeed<rtarget)rspeed=rtarget;
        }
        L1.set(lspeed);
        L2.set(lspeed);
        R1.set(rspeed);
        R2.set(rspeed);
    }
    public void reset(){
        time=System.currentTimeMillis();
    }
}
