package coderetreat.runner;

import coderetreat.view.GameOfLifeRenderer;

public class GameOfLifeExecutor {

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        GameOfLifeRenderer renderer = new GameOfLifeRenderer(200, 200, game, 2);

        while (!game.hasFinished()) {
            renderer.render();
            game.update();
        }
    }
}
