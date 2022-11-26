package code;

import javax.swing.text.Position;
import java.util.HashMap;
import java.util.HashSet;
public class Node {
    Pair position;
    int shipPassengers;
    int time;
    int remCap; // remaining capacity of coast guard boat
    boolean station;
    HashMap <Pair, Ship> ships;
    HashSet<Pair> stations;
    Node parent;

    public Node(int x, int y){
        position=new Pair(x,y);
    }
    public static class Pair{
        int x;
        int y;
        public Pair(int x, int y){
            this.x=x;
            this.y=y;
        }
    }
    public static class Ship{
        int lastTimeStamp; //initially 0
        int remPass; //initially total passengers
        public Ship(int t, int rem){
            lastTimeStamp=t;
            remPass=rem;
        }
    }
}
