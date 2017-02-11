package coderetreat.conway;

import coderetreat.Rules;
import coderetreat.World;

public class ConwaysRules implements Rules<Point2D> {

    @Override
    public boolean willBeAliveInNextGeneration(World<Point2D> world, Point2D point) {
        final long livingCells =
                point
                .getNeighbours()
                .filter(world::isAlive)
                .limit(4) // if there are already 4 known new cells there is no advantage in collecting more..
                .count();
        // some clever boolean logic here ;)
        return (livingCells == 3) || ((livingCells == 2) && world.isAlive(point));
    }

}
