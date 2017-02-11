package coderetreat;

import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class GameOfLife<T extends Coordinate<T>> {

    private Rules<T> _rules;
    private Class<T> _coordinateType;
    private World<T> _world;
    private World<T> _previousWorld;

    GameOfLife(Class<T> coordinateType, Rules<T> rules, World<T> initialWorld) {
        _rules = rules;
        _coordinateType = coordinateType;
        _world = initialWorld;
        _previousWorld = _world.clone();
    }


    private T checkType(T coordinate) {
        return _coordinateType.cast(coordinate);
    }


    public void setCellAlive(T coordinate) {
        _world.setCellAlive(checkType(coordinate));
    }


    public boolean isAlive(T coordinate) {
        return _world.isAlive(coordinate);
    }


    public Stream<T> getLivingCells() {
        return _world.getLivingCells();
    }


    public long getNumberOfLivingCells() {
        return _world.getNumberOfLivingCells();
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
        World<T> world = _world.clone();
        for (int i = 1; i <= maxTicks; i++) {
            world = world.tick(_rules);
            if (world.isIdenticalTo(_world)) {
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
        _world.tick(_rules, _previousWorld);
        final World<T> newWorld = _previousWorld;
        _previousWorld = _world;
        _world = newWorld;
    }

}
