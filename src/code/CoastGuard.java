package code;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CoastGuard extends GenericSearch{
    static int passengers=0; // total number of passengers on all ships to calc el deaths
    static HashSet<Pair>stations;
    static int n;
    static int m;
    static int [] dx = {-1,1,0,0};
    static int [] dy = {0,0,-1,1};
    static int maxCapacity; // do not change ya gama3a!!
    static PrintWriter pw = new PrintWriter(System.out);
    static HashSet<State>vis ;
    static int expand=0; // number of expanded nodes

    final static int wreckTime =-19;
    public static void main(String[] args) throws IOException {
//        String grid=genGrid();
//        solve(grid,"BF",false);
//        solve(grid,"ID",false);
//        solve(grid,"DF",false);
//        solve(grid,"GR1",false);
//        solve(grid,"GR2",false);
//        solve(grid,"AS1",false);
//        solve(grid,"AS2",false);
        String grid0 = "5,6;50;0,1;0,4,3,3;1,1,90;";
        String gridTest = "3,2;50;0,1;0,2,1,2;1,1,90;";
        String grid2X2="2,2;100;0,0;1,0;1,1,20;";
        System.out.println(genGrid());
//        decode(gridTest);

        System.out.println(solve(grid2X2,"BF",false));

//        testing backtrack
//        Node grandpa=new Node(new Pair(5,6),5,0,null,null,10,6);
//        Node parent= new Node(new Pair(5,6),6,1,null,grandpa,10,6);
//        Node lastNode=new Node(new Pair(6,6),7,1,null,parent,10,6);
//        System.out.println(backTrack(lastNode));
        pw.flush();pw.close();
    }
    public static String solve(String grid, String strategy, boolean visualize) {
        Node initialNode = decode(grid);
        vis = new HashSet<>();
        switch (strategy) {
            case ("BF"): {
                return backTrack(bfs(initialNode),visualize);
            }
            case ("ID"): {
                return backTrack(iterDeep(initialNode),visualize);
            }
            case ("DF"): {
                return backTrack(dfs(initialNode, (int) 1e9),visualize);
            }
            case ("GR1"): {
                return backTrack(greedy(initialNode, 1),visualize);
            }
            case ("GR2"): {
                return backTrack(greedy(initialNode, 2),visualize);
            }
            case ("AS1"): {
                return backTrack(Astar(initialNode, 1),visualize);
            }
            case ("AS2"): {
                return backTrack(Astar(initialNode, 2),visualize);
            }
            case ("UC"): {
                return backTrack(UniformCost(initialNode),visualize);
            }
        }
        pw.flush();pw.close();
            return "";
    }

    public static String genGrid(){
        StringBuilder sb=new StringBuilder();
        int m = (int) (Math.random()*11)+5;
        int n = (int) (Math.random()*11)+5;
        int c = (int) (Math.random()*71)+30;
        int maxTotal =m*n;
        int shipNo= (int) (Math.random()*(maxTotal-2))+1;
        int stationsNo= (int) (Math.random()*(maxTotal-shipNo-1))+1;
        System.out.println("stations: "+stationsNo+" ships: "+shipNo);
        int pos= (int) (Math.random()*maxTotal);
        sb.append(m+","+n+";"+c+";"+pos/n+","+pos%n+";");
        boolean[] vis = new boolean[maxTotal];
        vis[pos]=true;
        while(stationsNo>0){
            pos= (int) (Math.random()*maxTotal);
            if(vis[pos])continue;
            vis[pos]=true;
            sb.append(pos/n+","+pos%n+",");
            stationsNo--;
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        while(shipNo>0){
            pos= (int) (Math.random()*maxTotal);
            int capacity = (int)(Math.random()*(100))+1;
            if(vis[pos])continue;
            vis[pos]=true;
            sb.append(pos/n+","+pos%n+","+capacity+",");
            shipNo--;
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        return sb.toString();
    }

    private static Node decode(String s){
        passengers= 0; //count all passengers
        String [] split = s.split(";");
        HashMap<Pair,Ship> ships = new HashMap<>();
        stations = new HashSet<>();
        int capacity=0;
        int initialX=0;
        int initialY=0;
        //String gridTest = "3,2;50;0,1;0,2,1,2;1,1,90;";
        for(int  i=0;i< 5;i++){//todo change
            if(split[i].length()==0)continue;
            String [] entity = split[i].split(",");
            switch (i){
                case 0://todo check grid dimensions
                    m = Integer.parseInt(entity[0]);
                    n = Integer.parseInt(entity[1]);
//                    System.out.println("rows: "+n+" columns: "+m);
                    break;
                case 1:
                    capacity = Integer.parseInt(entity[0]);
                    maxCapacity=capacity;
//                    System.out.println("capacity: "+capacity);
                    break;
                case 2://agent place
                    initialX = Integer.parseInt(entity[0]);
                    initialY = Integer.parseInt(entity[1]);
//                    System.out.println("initialX: "+initialX+" initialY: "+initialY);
                    break;
                case 3:
                    for (int j = 0; j < entity.length; j+=2) {
                        int x = Integer.parseInt(entity[j]);
                        int y = Integer.parseInt(entity[j+1]);
                        stations.add(new Pair(x,y));
//                        System.out.println("Station position: "+x+" "+y);
                    }break;
                case 4://ships
                    for (int j = 0; j < entity.length; j+=3) {
                        int x = Integer.parseInt(entity[j]);
                        int y = Integer.parseInt(entity[j+1]);
                        int shipCapacity = Integer.parseInt(entity[j+2]);
                        passengers+=shipCapacity;
                        Ship ship = new Ship(0,shipCapacity);
                        ships.put(new Pair(x,y),ship);
//                        System.out.println("Ship position: "+x+" "+y+" Capacity: "+shipCapacity);
                    }break;
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
            if(vis.contains(new State(n.position,n.remCap,n.saved,n.boxes,n.time))){
                continue;
            }
            expand++;
            vis.add(new State(n.position,n.remCap,n.saved,n.boxes,n.time));
            //naming
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            // ship, box
            if(ships.containsKey(pos)){//todo check time  w rempass relation
                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= wreckTime){ // wreck ship
                    n.ships=deepCloneShip(ships,pos,null);
                }else if(remPassengers<=0){//retrieve black box and remove ship //todo check
                        q.add(new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved));
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);
                        q.add(childPickUp);
                    }
                }
            }
            else//station
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                    q.add(new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity)));

            }
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
                    q.add(new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved));
                }
            }
        }
        return null;
    }

    private static Node pickUp(Node n, Pair pos, int time, int remCapacity, HashMap<Pair, Ship> ships, int remPassengers) {//I am pickingUp at my state at time: time
        int takenPassengers=Math.min(remPassengers, remCapacity);
        Ship newShip = new Ship(takenPassengers==remPassengers?time+1:time, remPassengers -takenPassengers);//todo check time wala time+1
//        Ship newShip = new Ship(time, remPassengers -takenPassengers);//todo check time wala time+1
        Node childPickUp =new Node(pos, time +1, remCapacity -takenPassengers,deepCloneShip(ships, pos,newShip), n, n.boxes, n.saved);//removed saved as it is calculated in dropoff
//        pw.println("child : pickup ,remCapacity: "+childPickUp.remCap+" , shipPassenger :"+newShip.remPass);
        return childPickUp;
    }

    private static HashMap<Pair,Ship> deepCloneShip(HashMap<Pair,Ship> original,Pair pos,Ship newShip){//will clone hm and newship
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

    private static boolean isGoal(Node n){
        if(n.remCap!=maxCapacity)return false;
        boolean res=true;
        ArrayList<Pair> toRemove=new ArrayList<>();
        for (Pair key: n.ships.keySet()) {
            Ship s =n.ships.get(key);
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            if(remPassengers <= wreckTime)toRemove.add(key);
            else {
                res=false;
                break;
            }
        }

        for (Pair key: toRemove) {
            n.ships=deepCloneShip(n.ships,key,null);
        }
        return res;

    }

    private static String backTrack(Node n,boolean visualize){
         Stack<StringBuilder> stackViz =new Stack<>();
        Node finalNode=n;

        //todo plan;deaths;retrieved;nodes refer to sheet
        StringBuilder sb= new StringBuilder("");
        StringBuilder sbDebug= new StringBuilder("");
        int nodes=1;
       while(n.parent!=null){
           StringBuilder action=findAction(n.parent,n);
           sb.append(action.reverse());
           if(visualize) {stackViz.add(visualize(n));
           stackViz.add(new StringBuilder("\naction taken :").append(action.reverse().append("\n\n")));
           }
//           String pos="("+n.parent.position.x+","+n.parent.position.y+")";
//           sbDebug.append(findAction(n.parent,n).reverse());
//           sbDebug.append(new StringBuilder(pos).reverse());
           n=n.parent;
           if(n.parent!=null){
               sb.append(",");
//               sbDebug.append(",");
           }
           nodes++;
       }
       if(visualize) stackViz.add(visualize(n));
       String plan=sb.reverse()+"";
       int deaths=calcDeaths(finalNode);
       int retrieved=finalNode.boxes;


       StringBuilder print =new StringBuilder("");
        while (!stackViz.isEmpty()) {
            print.append(stackViz.pop());
        }
        System.out.println(plan+";"+deaths+";"+retrieved+";"+expand);//todo remove print

        return plan+";"+deaths+";"+retrieved+";"+expand;
    }

    private static StringBuilder visualize(Node n) {
        StringBuilder print = new StringBuilder("");
        print.append("Time :"+n.time+"\n");
        print.append("Agent position :"+ n.position+", boat carrying :" +(maxCapacity-n.remCap)+"\n");

        boolean first =true;

        for (Pair pos:n.ships.keySet()){
            Ship s = n.ships.get(pos);
            if(s.exists(n.time)){
                if(first){
                    print.append("Ships at : ");
                    first=false;
                }
                else print.append(", ");
                print.append(pos+" => ");
                print.append(s.visualize(n.time));
            }
        }

        if (first)print.append("all ships are wrecked \n");
        else print.append("\n");


        return print;
    }

    private static StringBuilder findAction(Node parent, Node child){
        if(parent.remCap> child.remCap)
            return new StringBuilder("pickup");
        if(parent.remCap< child.remCap)
            return new StringBuilder("drop");
        if(parent.position.x<child.position.x)
            return new StringBuilder("down");
        if(parent.position.x>child.position.x)
            return new StringBuilder("up");
        if(parent.position.y<child.position.y)
            return new StringBuilder("right");
        if(parent.position.y>child.position.y)
            return new StringBuilder("left");
        else
            return new StringBuilder("retrieve");

    }

    private static int calcDeaths(Node node){
        return passengers-node.saved;
    }

    private static boolean isValid(Pair pos){
        return pos.x>=0&&pos.y>=0&&pos.x<n&&pos.y<m;
    }

    public static Node dfs(Node node, int limit) {
        Stack<Node> stack= new Stack<>();
        stack.add(node);
        vis = new HashSet<>();
        while (!stack.isEmpty()){
            Node n = stack.pop();
            if(isGoal(n)){
                return n;
            }
            if(vis.contains(new State(n.position,n.remCap,n.saved,n.boxes,n.time))){
                continue;
            }
            if(n.time==limit)continue;
            expand++;
            vis.add(new State(n.position,n.remCap,n.saved,n.boxes,n.time));
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            // ship, station, box
            if(ships.containsKey(pos)){//todo check time  w rempass relation
                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= wreckTime){ // wreck ship
                    n.ships=deepCloneShip(ships,pos,null);
                }else if(remPassengers<=0){//retrieve black box and remove ship
                    stack.add(new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved));
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);
                        stack.add(childPickUp);
                    }
                }
            }
            else
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                stack.add(new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity)));

            }
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
                    stack.add(new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved));
                }
            }
        }
        return null;
    }

    public static Node iterDeep(Node node) {
        int limit=0;
        while (true){
           Node n = dfs(node,limit);
           limit++;
           if(n!=null)
               return n;
        }
    }

    public static Node greedy(Node node, int heuristicChoice) {
        node.heuristic=heuristicChoice==1?heuristic1(node): heuristic2(node);
        PriorityQueue<Node> pq=new PriorityQueue<>();
        pq.add(node);
        while (!pq.isEmpty()){
            Node n = pq.poll();
            if(isGoal(n)){
                return n;
            }
            if(vis.contains(new State(n.position,n.remCap,n.saved,n.boxes,n.time))){
                continue;
            }
            expand++;
            vis.add(new State(n.position,n.remCap,n.saved,n.boxes,n.time));

            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            // ship, , box
            if(ships.containsKey(pos)){

                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= wreckTime){ // wreck ship
                    n.ships = deepCloneShip(ships,pos,null);
                }else if(remPassengers<=0){//retrieve black box and remove ship
                    Node child=new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved);
                    child.heuristic=heuristicChoice==1?heuristic1(child): heuristic2(child);
                    pq.add(child);
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);
                        childPickUp.heuristic=heuristicChoice==1?heuristic1(childPickUp): heuristic2(childPickUp);
                        pq.add(childPickUp);
                    }
                }
            }
            else
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                Node child=new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity));
                child.heuristic=heuristicChoice==1?heuristic1(child): heuristic2(child);
                pq.add(child);
            }
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
                    Node child = new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved);
                    child.heuristic=heuristicChoice==1?heuristic1(child): heuristic2(child);
                    pq.add(child);
                }
            }

        }
        return null;
    }
    private static Pair cost(Node n){
        int death=0;
        int boxesLost=0;
        ArrayList<Pair> toRemove=new ArrayList<>();
        for (Pair key: n.ships.keySet()) {
            Ship s =n.ships.get(key);
            int remPassengers=s.remPass-(n.time+1-s.lastTimeStamp);
            if(remPassengers < wreckTime){
                toRemove.add(key);
            }
            else {
                if(remPassengers==wreckTime){
                    boxesLost++;
                } else if (remPassengers>=0)  {
                    death++;
                }
            }
        }
        for (Pair key: toRemove) {
            n.ships=deepCloneShip(n.ships,key,null);
        }
        return new Pair(death,boxesLost);
    }
    //old costs = pickup=1 drop=2 retrieve=3 pos=5
    public static Node Astar(Node node, int heuristicChoice) {
        //cost equal exp 1: pickup condition(pickedup all passenger ==>death-1) , 2: retrieve condition (time = -19 ==>black-1)
        node.heuristic=(heuristicChoice==1?heuristic1(node): heuristic2(node));

        PriorityQueue<Node> pq=new PriorityQueue<>();
        pq.add(node);
        while (!pq.isEmpty()){
            Node n = pq.poll();
            if(isGoal(n)){
                return n;
            }
            if(vis.contains(new State(n.position,n.remCap,n.saved,n.boxes,n.time))){
                continue;
            }
            expand++;
            vis.add(new State(n.position,n.remCap,n.saved,n.boxes,n.time));
            Pair cost = cost(n);
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            // ship, station, box
            if(ships.containsKey(pos)){//todo check time  w rempass relation

                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= wreckTime){ // wreck ship
                    n.ships=deepCloneShip(ships,pos,null);
                }else if(remPassengers<=0){//retrieve black box and remove ship
                    Node child=new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved);
                    child.heuristic=(heuristicChoice==1?heuristic1(child): heuristic2(child));
                    child.cost=(remPassengers==wreckTime-1?new Pair(cost.x,cost.y-1):cost).add(n.cost);
                    pq.add(child);
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);
                        childPickUp.heuristic=heuristicChoice==1?heuristic1(childPickUp): heuristic2(childPickUp);
                        Boolean tookAll=ships.get(pos).remPass==0;
                        childPickUp.cost=tookAll?new Pair(cost.x-1,cost.y):cost;
                        childPickUp.cost=childPickUp.cost.add(n.cost);
                        pq.add(childPickUp);
                    }
                }
            }
            else
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                Node child=new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity));
                child.heuristic=heuristicChoice==1?heuristic1(child): heuristic2(child);
                child.cost=n.cost.add(cost);
                pq.add(child);
            }
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
//                    &&(n.parent.position).compareTo(newPosition)!=0 //todo reduce redundant state esp in dfs
                    Node child = new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved);
                    child.heuristic=heuristicChoice==1?heuristic1(child): heuristic2(child);
                    child.cost=n.cost.add(cost);
                    pq.add(child);
                }
            }

        }
        return null;
    }
    public static Node UniformCost(Node node) {

        PriorityQueue<Node> pq=new PriorityQueue<>();
        pq.add(node);
        while (!pq.isEmpty()){
            Node n = pq.poll();

            if(isGoal(n)){
                return n;
            }
            if(vis.contains(new State(n.position,n.remCap,n.saved,n.boxes,n.time))){
                continue;
            }
            expand++;
            vis.add(new State(n.position,n.remCap,n.saved,n.boxes,n.time));
            Pair cost = cost(n);
            Pair pos = n.position;
            int time= n.time;
            int remCapacity= n.remCap;
            HashMap<Pair,Ship>ships= n.ships;
            // ship, box
            if(ships.containsKey(pos)){//todo check time  w rempass relation

                Ship s = ships.get(pos);
                int remPassengers=s.remPass-(time-s.lastTimeStamp);
                if(remPassengers <= wreckTime){ // wreck ship
                    n.ships=deepCloneShip(ships,pos,null);
                }else if(remPassengers<=0){//retrieve black box and remove ship
                    Node child=new Node(pos,time+1,remCapacity,deepCloneShip(ships,pos,null),n,n.boxes+1,n.saved);

                    child.cost=(remPassengers==wreckTime-1?new Pair(cost.x,cost.y-1):cost).add(n.cost);
                    pq.add(child);
                }
                else {//pickup
                    if(remCapacity>0){
                        Node childPickUp = pickUp(n, pos, time, remCapacity, ships, remPassengers);

                        Boolean tookAll=ships.get(pos).remPass==0;
                        childPickUp.cost=tookAll?new Pair(cost.x-1,cost.y):cost;
                        childPickUp.cost=childPickUp.cost.add(n.cost);
                        pq.add(childPickUp);
                    }
                }
            }
            else
            if(stations.contains(pos)&&remCapacity!=maxCapacity){ // dropOff and save the passengers on the boat
                Node child=new Node(pos,time+1,maxCapacity,ships,n,n.boxes,n.saved+(maxCapacity-remCapacity));

                child.cost=n.cost.add(cost);
                pq.add(child);
            }
            for(int i=0;i<4;i++){
                Pair newPosition = new Pair(pos.x+dx[i],pos.y+dy[i]);
                if(isValid(newPosition)){
                    Node child = new Node(newPosition,time+1,remCapacity,ships,n,n.boxes,n.saved);

                    child.cost=n.cost.add(cost);
                    pq.add(child);
                }
            }

        }
        return null;
    }

    private static int manhattanDistance(Pair p1, Pair p2){
        return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
    }
    private static int heuristic1Cool(Node n){
        int res=0;
        for (Ship s: n.ships.values()) {
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            Math.max(wreckTime,remPassengers);
            if(remPassengers>0)
                res+=4;
            else if(remPassengers>wreckTime)res+=1;
        }
        res=Math.min(res,wreckTime+20);
        if(res ==0) if(n.remCap!=maxCapacity)res=1;
        return res;
    }
   //cost = death,boxLost ==> h (death) h1,0 ,h(box) ==> 0,h2
    private static Pair heuristic1(Node n){
        int sum=0;
        for (Pair p: n.ships.keySet()) {
            Ship ship=n.ships.get(p);
            int remPassengers=ship.remPass-(n.time-ship.lastTimeStamp);
            int distToShip=manhattanDistance(n.position,p);
            sum+=Math.min(Math.max(0,remPassengers),distToShip);
        }
        return new Pair(sum,0);
    }

    private static Pair heuristic2(Node n){ // h(box) ==> 0,h2
        int sum=0;
        for (Pair p: n.ships.keySet()) {
            Ship ship=n.ships.get(p);
            int remPassengers=ship.remPass-(n.time-ship.lastTimeStamp);
            int distToShip=manhattanDistance(n.position,p);
            if(remPassengers-distToShip <= wreckTime)
                sum++;
        }
        return new Pair(0,sum);
    }
    private static int heuristic2Cool(Node n){ //similar but dominates heuristic 1
        int res=0;
        for (Ship s: n.ships.values()) {
            int remPassengers=s.remPass-(n.time-s.lastTimeStamp);
            Math.max(wreckTime,remPassengers);
            if(remPassengers>0){
                int pickupTimes=(remPassengers+maxCapacity+1)/maxCapacity;
                res+=pickupTimes*3+1;
            }

            else if(remPassengers>wreckTime)res+=1;
        }
        res=Math.min(res,wreckTime+20);
        if(res ==0) if(n.remCap!=maxCapacity)res=1;
        return res;
    }
}