package coderetreat.conway;

import coderetreat.Coordinate;
import coderetreat.GameOfLife;
import coderetreat.Rules;

@SuppressWarnings("SpellCheckingInspection")
public class ConwaysRules implements Rules {

    @Override
    public <T extends Coordinate<T>> boolean willBeAliveInNextGeneration(GameOfLife<T> game, Coordinate<T> coordinate) {
        final long livingCells =
                coordinate
                .getNeighbours()
                .filter(game::isAlive)
                .limit(4) // if there are already 4 known new cells there is no advantage in collecting more..
                .count();
        // some clever boolean logic here ;)
        return (livingCells == 3) || ((livingCells == 2) && game.isAlive(coordinate));
    }

}
