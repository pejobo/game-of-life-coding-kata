package coderetreat.view;

import java.awt.*;
import javax.swing.*;

public class GameOfLifeRenderer {
    private final RendererContainerFrame rendererContainerFrame;
    private final WorldState worldState;

    private long lastRendered;
    private Color backgroundColor;
    private Color foregroundColor;
    private long delta;

    /**
     * Use this to generate
     * @param width blabla
     * @param height ba
     * @param worldState bawd
     */
    public GameOfLifeRenderer(int width, int height, WorldState worldState) {
        backgroundColor = Color.GRAY;
        foregroundColor = Color.CYAN;
        lastRendered = System.currentTimeMillis();
        delta = 0;
        rendererContainerFrame = new RendererContainerFrame(width, height);
        this.worldState = worldState;
    }

    public void render() {
        if (worldState == null) {
            throw new IllegalStateException("The worldstate is null, please set a valid worldstate.");
        }
        rendererContainerFrame.repaint();
        updateDelta();
    }

    private void updateDelta() {
        long now = System.currentTimeMillis();
        delta = now - lastRendered;
        lastRendered = System.currentTimeMillis();
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public long getDelta() {
        return delta;
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
            super.paintComponent(graphics);
            graphics.setColor(backgroundColor);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setColor(foregroundColor);
            worldState.getCells().stream().filter(Cell::isAlive).forEach(cell -> drawCell(graphics, cell));
        }

        private void drawCell(Graphics graphics, Cell cell) {
            graphics.fillRect(cell.getX(), cell.getY(), cell.getX()+1, cell.getY()+1
            );
        }
    }
}
