package code;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class CoastGuard extends GenericSearch{
    static int passengers=0; // total number of passengers on all ships to calc el deaths
    static HashSet<Pair>stations;
    static int n;
    static int m;
    static int [] dx = {-1,1,0,0};
    static int [] dy = {0,0,-1,1};
    static int maxCapacity; // do not change ya gama3a!!
    public static void main(String[] args) {
        String grid=genGrid();
        solve(grid,"BF",false);
        solve(grid,"ID",false);
        solve(grid,"DF",false);
        solve(grid,"GR1",false);
        solve(grid,"GR2",false);
        solve(grid,"AS1",false);
        solve(grid,"AS2",false);
    }

    public static String solve(String grid, String strategy, boolean visualize) {
        Node initialNode = decode(grid);
        switch (strategy) {
            case ("BF"): {
                bfs(initialNode);
                break;
            }
            case ("ID"): {
                iterDeep(initialNode);
                break;
            }
            case ("DF"): {
                dfs(initialNode, (int) 1e9);
                break;
            }
            case ("GR1"): {
                greedy(initialNode, 1);
                break;
            }
            case ("GR2"): {
                greedy(initialNode, 2);
                break;
            }
            case ("AS1"): {
                Astar(initialNode, 1);
                break;
            }
            case ("AS2"): {
                Astar(initialNode, 2);
                break;
            }
        }
            return "";

    }
    public static String genGrid(){
        int m = (int) (Math.random()*11)+5;
        int n = (int) (Math.random()*11)+5;
        int[][]grid = new int[m][n];

        return "";
    }
    public static Node decode(String s){
        passengers= 0; //count all passengers
        String [] split = s.split(";");
        HashMap<Pair,Ship> ships = new HashMap<>();
        stations = new HashSet<>();
        int capacity=0;
        int initialX=0;
        int initialY=0;
        for(int  i=0;i< split.length;i++){
            if(split[i].length()==0)continue;
            String [] entity = split[i].split(",");
            if(i==0){
                n = Integer.parseInt(entity[0]);
                m = Integer.parseInt(entity[1]);
//                System.out.println("rows: "+n+" columns: "+m);
                continue;
            }
            if(i==1){
                capacity = Integer.parseInt(entity[0]);
                maxCapacity=capacity;
//                System.out.println("capacity: "+capacity);
                continue;
            }
            if(i==2){
                initialX = Integer.parseInt(entity[0]);
                initialY = Integer.parseInt(entity[1]);
//                System.out.println("initialX: "+initialX+" initialX: "+initialX);
                continue;
            }

            int x = Integer.parseInt(entity[0]);
            int y = Integer.parseInt(entity[1]);
            if(entity.length>2){
                int shipCapacity = Integer.parseInt(entity[2]);
                passengers+=shipCapacity;
                Ship ship = new Ship(0,shipCapacity);
                ships.put(new Pair(x,y),ship);
//                System.out.println("Ship position: "+x+" "+y+" Capacity: "+shipCapacity);
            }else{
                stations.add(new Pair(x,y));
//                System.out.println("Station position: "+x+" "+y);
            }
        }
        return new Node(new Pair(initialX,initialY),0,capacity,ships,null,0,0);
//        System.out.println("Total number of passengers "+passengers);
    }


    public static Node bfs(Node node) {
        Queue<Node> q= new LinkedList<>();
        q.add(node);
        while (!q.isEmpty()){
            Node n = q.poll();
            if(isGoal(n)){
                return n;
            }
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)&&(n.parent.position).compareTo(newPosition)!=0){
                    q.add(new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved));
                }
            }
            // ship, station, box
            if(ships.containsKey(pos)){
                int remPassengers=0;//todo change

                if(remPassengers <= 0){ // retrieve box and wreck ship

                }else{//my cap pickup

                }
                continue;
            }
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff
                q.add(new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity)));
            }
        }
        return null;
    }
    public static Node retrieve(){
        return null;
    }
    public static Node pickUp(){
        return null;
    }
    public static boolean isGoal(Node n){
        return true;
    }
    public static String backTrack(){
        return "";
    }
    private static boolean isValid(Pair pos){
        return pos.x>=0&&pos.y>=0&&pos.x<n&&pos.y<m;
    }

    public static Node dfs(Node node, int limit) {
        return null;
    }

    public static Node iterDeep(Node node) {
        //for loop that calls dfs() with a greater depth every time
        return null;
    }

    public static Node greedy(Node node, int heuristic) {
        return null;
    }


    public static Node Astar(Node node, int heuristic) {
        return null;

    }
    public int heuristic1(Node node){
        return 0;
    }

    public int heuristic2(Node node){
        return 0;
    }
}