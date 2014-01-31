/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;

/**
 *
 * @author robotics
 */
public class RecordableAutonomous {
    FileConnection fc; //the class used to access files
    DataOutputStream out; //used to write data to a file
    DataInputStream in; //used to read data from a file
    double x,y,r; //x=horizontal velocity, y=vertical, r=rotation
    int buttons; //a binary representation of the buttons on the joystick
    boolean state; //whether or not it's recording or playing back
    public static final boolean RECORDING=true;
    public static final boolean PLAYBACK=false;
    public RecordableAutonomous(){
        x=y=r=0;//sets everything to default values
        buttons=0;
    }
    public boolean initRecord(int program){
        state=RECORDING;//sets the state to recording
        try {
            //opens up a connection to a file depending on the program number
            fc=(FileConnection) Connector.open("file:///autonomous"+program+".txt",Connector.READ_WRITE);
            //if it already exists, delete it
            if(fc.exists())fc.delete();
            //create an empty file at the desired location
            fc.create();
            //open the output stream for writing data
            out=fc.openDataOutputStream();
            
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;//something must've gone wrong
        }
    }
    public boolean initPlayback(int program){
        state=PLAYBACK;//sets the state to playback
        try {
            //opens up the file connection
            fc=(FileConnection) Connector.open("file:///autonomous"+program+".txt",Connector.READ_WRITE);
            //checks if the routine exists
            if(fc.exists()){
                
                //opens the input stream for reading data
                in=fc.openDataInputStream();
                //tells the robot that it exists
                return true;
            }
            
            else return false;//don't do anything if there is no routine
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;//something went wrong
        }
    }
    public void writeButton(int b){
        //sets the bit at the button's location to 1 in "buttons"
        byte i=(byte)(1<<(b-1));
        buttons=buttons|i;
    }
    public void writeJoystick(double x,double y, double r){
        try {
            //convert x, y, and r to bytes for compression reasons
            byte xi=(byte)(x*127);
            byte yi=(byte)(y*127);
            byte ri=(byte)(r*127);
            //compiles x, y, r, and buttons into a single integer
            int o=((int)xi<<24)|((int)yi<<16)|((int)ri<<8)|buttons;
            //writes the integer to a file
            out.writeInt(o);
            //resets the button values
            buttons=0;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void getJoystick(){
        //this method reads the routine and sets x, y, and r accordingly
        try {
            //get the integer representation of the controller state
            int i=in.readInt();
            //extracts the bytes and converts them to doubles
            x=(double)((byte)((i>>24)&0xFF))/127;
            y=(double)((byte)((i>>16)&0xFF))/127;
            r=(double)((byte)((i>>8)&0xFF))/127;
            //extracts the byte for the button data
            buttons=i&0xFF;
        } catch (IOException ex) {
            
        }
    }
    public boolean getButton(int button){
        if(((buttons>>(button-1))&1)==1){
            return true;
        }
        else return false;
    }
    public void close(){
        x=y=r=0;
        buttons=0;
        try {
            if(state==RECORDING){
                out.close();
            }
            else{
                in.close();
            }
            fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
