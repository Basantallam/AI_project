package code;

import java.util.Objects;

public class State {
    Pair pos;
    int savedSoFar;
    int boxes;
    int remCap;

    public State(Pair pos, int remCap,int savedSoFar, int boxes){
        this.pos=pos;
        this.savedSoFar=savedSoFar;
        this.boxes=boxes;
        this.remCap= remCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return savedSoFar == state.savedSoFar && boxes == state.boxes && remCap == state.remCap && pos.equals(state.pos);
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
