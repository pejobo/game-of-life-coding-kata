package coderetreat.runner;

import coderetreat.view.GameOfLifeRenderer;

public class GameOfLiveExecutor {

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        GameOfLifeRenderer renderer = new GameOfLifeRenderer(2000, 100, game);

        while (!game.hasFinished()) {
            renderer.render();
            game.update();
        }
    }
}
