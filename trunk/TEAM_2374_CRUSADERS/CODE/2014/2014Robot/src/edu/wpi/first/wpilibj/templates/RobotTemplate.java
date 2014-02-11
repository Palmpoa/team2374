/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
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
    Joystick drivestick;
    BasicIntakeSystem intake;
    DriverStationLCD lcd;
    Catapult catapult;
    DriverStation ds;
    AnalogChannel sonar;
    
    Encoder encoder;
    int encoderPortA=3; // assign 
    int encoderPortB=4; // assign 
    
    public void robotInit(){
        drivetrain=new Drivetrain();
        drivestick=new Joystick(1);
        intake=new BasicIntakeSystem();
        catapult=new Catapult();
        lcd=DriverStationLCD.getInstance();
        ds=DriverStation.getInstance();
        sonar=new AnalogChannel(2);
        
        encoder = new Encoder(encoderPortA, encoderPortB);
        encoder.start();
    }
    public void autonomous() {
        //testing precision of rotation
        drivetrain.calibrateGyro();
        drivetrain.turnToHeading(90);
        sleep(1000);
        drivetrain.turnToHeading(-90);
        sleep(1000);
        drivetrain.turnToHeading(0);
        
        /*AUTONOMOUS IDEAS
        1) Start aligned with goal
           Go forward to within shooting range
           Shoot
           Stay There
        2) Start aligned with goal
           Go forward to within shooting range
           Shoot
           Return to white area and gather another ball (from teammate)
           Go forward within shooting range
           Shoot
           Stay There
        3) Start between high goals
           Detect which one is hot
           Shoot in the hot goal
        4) Start aligned with goal
           Go forward to within shooting range and detect if goal is hot
           Wait till goal IS hot to shoot
        5) Just go forward - IF ALL ELSE FAILS
        */
        /* (Preload Ball)
         Drive Forward Until 20in away from wall
            -Sensor?
            -Calculate Distance (so Set Starting Place)?
         Shoot ball
         Return to original place OR Go to other DESIGNATED place So (you can)
         Intake Second Ball
         Drive Forward Until 20in away from wall (see earlier 
         Shoot Ball
         END OF AUTONOMOUS*/
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        drivetrain.calibrateGyro();
        
        //encoder.reset();
        
        while(isOperatorControl()&&isEnabled()){
            double lspeed=drivestick.getRawAxis(2);
            double rspeed=drivestick.getRawAxis(4);
            //deadband calculation: parabolic mapping to motors
            //comment this out if you don't like it, Erin
            lspeed*=Math.abs(lspeed);
            rspeed*=Math.abs(rspeed);
            //sets the drivetrain to go
            drivetrain.set(lspeed,rspeed);
            //Note: change this so the intake control is on the stick controller
            //as opposed to the driver's controller; to be done after
            //prototyping, testing is done
            
            //if(Math.abs(drivestick.getRawAxis(5))>0.5)intake.setRoller(0); 
            //if(drivestick.getRawAxis(6)>0.5)intake.setRoller(1); 
            //if(drivestick.getRawAxis(6)<-0.5)intake.setRoller(-1);
            
            //if(drivestick.getRawButton(8))intake.setArm(-0.5);
            //else if(drivestick.getRawButton(6))intake.setArm(0.5);
            //else intake.setArm(0);
            
            
            if(drivestick.getRawButton(1)||drivestick.getRawButton(4)){
                if(drivestick.getRawButton(1)){ //LONG SHOT
                    catapult.stopValue=(int)(ds.getAnalogIn(1)*100);//for testing purposes
                    catapult.power=0.85; //experimentally found value
                }
                if(drivestick.getRawButton(4)){ //SHORT SHOT
                    catapult.stopValue=(int)(ds.getAnalogIn(2)*100);//for testing purposes
                    catapult.power=1;
                }
                if(intake.prepareToShoot()){
                    catapult.shoot();
                }
            }
            else{
                catapult.reload();
                if(!catapult.restPos.get()){
                    intake.prepareToShoot();
                }
                else if(drivestick.getRawButton(8))intake.pass();
                else if(intake.ballDetector.get())intake.resetArm();
                else if(drivestick.getRawButton(6))intake.pickUp();
                else intake.resetArm();
            }
            

            
            catapult.update();
            lcd.println(DriverStationLCD.Line.kUser3, 1, "Sonar: "+round(sonar.getVoltage(),4)+"  ");
            //lcd.println(DriverStationLCD.Line.kUser2, 1, "Power: "+percentString(catapult.power)+"    ");
            
            lcd.println(DriverStationLCD.Line.kUser3, 1, catapult.armOffset+"        ");
            lcd.println(DriverStationLCD.Line.kUser1, 1,"Shooter: "+catapult.softStop.get()+"   ");
            lcd.println(DriverStationLCD.Line.kUser4, 1,"Sh_Rest: "+catapult.restPos.get()+"   ");
            //lcd.println(DriverStationLCD.Line.kUser4, 1, "Gyro: "+round(drivetrain.gyro.getAngle(),4)+"  ");
            lcd.println(DriverStationLCD.Line.kUser5, 1, "S_Encoder: "+catapult.getArmPos()+"     ");
            lcd.println(DriverStationLCD.Line.kUser6, 1, "D_Encoder: " + encoder.getDistance()+"      "); //please verify
            
            lcd.updateLCD();
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        VisionProcessor v=new VisionProcessor();
        while(isTest()&&isEnabled()){
            lcd.println(DriverStationLCD.Line.kUser6, 1, "Hot Goal: " + v.isHot()+"   ");
            lcd.updateLCD();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public String percentString(double decimal){
        int i=(int)(decimal*100);
        return i+"%";
    }
    public String round(double num,int sig){
        String s=num+"";
        if(s.length()>sig)s=s.substring(0, sig);
        return s;
    }
    public void sleep(long millis){
        long time=System.currentTimeMillis();
        while(System.currentTimeMillis()<time+millis);
    }
}
