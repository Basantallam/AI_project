package code;

import java.util.HashMap;
public class Node {
    Pair position;
    int time;
    int remCap; // remaining capacity of coast guard boat
    HashMap <Pair, Ship> ships;
    Node parent;
    int boxes;
    int saved;

    public Node(Pair position, int time, int remCap, HashMap<Pair, Ship> ships, Node parent, int boxes, int saved) {
        this.position = position;
        this.time = time;
        this.remCap = remCap;
        this.ships = ships;
        this.parent = parent;
        this.boxes= boxes;
        this.saved=saved;
    }

    @Override
    public String toString() {
        return "Node{" +
                "position=" + position +
                ", time=" + time +
                ", remCap=" + remCap +
                ", ships=" + ships +
                ", boxes=" + boxes +
                ", saved=" + saved +
                '}';
    }
}
