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
    private Set<Coordinate<T>> _world;
    private Set<Coordinate<T>> _previousWorld;

    GameOfLife(Rules rules, Class<T> coordinateType) {
        _rules = rules;
        _coordinateType = coordinateType;
        _world = Collections.newSetFromMap(new ConcurrentHashMap<>());
        _previousWorld = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }


    private Coordinate<T> checkType(Coordinate<T> coordinate) {
        return _coordinateType.cast(coordinate);
    }


    public boolean willBeAliveInNextGeneration(Coordinate<T> coordinate) {
        return _rules.willBeAliveInNextGeneration(this, checkType(coordinate));
    }

    public boolean isAlive(Coordinate<T> coordinate) {
        return _world.contains(checkType(coordinate));
    }


    public boolean isDead(Coordinate<T> coordinate) {
        return ! isAlive(checkType(coordinate));
    }


    public void setAlive(Coordinate<T> coordinate) {
        _world.add(checkType(coordinate));
    }


    public Stream<Coordinate<T>> getLivingCells() {
        return _world.stream();
    }


    public int getNumberOfLivingCells() {
        return _world.size();
    }


    public void tick(int numberOfGenerations) {
        for (int i = 0; i < numberOfGenerations; i++) {
            tick();
        }
    }


    /**
     * Calculate the period of the configuration by advancing the world until a world identical
     * to the current state is found or the specified maximum amount of ticks is exceeded - whichever
     * comes first.
     *
     * @param maxTicks maximum number of ticks
     * @return {@code -1} if the configuration isn't periodic or the period is bigger than the
     * specified {@code maxTicks} value.
     */
    public int calculatePeriod(final int maxTicks) {
        final Set<Coordinate<T>> comparisonWorld = new HashSet<>(_world);
        for (int i = 1; i <= maxTicks; i++) {
            tick();
            if ((comparisonWorld.size() == _world.size()) && comparisonWorld.containsAll(_world)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Shortcut for {@code {@link #calculatePeriod(int) calculatePeriod(1)} == 1}.
     * Side effect: Will advance the current world one '{@link #tick() tick}'.
     *
     * @return {@code true} when the current world is static.
     */
    public boolean isStatic() {
        return calculatePeriod(1) == 1;
    }


    public void tick() {
        if (_world.isEmpty()) {
            return;
        }
        // reuse last generation
        final Set<Coordinate<T>> newWorld = _previousWorld;
        newWorld.clear();
        // already checked (dead) cells
        final Set<Coordinate<T>> cellsChecked = new HashSet<>(_previousWorld.size() * 6);
        // add surviving cells to new world
        getLivingCells()
                .filter(this::willBeAliveInNextGeneration)
                .forEach(newWorld::add);
        // add new-born cells to new world
        getLivingCells()
                .flatMap(Coordinate::getNeighbours)
                .filter(this::isDead)
                .filter(cellsChecked::add) // prevent duplicate checking
                .filter(c -> _rules.willBeAliveInNextGeneration(this, c))
                .forEach(newWorld::add);
        // save last generation
        _previousWorld = _world;
        // new generation
        _world = newWorld;
    }

}
