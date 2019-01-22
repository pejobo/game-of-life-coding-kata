package coderetreat.runner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import coderetreat.conway.ConwaysRules;
import coderetreat.conway.Point2D;
import coderetreat.model.GameOfLifeState;
import coderetreat.model.WorldImpl;
import coderetreat.view.Cell;
import coderetreat.view.WorldState;

public class GameOfLife implements WorldState {
    private GameOfLifeState<Point2D> gameOfLifeState;

    GameOfLife() {
        gameOfLifeState = new GameOfLifeState<>(Point2D.class, new ConwaysRules(), new WorldImpl<>());
        initializeWorld(gameOfLifeState);
    }

    @Override
    public Set<Cell> getCells() {
        Stream<Point2D> livingCells = gameOfLifeState.getLivingCells();
        Set<Cell> cells = new HashSet<>();
        livingCells.forEach(point2D -> cells.add(new Cell() {
            @Override
            public boolean isAlive() {
                return gameOfLifeState.isAlive(point2D);
            }

            @Override
            public int getX() {
                return point2D.getX();
            }

            @Override
            public int getY() {
                return point2D.getY();
            }
        }));
        return cells;
    }

    @Override
    public boolean hasFinished() {
        return gameOfLifeState.isStatic();
    }

    @Override
    public void update() {
        gameOfLifeState.tick();
    }

    private static void initializeWorld(GameOfLifeState<Point2D> gameOfLife) {
        //adds a blinker at 20:20
        gameOfLife.setCellAlive(Point2D.at(20, 20));
        gameOfLife.setCellAlive(Point2D.at(20, 21));
        gameOfLife.setCellAlive(Point2D.at(20, 22));
    }
}
