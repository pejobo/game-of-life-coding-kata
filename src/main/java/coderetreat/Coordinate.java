package coderetreat;

import java.util.stream.Stream;

public interface Coordinate<T extends Coordinate> {

    Stream<Coordinate<T>> getNeighbours();

    T move(T delta);

}
