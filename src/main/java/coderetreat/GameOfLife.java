package coderetreat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class GameOfLife<T extends Coordinate<T>> {

    private Rules _rules;
    private Class<T> _coordinateType;
    private Set<Coordinate<T>> _board;
    private Set<Coordinate<T>> _previousBoard;

    GameOfLife(Rules rules, Class<T> coordinateType) {
        _rules = rules;
        _coordinateType = coordinateType;
        _board = Collections.newSetFromMap(new ConcurrentHashMap<>());
        _previousBoard = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }


    private Coordinate<T> checkType(Coordinate<T> coordinate) {
        return _coordinateType.cast(coordinate);
    }


    public boolean willBeAliveInNextGeneration(Coordinate<T> coordinate) {
        return _rules.willBeAliveInNextGeneration(this, checkType(coordinate));
    }

    public boolean isAlive(Coordinate<T> coordinate) {
        return _board.contains(checkType(coordinate));
    }


    public boolean isDead(Coordinate<T> coordinate) {
        return ! isAlive(checkType(coordinate));
    }


    public void setAlive(Coordinate<T> coordinate) {
        _board.add(checkType(coordinate));
    }


    public Stream<Coordinate<T>> getLivingCells() {
        return _board.stream();
    }


    public int getNumberOfLivingCells() {
        return _board.size();
    }


    public void tic(int numberOfGenerations) {
        for (int i = 0; i < numberOfGenerations; i++) {
            tic();
        }
    }


    /**
     * Calculate the period of the configuration by advancing the world until a world identical
     * to the current state is found or the specified maximum amount of tics is exceeded - whichever
     * comes first.
     *
     * @param maxTics maximum number of tics
     * @return {@code -1} if the configuration isn't periodic or the period is bigger than the
     * specified {@code maxTics} value.
     */
    public int calculatePeriod(final int maxTics) {
        final Set<Coordinate<T>> comparisonWorld = new HashSet<>(_board);
        for (int i = 1; i <= maxTics; i++) {
            tic();
            if ((comparisonWorld.size() == _board.size()) && comparisonWorld.containsAll(_board)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Shortcut for {@code {@link #calculatePeriod(int) calculatePeriod(1)} == 1}.
     * Side effect: Will advance the current world one '{@link #tic() tic}'.
     *
     * @return {@code true} when the current world is static.
     */
    public boolean isStatic() {
        return calculatePeriod(1) == 1;
    }


    public void tic() {
        // reuse last generation
        Set<Coordinate<T>> newBoard = _previousBoard;
        newBoard.clear();
        // surviving cells
        getLivingCells()
                .filter(this::willBeAliveInNextGeneration)
                .forEach(newBoard::add);
        // new-born cells
        getLivingCells()
                .flatMap(Coordinate::getNeighbours)
                .filter(this::isDead)
                .filter(coordinate -> ! newBoard.contains(coordinate)) // prevent neighbour calculation for cells where is already known that they come to life
                .filter(c -> _rules.willBeAliveInNextGeneration(this, c))
                .forEach(newBoard::add);
        // save last generation
        _previousBoard = _board;
        // new generation
        _board = newBoard;
    }

}
