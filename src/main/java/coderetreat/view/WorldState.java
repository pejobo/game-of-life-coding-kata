package coderetreat.view;

import java.util.Set;

/**
 * A WorldState provides access to all information the
 * {@link coderetreat.view.GameOfLifeRenderer} needs to draw the current game state.
 */
public interface WorldState {

    /**
     * The rendering component needs to know each cell that should be rendered.
     * @return a set of all relevant cells
     */
    Set<Cell> getCells();

    /**
     * If this method returns true, the {@link coderetreat.view.GameOfLifeRenderer}will stop rendering.
     * @return true if the simulation has finished (false otherwise)
     */
    boolean hasFinished();

    /**
     * The game state should update(=tick) each call of the rendering loop
     */
    void update();
}
