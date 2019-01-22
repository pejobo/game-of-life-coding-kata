package coderetreat.view;

import java.util.Set;

public interface WorldState {
    Set<Cell> getCells();
    boolean hasFinished();
    void update();
}
