package code;

public class CoastGuard implements GenericSearch{
    static int passengers=0;
    static int rows=0;   //?
    static int columns=0; //?
    static int boatCapacity;
    static int initX;
    static int initY;

    public static void main(String[] args) {
        CoastGuard cg=new CoastGuard();
        String grid=genGrid();
        cg.solve(grid,"BF",false);
        cg.solve(grid,"ID",false);
        cg.solve(grid,"BF",false);
        cg.solve(grid,"DF",false);
        cg.solve(grid,"GR1",false);
        cg.solve(grid,"GR2",false);
        cg.solve(grid,"AS1",false);
        cg.solve(grid,"AS2",false);
    }

    public static String solve(String grid, String strategy, boolean visualize){
        int[][] gridArr=decode(grid);

        return "";
    }
    public static String genGrid(){
        int m = (int) (Math.random()*11)+5;
        int n = (int) (Math.random()*11)+5;
        int[][]grid = new int[m][n];

        return "";
    }
    public static int[][] decode(String s){
        passengers= 0; //count all passengers

        return null;
    }

    @Override
    public void bfs(Node node) {

    }

    @Override
    public void dfs(Node node, int limit) {

    }

    @Override
    public void iterDeep(Node node) {

    }

    @Override
    public void greedy(Node node, int heuristic) {

    }

    @Override
    public void Astar(Node node, int heuristic) {

    }
    public int heuristic1(Node node){
        return 0;
    }

    public int heuristic2(Node node){
        return 0;
    }
}