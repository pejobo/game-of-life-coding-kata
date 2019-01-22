package coderetreat.view;

/**
 * This interface provides access to all information the
 * {@link coderetreat.view.GameOfLifeRenderer} needs to draw the current state's cells.
 */
public interface Cell {

    /**
     * @return true if the cell is alive (and should be rendered)
     */
    boolean isAlive();

    /**
     * @return the x-coordinate of the cell
     */
    int getX();

    /**
     * @return the y-coordinate of the cell
     */
    int getY();
}
