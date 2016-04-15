package pl.czak.retronix.desktop;

import pl.czak.retronix.GameRenderer;
import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by czak on 24/02/16.
 */
public class Screen extends JPanel implements GameRenderer {
    private GameState state;

    public Screen() {
        setPreferredSize(new Dimension(500, 300));
        setBackground(Color.BLACK);
    }

    @Override
    public void render(GameState state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        state.render(new Canvas() {
            @Override
            public void fillRect(double x, double y, double width, double height, Color color) {
                Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
                g2.setColor(getSystemColor(color));
                g2.fill(rect);
            }

            @Override
            public int getWidth() {
                return 500;
            }

            @Override
            public int getHeight() {
                return 300;
            }
        });
    }

    Color getSystemColor(Canvas.Color c) {
        switch (c) {
            case RED:       return Color.RED;
            case GREEN:     return Color.GREEN;
            case BLUE:      return Color.BLUE;
            case YELLOW:    return Color.YELLOW;
            case MAGENTA:   return Color.MAGENTA;
            default:        return null;
        }
    }
}