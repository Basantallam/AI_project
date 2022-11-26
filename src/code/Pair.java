package code;

public class Pair implements Comparable<Pair>{
    int x ;
    int y ;

    public Pair(int x, int y){
        this.x=x;
        this.y=y;
    }

    @Override
    public int compareTo(Pair o) {
        if(this.x==o.x)return this.y-o.y;
        return this.x-o.x;
    }
}
