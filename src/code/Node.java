package code;

import java.util.HashMap;
public class Node implements Comparable<Node>{
    Pair position;
    int time;
    int remCap; // remaining capacity of coast guard boat
    HashMap <Pair, Ship> ships;
    Node parent;
    int boxes;
    int saved;
    int heuristic;
    int cost;

    public Node(Pair position, int time, int remCap, HashMap<Pair, Ship> ships, Node parent, int boxes, int saved) {
        this.position = position;
        this.time = time;
        this.remCap = remCap;
        this.ships = ships;
        this.parent = parent;
        this.boxes= boxes;
        this.saved=saved;
    }

    public Node(Pair position, int time, int remCap, HashMap<Pair, Ship> ships, Node parent, int boxes, int saved,int heuristic) {
        new Node(position,time,remCap,ships,parent,boxes,saved);
        this.heuristic= heuristic;
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

    @Override
    public int compareTo(Node o) {
        return (this.cost+ this.heuristic)-(o.cost+o.heuristic);
    }
}
