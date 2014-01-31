/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014scoutingui;

import Utilities.ImageCollection;
import Utilities.Rect;
import Utilities.Vector2;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author robotics
 */
public class ScoreMatrix {
    int[] teams;
    int lastTeamSelected;
    RectButton[] buttons;
    boolean[][] assistConfigurations;
    int assists;
    int x,y;
    int trussTeam,catchTeam;
    boolean highlighted;
    boolean flipColor;
    LinkedList<RectButton> buttonPresses;
    
    int[] goalPoints,assistPoints,trussPoints,catchPoints;
    public ScoreMatrix(){
        teams=new int[]{0,0,0};
        boolean t=true;
        boolean f=false;
        //ALL THE PERMUTATIONS
        assistConfigurations=new boolean[][]{
            {t,f,f, f,t,f, f,f,t},
            {t,f,f, f,f,t, f,t,f},
            {f,t,f, t,f,f, f,f,t},
            {f,t,f, f,f,t, t,f,f},
            {f,f,t, t,f,f, f,t,f},
            {f,f,t, f,t,f, t,f,f}
        };
        assists=0;
        lastTeamSelected=0;
        trussTeam=catchTeam=0;
        buttons=new RectButton[16];
        buttonPresses=new LinkedList<RectButton>();
        Color c;
        for(int i=0; i<3; i++){
            c=Color.blue;
            if(i==1)c=Color.gray;
            else if(i==0)c=Color.red;
            for(int j=0; j<3; j++){
                buttons[i+j*3]=new RectButton("Team "+teams[j],new Rect(i*150+80,j*100+20,150,100),c,i+j*3);
            }
        }
        buttons[9]=new RectButton("High",new Rect(20,20,60,150),Color.red,9);
        buttons[10]=new RectButton("High",new Rect(530,20,60,150),Color.blue,10);
        buttons[11]=new RectButton("Low",new Rect(20,170,60,150),Color.red,11);
        buttons[12]=new RectButton("Low",new Rect(530,170,60,150),Color.blue,12);
        buttons[13]=new RectButton("Truss",new Rect(20,320,285,60),Color.gray,13);
        buttons[14]=new RectButton("Catch",new Rect(305,320,285,60),Color.gray,14);
        
        buttons[15]=new RectButton("New Match", new Rect(20,410,570,60),Color.gray,15);
        x=y=0;
        highlighted=false;
        
        goalPoints=new int[3];
        assistPoints=new int[3];
        trussPoints=new int[3];
        catchPoints=new int[3];
    }
    public void update(Vector2 mouse){
        for(int i=0; i<buttons.length; i++){
            buttons[i].highlight(mouse);
        }
        calculateAssists();
        
        if(buttons[9].selected||buttons[10].selected){
            //add 10 points
            calculatePoints(10);
        }
        if(buttons[11].selected||buttons[12].selected){
            //add 1 points
            calculatePoints(1);
        }
        
        if(buttons[15].selected){
            //save scores
            saveScores();
            for(int i=0; i<buttons.length; i++){
                buttons[i].selected=false;
            }
            newMatch();
        }
    }
    public void saveScores(){
        for(int i=0; i<3; i++){
            if(teams[i]==0)return;
        }
        
        Path file=Paths.get("data.txt");
        Charset charset = Charset.forName("US-ASCII");
        try {
            BufferedWriter writer=Files.newBufferedWriter(file,charset,StandardOpenOption.APPEND);
            writer.write("Time "+System.currentTimeMillis());
            writer.newLine();
            for(int i=0; i<3; i++){
                writer.write(teams[i]+" ");
                writer.write(goalPoints[i]+" ");
                writer.write(assistPoints[i]+" ");
                writer.write(trussPoints[i]+" ");
                writer.write(catchPoints[i]+" ");
                writer.newLine();
                
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ScoreMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void calculatePoints(int goalValue){
        lastTeamSelected=trussTeam=catchTeam=-1;
        boolean caught=false;
        boolean[] teamAssists=new boolean[3];
        for(RectButton b:buttonPresses){
            if(b.id<9){
                lastTeamSelected=b.id/3;
                teamAssists[b.id/3]=true;
                if(trussTeam!=-1&&catchTeam==-1&&lastTeamSelected!=trussTeam){
                    catchTeam=lastTeamSelected;
                }
            }
            if(b.id==13){
                trussTeam=lastTeamSelected;
            }
            if(b.id==14){
                caught=true;
            }
        }
        if(!caught)catchTeam=-1;
        for(int i=0; i<buttons.length; i++){
            buttons[i].selected=false;
        }
        buttonPresses.clear();
        
        int n=0;
        for(int i=0; i<3; i++){
            if(teamAssists[i])n++;
        }
        int assistValue=0;
        if(assists==2)assistValue=10;
        if(assists==3)assistValue=30;
        for(int i=0; i<3; i++){
            if(teamAssists[i])assistPoints[i]+=assistValue/n;
        }
        if(trussTeam!=-1)trussPoints[trussTeam]+=10;
        if(catchTeam!=-1)catchPoints[catchTeam]+=10;
        goalPoints[lastTeamSelected]+=goalValue;
    }
    public void newMatch(){
        try{
            String s=JOptionPane.showInputDialog("Enter the first team's number:", null);
            Scanner reader=new Scanner(s);
            teams[0]=reader.nextInt();
            s=JOptionPane.showInputDialog("Enter the second team's number:", null);
            reader=new Scanner(s);
            teams[1]=reader.nextInt();
            s=JOptionPane.showInputDialog("Enter the third team's number:", null);
            reader=new Scanner(s);
            teams[2]=reader.nextInt();
        }
        catch(Exception e){
            teams=new int[]{0,0,0};
        }
        Color c;
        for(int i=0; i<3; i++){
            c=Color.blue;
            if(i==1)c=Color.gray;
            else if(i==0)c=Color.red;
            for(int j=0; j<3; j++){
                buttons[i+j*3]=new RectButton("Team "+teams[j],new Rect(i*150+80,j*100+20,150,100),c,i+j*3);
            }
        }
    }
    public void calculateAssists(){
        assists=0;
        for(int n=0; n<6; n++){
            int a=0;
            for(int i=0; i<9; i++){
                if(buttons[i].selected&&assistConfigurations[n][i])a++;
            }
            if(assists<a)assists=a;
        }
    }
    public void select(Vector2 mouse){
        for(int i=0; i<buttons.length; i++){
            if(buttons[i].highlighted){
                buttons[i].selected=true;
                buttonPresses.add(buttons[i]);
                //if(i<9)lastTeamSelected=teams[i/3];
            }
        }

    }
    public void deselect(Vector2 mouse){
        for(int i=0; i<buttons.length; i++){
            if(buttons[i].highlighted){
                buttons[i].selected=false;
                buttonPresses.remove(buttons[i]);
            }
        }
    }
    public void draw(ImageCollection batch,boolean click){
        for(int i=0; i<buttons.length; i++){
            buttons[i].draw(batch,click);
        }
        batch.DrawString(new Vector2(20,400), "Assists: "+assists, Color.white, 5);
    }
}
