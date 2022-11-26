package code;

import java.util.HashMap;
public class Node {
    Pair position;
    int time;
    int remCap; // remaining capacity of coast guard boat
    HashMap <Pair, Ship> ships;
    Node parent;

    public Node(Pair position, int time, int remCap, HashMap<Pair, Ship> ships, Node parent) {
        this.position = position;
        this.time = time;
        this.remCap = remCap;
        this.ships = ships;
        this.parent = parent;
    }


}
