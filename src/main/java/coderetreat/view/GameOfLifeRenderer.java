package coderetreat.view;

import java.awt.*;
import javax.swing.*;

public class GameOfLifeRenderer {
    private final RendererContainerFrame rendererContainerFrame;
    private final WorldState worldState;
    private final int scaleFactor;

    private Color backgroundColor;
    private Color foregroundColor;

    /**
     * Creates a renderer, which instantiates a game of life rendering frame.
     * @param width of the rendering component (in pixel)
     * @param height of the rendering component (in pixel)
     * @param worldState - an implementation of the {@link coderetreat.view.WorldState}
     * @param scaleFactor - the scale factor of the rendering component (1 is default)
     */
    public GameOfLifeRenderer(int width, int height, WorldState worldState, int scaleFactor) {
        backgroundColor = Color.GRAY;
        foregroundColor = Color.CYAN;
        this.scaleFactor = scaleFactor;
        rendererContainerFrame = new RendererContainerFrame(width, height);
        this.worldState = worldState;
    }

    /**
     * Call this method to draw the current state of the given {@link coderetreat.view.WorldState}
     */
    public void render() {
        if (worldState == null) {
            throw new IllegalStateException("The worldstate is null, please set a valid worldstate.");
        }
        rendererContainerFrame.repaint();
    }

    /**
     * Sets the background color. (Default color is Color.GRAY)
     * @param backgroundColor
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Sets the foreground color. (Default color is Color.CYAN)
     * @param foregroundColor
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    private class RendererContainerFrame {
        private JFrame frame;

        RendererContainerFrame(int width, int height) {
            frame = new JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setSize(width, height);
            frame.add(new GameOfLifeRenderingComponent());
            frame.setVisible( true );
        }

        void repaint() {
            frame.repaint();
        }
    }

    private class GameOfLifeRenderingComponent extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics){
            if (backgroundColor == null) {
                throw new IllegalStateException("BackgroundColor may not be null. Call setBackgroundColor with a valid parameter!");
            }
            if (foregroundColor == null) {
                throw new IllegalStateException("ForegroundColor may not be null. Call setForegroundColor with a valid parameter!");
            }
            graphics.setColor(backgroundColor);
            super.paintComponent(graphics);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setColor(foregroundColor);
            worldState.getCells().stream().filter(Cell::isAlive).forEach(cell -> drawCell(graphics, cell));
        }

        private void drawCell(Graphics graphics, Cell cell) {
            graphics.fillRect(cell.getX()*scaleFactor, cell.getY()*scaleFactor, scaleFactor, scaleFactor);
        }
    }
}
