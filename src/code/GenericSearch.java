package code;

public interface GenericSearch {
    public void bfs(Node node);
    public void dfs(Node node, int limit);
    public void iterDeep(Node node);
    public void greedy(Node node, int heuristic);
    public void Astar(Node node, int heuristic);


}
