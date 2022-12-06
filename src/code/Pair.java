package code;

import java.util.Objects;

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
    public Pair add (Pair o){
        return new Pair(this.x+o.x,this.y+o.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return x == pair.x && y == pair.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
