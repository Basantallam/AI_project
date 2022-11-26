package code;

public class CoastGuard extends GenericSearch{
    static int passengers=0;
    static int rows=0;
    static int columns=0;
    static int boatCapacity;
    static int initX;
    static int initY;

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
        int[][] gridArr = decode(grid);
        Node initialNode = new Node(initX, initY);
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
    public static int[][] decode(String s){

        return null;
    }


    public static void bfs(Node node) {

    }


    public static void dfs(Node node, int limit) {

    }

    public static void iterDeep(Node node) {
        //for loop that calls dfs() with a greater depth every time
    }

    public static void greedy(Node node, int heuristic) {

    }


    public static void Astar(Node node, int heuristic) {

    }
    public int heuristic1(Node node){
        return 0;
    }

    public int heuristic2(Node node){
        return 0;
    }
}