/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author robotics
 */
public class BasicIntakeSystem {
    SafeJaguar roller, arm;
    public BasicIntakeSystem(){
        roller=new SafeJaguar(5);
        arm=new SafeJaguar(8);
    }
    public void setRoller(double speed){
        roller.set(speed);
    }
    public void setArm(double speed){
        arm.set(speed);
    }
}
