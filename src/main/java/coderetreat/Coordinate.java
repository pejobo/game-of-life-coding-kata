package coderetreat;

import java.util.stream.Stream;

public interface Coordinate<T extends Coordinate> {

    Stream<T> getNeighbours();

    T move(T delta);

}
