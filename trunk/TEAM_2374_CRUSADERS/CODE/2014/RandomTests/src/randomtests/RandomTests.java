/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package randomtests;

/**
 *
 * @author robotics
 */
public class RandomTests {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long time=System.currentTimeMillis();
        System.out.print("Hi");
        while(System.currentTimeMillis()<time+2000000);
        System.out.print(" there");
    }
    
}
