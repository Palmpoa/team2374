/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg2014scoutingui;

import java.util.ArrayList;

/**
 *
 * @author robotics
 */
public class Team {
    int teamNumber;
    ArrayList<Match>matches;
    int goalPoints, assistPoints, trussPoints, catchPoints, totalPoints;
    public Team(int teamNumber){
        this.teamNumber=teamNumber;
        matches=new ArrayList<Match>();
        goalPoints=assistPoints=trussPoints=catchPoints=totalPoints=0;
    }
    public void addMatch(long time, int gp, int ap, int tp, int cp){
        matches.add(new Match(time,gp,ap,tp,cp));
    }
}
class Match implements Comparable{
    long time;
    int goalPoints, assistPoints, trussPoints, catchPoints, totalPoints;
    public Match(long time, int gp, int ap, int tp, int cp){
        this.time=time;
        goalPoints=gp;
        assistPoints=ap;
        trussPoints=tp;
        catchPoints=cp;
        totalPoints=gp+ap+tp+cp;
    }
    @Override
    public int compareTo(Object t) {
        Match m=(Match)t;
        if(time>m.time)return 1;
        else if(time<m.time)return -1;
        else return 0;
    }
    
}
