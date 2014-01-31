/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 *
 * @author robotics
 */
public class IntakeSystem {
    //Ultrasonic ultrasonicR;
    //int pingChannelR; //variable for declaring ultrasonicR that needs verification
    //int echoChannelR; //variable for declaring ultrasonicR that needs verification
    //Ultrasonic ultrasonicL;
    //int pingChannelL; //variable for declaring ultrasonicL that needs verification
    //int echoChannelL; //variable for declaring ultrasonicL that needs verification
    Jaguar roller, shooter;
    AnalogChannel ir;
    Solenoid led;
    int state;
    public static final int SHOOTING=1;
    public static final int LOADING=2;
    public static final int LOADED=3;
    public IntakeSystem(){
        roller=new Jaguar(5);
        shooter=new Jaguar(6);
        ir=new AnalogChannel(2);
        led=new Solenoid(1);
        state=SHOOTING;
        //ultrasonicR = new Ultrasonic(pingChannelR, echoChannelR, Ultrasonic.Unit.kInches);
        //ultrasonicL = new Ultrasonic(pingChannelL, echoChannelL, Ultrasonic.Unit.kInches);
    }
    public void set(double amount){
        state=SHOOTING;
        roller.set(amount);
        shooter.set(amount);
        led.set(hasBall());
    }
    public void intake(){
        if(state==SHOOTING){
            roller.set(1);
            shooter.set(1);
            if(!hasBall()){
                state=LOADING;
            }
        }
        else if(state==LOADING){
            roller.set(1);
            shooter.set(1);
            if(hasBall()){
                state=LOADED;
            }
        }
        else if(state==LOADED){
            roller.set(0);
            shooter.set(0);
        }
        led.set(hasBall());
    }
    public boolean hasBall(){
        //we experimented with the IR sensor and this seems to work fairly well
        if(getIR()>0.8&&getIR()<2){
            return true;
        }
        else return false;
    }
    public double getIR(){
        return ir.getVoltage();
    }
    
    /*
    public boolean deliverCargo(){
        while(ultrasonicR.getRangeInches() > 2 && ultrasonicL.getRangeInches() > 2 ){
            //go forward,oops!
        }
        
        return true;
    }
    */
}
