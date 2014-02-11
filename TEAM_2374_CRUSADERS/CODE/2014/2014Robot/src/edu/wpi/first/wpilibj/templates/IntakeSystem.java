/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author robotics
 */
public class IntakeSystem {
    SafeJaguar roller;
    
    SafeJaguar roller_height_adjuster;
    public static final int LOWER_ARM_INTAKE_BALL = -1;
    public static final int IDLE = 0;
    public static final int HIGHER_ARM_STOP_ROLLER = 1;
    public static final int ROLLER_ONLY = 2;
    int currentState;
    DigitalInput rollerDown; //give better name
    int rollerDownlimSwitchPort; //need to assign
    DigitalInput rollerUp; //give better name
    int rollerUplimSwitchPort; //need to assign
    
    AnalogChannel IR; //changing to a limit switch
    public IntakeSystem(){
        roller=new SafeJaguar(7);
        IR=new AnalogChannel(3); //changing to limit switch
        
        rollerDown = new DigitalInput(rollerDownlimSwitchPort);
        rollerUp = new DigitalInput(rollerUplimSwitchPort);
        currentState = IDLE;
    }
    public boolean hasBall(){ //changing this to use limit switch
        if(IR.getVoltage()<1.8&&IR.getVoltage()>1)return true;
        return false;
    }
    public void set(double amount){
        roller.set(amount);
    }
    
    public void gatherBall(){
        //make sure nothing happnes if starting situation is wrong
        if(currentState != IDLE){ 
            return;
        }
        //if everything is fine, begin; set things going to bring ball in
        else{
            currentState = LOWER_ARM_INTAKE_BALL;
            roller.set(-1);
            roller_height_adjuster.set(1);
        }
        
        //as long as either the roller arm hasn't reached it's limit or the ball
        //has not been brought in, change nothing about what's happening
        while(!rollerDown.get() || !this.hasBall());
        //if roller arm has reached limit, stop moving the roller arm
        if(rollerDown.get()){
            roller_height_adjuster.set(0);
            //don't change anything else as long as the ball hasn't been retrieved 
            while(!this.hasBall());
            //objectives of state have been achieved; change state now
            currentState = HIGHER_ARM_STOP_ROLLER;
        }
        //if ball has been obtained, objectives of state have been acieved; change state now
        else if(this.hasBall()){
            currentState = HIGHER_ARM_STOP_ROLLER;
        }
        //Since state has been changed, change other factors accordingly
        roller_height_adjuster.set(-0.5);
        roller.set(-0.5);
        //As long as the roller arm has not reached its upper limit, don't change anything
        while(!rollerUp.get());
        //objective of state has been achieved; change state; change factors accordingly
        currentState = IDLE;
        roller.set(0);
        roller_height_adjuster.set(0);
        
       
            //currentState = HIGHER_ARM_STOP_ROLLER;
         //else if (currentState == HIGHER_ARM_STOP_ROLLER) {
            //if roller arm down, keep adjusting height
            //if (rollerDown.get()) {
                //roller.set(-0.5); //roller slows, but doesn't stop yet; ensure ball captured
                //roller_height_adjuster.set(-0.5);
            //} //if roller arm is up, stop adjusting height and stop roller
            //else if (rollerUp.get()) {
              //  roller.set(0);
                //roller_height_adjuster.set(0);
                //currentState = IDLE;
            }
        
    }
    
  /*public void gatherBall(){
        public static final int LOWER_ARM_INTAKE_BALL = -1;
        public static final int IDLE = 0;
        public static fianl int HIGHER_ARM_STOP_ROLLER = 1;
        long start_time = System.currentTimeMillis();
        while(System.currentTimeMillis()-start_time < 3000){
             roller_height_adjuster.set(.5);
         }
         roller_height_adjuster.set(0); //how stop?
        while(!roller.hasBall()){
             roller.set(-1);
        }
        if(roller.hasBall()){
             roller.set(0);
             roller_height_adjuster.set(-1); //how stop?
         }
    }
    */
    /*NOTE: Following Method May NOT Stay here
    Program Planning:
    Goal: Automate Ball Intake. This entails...
         1)Detect a Ball to take in (sensor) OR hit a button(drive crew)
         2a)Lower Roller to appropriate height (mechanical regulation)NEED MOTOR: roller_height_adjuster
         2b)Start intake (in sync with 2a): roller
         3)Detect that ball has entered system (hasBall()-check): roller
         4)Lift Roller back up to beginning position(mechanical...)
         5)Stop roller(stop intake system)
    
     public void bringInBall(){
           roller_height_adjuster.set(1); //when  does this stop(mechanical)
           while(!roller.hasBall()){
                roller.set(-1);
           }
           if(roller.hasBall()){
             roller.set(0);
             roller_height_adjuster.set(-1); //when does this stop (mechanical?)
           }
           roller.set(0);
            */

