package coderetreat;

public interface Rules<T extends Coordinate<T>> {

    boolean willBeAliveInNextGeneration(World<T> world, T coordinate);

}
