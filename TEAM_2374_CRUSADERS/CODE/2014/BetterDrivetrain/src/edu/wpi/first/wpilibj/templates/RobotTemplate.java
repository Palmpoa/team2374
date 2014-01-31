/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    Drivetrain drivetrain;
    Joystick drivestick;
    Jaguar intake;
    public void robotInit(){
        drivetrain=new Drivetrain();
        drivestick=new Joystick(1);
        intake=new Jaguar(5);
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drivetrain.reset();
        while(isOperatorControl()){
            drivetrain.update(drivestick.getRawAxis(2), drivestick.getRawAxis(4));
            if(Math.abs(drivestick.getRawAxis(5))>0.5)intake.set(0); //stop (left, right)
            if(drivestick.getRawAxis(6)>0.5)intake.set(1); //others
            if(drivestick.getRawAxis(6)<-0.5)intake.set(-1);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
