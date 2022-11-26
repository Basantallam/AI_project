package code;
import java.util.*;

public class CoastGuard extends GenericSearch{
    static int passengers=0; // total number of passengers on all ships to calc el deaths
    static HashSet<Pair>stations;
    static int n;
    static int m;
    static int [] dx = {-1,1,0,0};
    static int [] dy = {0,0,-1,1};
    static int maxCapacity; // do not change ya gama3a!!
    public static void main(String[] args) {
//        String grid=genGrid();
//        solve(grid,"BF",false);
//        solve(grid,"ID",false);
//        solve(grid,"DF",false);
//        solve(grid,"GR1",false);
//        solve(grid,"GR2",false);
//        solve(grid,"AS1",false);
//        solve(grid,"AS2",false);

        solve("2,2;5;0,0;1,0;1,1,20","BF",false);
    }

    public static String solve(String grid, String strategy, boolean visualize) {
        Node initialNode = decode(grid);
        switch (strategy) {
            case ("BF"): {
               backTrack( bfs(initialNode));
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
        StringBuilder sb=new StringBuilder();
        int m = (int) (Math.random()*11)+5;
        int n = (int) (Math.random()*11)+5;
        int c = (int) (Math.random()*71)+30;
        int cx=(int) (Math.random()*m);
        int cy=(int) (Math.random()*n);
        sb.append(m+","+n+";"+c+cx+cy);
        int maxTotal =m*n;
        int stationsNo= (int) (Math.random()*maxTotal-2)+1;
        int passNo= (int) (Math.random()*maxTotal-stationsNo-1)+1;
        //todo complete

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
//            System.out.println(n);
            if(isGoal(n)){
                return n;
            }
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
//                    &&(n.parent.position).compareTo(newPosition)!=0 //todo reduce redundant state esp in dfs
                    q.add(new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved));
                }
            }
            // ship, station, box
            if(ships.containsKey(pos)){//todo check time  w rempass relation

                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= -20){ // wreck ship
                    ships.remove(pos);
                }else if(remPassengers<=0){//retrieve black box and remove ship
                    q.add(new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved));
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);
                        q.add(childPickUp);
                    }
                }
                continue;
            }
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                q.add(new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity)));
            }
        }
        return null;
    }

    private static Node pickUp(Node n, Pair pos, int time, int remCapacity, HashMap<Pair, Ship> ships, int remPassengers) {//I am pickingUp at my state at time: time
        int takenPassengers=Math.min(remPassengers, remCapacity);
        Ship newShip = new Ship(time, remPassengers -takenPassengers);//todo check time wala time+1
        Node childPickUp =new Node(pos, time +1, remCapacity -takenPassengers,deepCloneShip(ships, pos,newShip), n, n.boxes, n.saved);//removed saved as it is calculated in dropoff
        return childPickUp;
    }

    static HashMap<Pair,Ship> deepCloneShip(HashMap<Pair,Ship> original,Pair pos,Ship newShip){//will clone hm and newship
        HashMap<Pair,Ship> hm = new HashMap<>();
        for (Pair key: original.keySet()) {
            hm.put(key,original.get(key));
        }
       if(newShip!= null) {// add  new ship
            hm.put(pos,newShip);
       }else
           hm.remove(pos);
        return hm;
    }

    public static boolean isGoal(Node n){
        if(n.remCap!=maxCapacity)return false;
        boolean res=true;
        ArrayList<Pair> toRemove=new ArrayList<>();
        for (Pair key: n.ships.keySet()) {
            Ship s =n.ships.get(key);
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            if(remPassengers <= -20)toRemove.add(key);
            else {
                res=false;
                break;
            }
        }
        for (Pair key: toRemove) {
            n.ships.remove(key);
        }
        return res;
    }
    public static String backTrack(Node n){
        //todo plan;deaths;retrieved;nodes refer to sheet
       while(n!=null){
           System.out.println(n.toString());
           n=n.parent;
       }
        return "";
    }
    private static boolean isValid(Pair pos){
        return pos.x>=0&&pos.y>=0&&pos.x<n&&pos.y<m;
    }

    public static Node dfs(Node n, int limit) {
        return null;
    }

    public static Node iterDeep(Node n) {
        //for loop that calls dfs() with a greater depth every time
        return null;
    }

    public static Node greedy(Node node, int heuristicChoice) {
        node.heuristic=heuristicChoice==1?heuristic1(node): heuristic2(node);
        PriorityQueue<Node> pq=new PriorityQueue<>();
        pq.add(node);
        while(!pq.isEmpty()){

        }
        return null;
    }


    public static Node Astar(Node node, int heuristicChoice) {
        return null;

    }
    private static int heuristic1(Node n){
        int res=0;
        int maxNoPassengers=-20;
        for (Ship s: n.ships.values()) {
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            Math.max(maxNoPassengers,remPassengers);
            if(remPassengers>0)
                res+=4;
            else if(remPassengers>-20)res+=1;
        }
        res=Math.min(res,maxNoPassengers+20);
        if(res ==0) if(n.remCap!=maxCapacity)res=1;
        return res;
    }

    private static int heuristic2(Node n){ //similar but dominates heuristic 1
        int res=0;
        int maxNoPassengers=-20;
        for (Ship s: n.ships.values()) {
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            Math.max(maxNoPassengers,remPassengers);
            if(remPassengers>0){
                int pickupTimes=(remPassengers+maxCapacity+1)/maxCapacity;
                res+=pickupTimes*3+1;
            }

            else if(remPassengers>-20)res+=1;
        }
        res=Math.min(res,maxNoPassengers+20);
        if(res ==0) if(n.remCap!=maxCapacity)res=1;
        return res;
    }
}