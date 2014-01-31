/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
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
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    Drivetrain drivetrain;
    Joystick driveStick;
    boolean buttonPressed;
    DriverStationLCD lcd;
    RecordableAutonomous ra;
    IntakeSystem intake;
    public void robotInit(){
        driveStick=new Joystick(1);
        drivetrain=new Drivetrain();
        buttonPressed=false;
        lcd=DriverStationLCD.getInstance();
        ra=new RecordableAutonomous();
        intake=new IntakeSystem();
    }
    public void autonomous() {
        int program=getProgram();
        lcd.println(DriverStationLCD.Line.kUser1,1,"Program "+program+"         ");
        lcd.updateLCD();
        if(program==0){
            long endTime=System.currentTimeMillis()+5000;
            while(System.currentTimeMillis()<endTime){
                drivetrain.move(-1,0,0);
                
            }
            drivetrain.move(0,0,0);
        }
        else if(ra.initPlayback(program)){
            long time=System.currentTimeMillis();
            long endTime=System.currentTimeMillis()+15000;
            while(System.currentTimeMillis()<endTime){
                if(System.currentTimeMillis()>time+50){
                    ra.getJoystick();
                    update();
                    time+=50;
                }
            }
            drivetrain.move(0,0,0);
            ra.close();
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drivetrain.gyro.reset();
        while(isOperatorControl()){
            update();
        }
    }
    public void update(){
        //drivetrain.move(1,2,3)
            //1: positive=forwards, negative=backwards
            //2: positive=strafe right, negative=strafe left
            //3: positive=rotate right, negative=rotate left
        if(ra.state==RecordableAutonomous.PLAYBACK&&isAutonomous()){
            drivetrain.move(ra.y,ra.x,ra.r);
        }
        else{
            drivetrain.move(-driveStick.getThrottle(),driveStick.getZ(),driveStick.getX());
        }
        if(driveStick.getRawButton(6)||ra.getButton(6)){
            //takes in dodgeballs, w/ infra-red detection
            intake.intake();
        }
        else if(driveStick.getRawButton(1)||ra.getButton(1)){
            //spits out dodgeballs, regardless of IR
            intake.set(1);
        }
        else if(driveStick.getRawButton(2)||ra.getButton(2)){
            //runs dodgeballs out backwards(top to bottom), for emergency use
            intake.set(-1);
        }
        else{
            intake.set(0);
        }
        /*if(driveStick.getRawButton(I_NEED_A_BUTTON)){
            if(drivetrain.ReachedGoal()){
                intake.set(1);
            }
         * 
         */
        lcd.println(DriverStationLCD.Line.kUser1, 1, "Program "+getProgram()+"          ");
        if(intake.hasBall()){
            lcd.println(DriverStationLCD.Line.kUser2, 1, "i has a ball (^_^) ");
        }
        else{
            lcd.println(DriverStationLCD.Line.kUser2, 1, "i no has ball (;_;)");
        }
        lcd.println(DriverStationLCD.Line.kUser3, 1, "IR: "+roundToHundredths(intake.getIR())+"     ");
        //lcd.println(DriverStationLCD.Line.kUser3, 1, "Angle: " + roundToHundredths(drivetrain.gyro.getAngle())+"    ");
        //lcd.println(DriverStationLCD.Line.kUser4, 1, "Gyro: " + drivetrain.gyroEnabled+"    ");
        lcd.updateLCD();
    }
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        int program=0;
        lcd.println(DriverStationLCD.Line.kUser2,1,"Press 1 to record");
        lcd.updateLCD();
        while(!driveStick.getRawButton(1)){
            program=getProgram();
            lcd.println(DriverStationLCD.Line.kUser1,1,"Program "+program+"         ");
            lcd.updateLCD();
        }
        //record the autonomous mode
        if(ra.initRecord(program)){
            lcd.println(DriverStationLCD.Line.kUser2,1,"Recording...        ");
            lcd.updateLCD();
            long time=System.currentTimeMillis();
            long endTime=System.currentTimeMillis()+15000;
            while(System.currentTimeMillis()<endTime){
                if(System.currentTimeMillis()>time+50){
                    for(int i=1; i<=8; i++){
                        if(driveStick.getRawButton(i))ra.writeButton(i);
                    }
                    drivetrain.move(-driveStick.getThrottle(),driveStick.getZ(),driveStick.getX());
                    ra.writeJoystick(driveStick.getZ(),-driveStick.getThrottle(),driveStick.getX());
                    time+=50;
                }
            }
            ra.writeJoystick(0, 0, 0);
            ra.close();
            lcd.println(DriverStationLCD.Line.kUser2,1,"Recorded.       ");
            lcd.updateLCD();
        }
    }
    public float roundToHundredths(double x){
        return (float)(.01*Math.floor(x*100));
    }
    public int getProgram(){
        int program=0;
        for(int i=1; i<=3; i++){
            if(DriverStation.getInstance().getDigitalIn(i)){
                program=i;
            }
        }
        return program;
    }
}
