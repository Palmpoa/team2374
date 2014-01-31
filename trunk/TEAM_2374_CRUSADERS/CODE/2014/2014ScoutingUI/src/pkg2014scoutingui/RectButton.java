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

/**
 *
 * @author robotics
 */
public class RectButton {
    Rect rect;
    Color color;
    String title;
    boolean highlighted,selected;
    int id;
    public RectButton(String title, Rect rect, Color c, int id){
        this.title=title;
        this.rect=rect;
        this.color=c;
        highlighted=selected=false;
        this.id=id;
    }
    public void highlight(Vector2 v){
        highlighted=(rect.contains(v));
    }
    public void draw(ImageCollection batch,boolean click){
        batch.fillRect(rect, color, 1);
        if(selected) batch.drawRect(new Vector2(rect.x+4,rect.y+4),rect.width-8,rect.height-8, Color.white, 2);
        
        if(highlighted&&click) batch.drawRect(new Vector2(rect.x+1,rect.y+1),rect.width-2,rect.height-2, Color.white, 2);
        batch.drawRect(rect, Color.black, 2);
        
        batch.DrawString(new Vector2(rect.x+6,rect.y+17), title, Color.white, 3);
    }
}
