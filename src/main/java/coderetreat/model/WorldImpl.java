package coderetreat.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class WorldImpl<T extends Coordinate<T>> implements World<T> {

    private final Set<T> _livingCells;

    public WorldImpl() {
        this(Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    private WorldImpl(Set<T> livingCells) {
        _livingCells = livingCells;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public World<T> clone() {
        final Set<T> livingCells = Collections.newSetFromMap(new ConcurrentHashMap<>(_livingCells.size()));
        livingCells.addAll(_livingCells);
        return new WorldImpl<>(livingCells);
    }

    @Override
    public void clear() {
        _livingCells.clear();
    }

    @Override
    public Stream<T> getLivingCells() {
        return _livingCells.stream();
    }

    @Override
    public boolean isAlive(@NotNull T coordinate) {
        return _livingCells.contains(coordinate);
    }

    @Override
    public void setCellAlive(T coordinate) {
        _livingCells.add(coordinate);
    }

    @Override
    public void killCell(T coordinate) {
        _livingCells.remove(coordinate);
    }

    @Override
    public long getNumberOfLivingCells() {
        return _livingCells.size();
    }

    @Override
    public boolean isEmpty() {
        return _livingCells.isEmpty();
    }
}
