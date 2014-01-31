/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;

/**
 * A Jaguar equipped with an internal PID voltage thread.
 * Designed to prevent stalling.
 * @author Peter Cowal
 */
public class SafeJaguar{
    private final Jaguar jag;
    private final UpdateThread updater;
    /**
     * Initializes the jaguar and starts the update thread.
     * @param port The port that the Jaguar is attached to.
     */
    public SafeJaguar(int port){
        jag=new Jaguar(port);
        updater=new UpdateThread(this,jag);
    }
    /**
     * Commands the Jaguar to ramp up to the target speed.
     * @param targetSpeed The target speed, from -1 to 1.
     */
    public void set(double targetSpeed){
        updater.targetSpeed=targetSpeed;
    }
    /**
     * Sets the jaguar directly, without voltage ramping.
     * DOES NOT PREVENT STALLING
     * @param targetSpeed 
     */
    public void setDirect(double targetSpeed){
        updater.targetSpeed=targetSpeed;
        updater.speed=targetSpeed;
        jag.set(targetSpeed);
    }
    /**
     * Sets the rate at which the PID algorithm updates the motor's speed.
     * @param millis The amount of time for the motor to go from stopped to full speed.
     */
    public void setPIDRate(int millis){
        updater.pidRate=millis;
    }
}
class UpdateThread extends Thread{
    long time;
    int pidRate;
    double speed,targetSpeed;
    SafeJaguar safejag;
    Jaguar jag;
    boolean active;
    public UpdateThread(SafeJaguar s, Jaguar j){
        super();
        pidRate=100;
        time=System.currentTimeMillis();
        speed=0;
        targetSpeed=0;
        safejag=s;
        jag=j;
        start();
    }
    public void run(){
        active=true;
        while(active){
            long dt=System.currentTimeMillis()-time; //dt = difference in time
            time=System.currentTimeMillis();
            if(speed!=targetSpeed){
                double amt=(double)dt/(double)pidRate;
                if(speed<targetSpeed){
                    speed+=amt;
                    if(speed>targetSpeed)speed=targetSpeed;
                }
                if(speed>targetSpeed){
                    speed-=amt;
                    if(speed<targetSpeed)speed=targetSpeed;
                }
                jag.set(speed);
            }
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }
    }
}