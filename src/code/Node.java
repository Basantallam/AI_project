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
    Pair heuristic;
    Pair cost;


    public Node(Pair position, int time, int remCap, HashMap<Pair, Ship> ships, Node parent, int boxes, int saved) {
        this.position = position;
        this.time = time;
        this.remCap = remCap;
        this.ships = ships;
        this.parent = parent;
        this.boxes= boxes;
        this.saved=saved;
        this.heuristic=new Pair(0,0);
        this.cost=new Pair(0,0);
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
                (parent ==null?"":(", parent pos"+ parent.position ))+
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return (this.cost.add(this.heuristic)).compareTo(o.cost.add(o.heuristic));
    }
}
