package coderetreat;

public interface Rules {

    <T extends Coordinate<T>> boolean willBeAliveInNextGeneration(GameOfLife<T> game, Coordinate<T> coordinate);

}
