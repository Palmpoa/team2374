/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014scoutingui;

import Game.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author robotics
 */
public class ScoutingLoop extends Game{
    ScoreMatrix matrix;
    boolean canClick;
    @Override
    public void InitializeAndLoad() {
        matrix=new ScoreMatrix();
        canClick=true;
        this.setBackground(Color.black);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void UnloadContent() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Update() {
        matrix.update(mouse.location());
        if(mouse.isPressed(MouseEvent.BUTTON1)){
            if(canClick){
                matrix.select(mouse.location());
                canClick=false;
            }
        }
        else if(mouse.isPressed(MouseEvent.BUTTON3)){
            if(canClick){
                matrix.deselect(mouse.location());
            }
        }
        else{
            canClick=true;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Draw(Graphics grphcs) {
        matrix.draw(batch,canClick);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
