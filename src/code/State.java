package code;

import java.util.HashSet;
import java.util.Objects;

public class State {
    Pair pos;
    int savedSoFar;
    int boxes;
    int remCap;
    int time;

    public State(Pair pos, int remCap,int savedSoFar, int boxes, int time){
        this.pos=pos;
        this.savedSoFar=savedSoFar;
        this.boxes=boxes;
        this.remCap= remCap;
        this.time=time;
    }

    @Override
    public boolean equals(Object o) { // Adding a condition on time todo: check the condition
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return savedSoFar == state.savedSoFar && boxes == state.boxes && remCap == state.remCap && time >= state.time && pos.equals(state.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, savedSoFar, boxes, remCap);
    }

    @Override
    public String toString() {
        return "State{" +
                "pos=" + pos +
                ", savedSoFar=" + savedSoFar +
                ", boxes=" + boxes +
                ", remCap=" + remCap +
                '}';
    }

    // just for testing todo: remove
    // 3alatool beyd5ol ela law el time ad el time bezabt msh 3arfa leh ??
    public static void main(String[] args) {
        HashSet<State>s= new HashSet<>();
        s.add(new State(new Pair(1,1),1,1,1,4));
        State state=new State(new Pair(1,1),1,1,1,5);
        if(!s.contains(state)){
            System.out.println(" not equal");
            s.add(state);

        }
        else System.out.println("equal");
        System.out.println(s.size());
    }
}
