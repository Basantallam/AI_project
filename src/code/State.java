package code;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return  pos.equals(state.pos) && savedSoFar == state.savedSoFar && boxes == state.boxes && remCap == state.remCap  && time >= state.time ;
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
}
