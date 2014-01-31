/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014scoutingui;

import Game.GameBase;

/**
 *
 * @author robotics
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new GameBase(new ScoutingLoop(),"2014 Scouting User Interface").Run();
    }
    
}
