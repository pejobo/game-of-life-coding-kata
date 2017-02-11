package coderetreat;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public interface World<T extends Coordinate<T>> extends Cloneable {

    Stream<T> getLivingCells();

    void setCellAlive(T coordinate);

    void killCell(T coordinate);

    World<T> clone();

    default boolean isIdenticalTo(final @NotNull World<T> world) {
        return (getNumberOfLivingCells() == world.getNumberOfLivingCells())
                && (isEmpty() || world.getLivingCells().noneMatch(this::isDead));
    }

    default World<T> tick(final @NotNull Rules<T> rules) {
        final World<T> newWorld = clone();
        tick(rules, newWorld);
        return newWorld;
    }

    default void tick(final @NotNull Rules<T> rules, final @NotNull World<T> newWorld) {
        newWorld.clear();
        if (getNumberOfLivingCells() == 0) {
            return;
        }
        // prevent duplicate checking
        final Set<T> cellsChecked = new HashSet<>((int) getNumberOfLivingCells() * 3);
        // add surviving cells to new world
        getLivingCells()
                .filter(cell -> rules.willBeAliveInNextGeneration(this, cell))
                .forEach(newWorld::setCellAlive);
        // add new-born cells to new world
        getLivingCells()
                .flatMap(Coordinate::getNeighbours)
                .filter(this::isDead)
                .filter(cellsChecked::add) // prevent duplicate checking
                .filter(c -> rules.willBeAliveInNextGeneration(this, c))
                .forEach(newWorld::setCellAlive);
    }

    default void clear() {
        getLivingCells().forEach(this::killCell);
    }

    default boolean isAlive(final @NotNull T coordinate) {
        return getLivingCells().anyMatch(coordinate::equals);
    }

    default boolean isDead(final @NotNull T coordinate) {
        return ! isAlive(coordinate);
    }

    default long getNumberOfLivingCells() {
        return getLivingCells().count();
    }

    default boolean isEmpty() {
        return 0L == getNumberOfLivingCells();
    }

}
