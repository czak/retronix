package pl.czak.retronix.desktop;

import pl.czak.retronix.State;
import pl.czak.retronix.engine.Canvas;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by czak on 24/02/16.
 */
public class Screen extends JPanel {
    private static BufferedImage font;

    static {
        try {
            font = ImageIO.read(ClassLoader.getSystemResource("font.png"));
        } catch (IOException ex) {
            System.out.println("Font not loaded");
        }
    }

    private State state;
    private Graphics2D g2;
    private Graphics2DCanvas canvas;

    public Screen() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);

        // No state is preserved but I'm storing it anyway
        // to prevent instantiating for every frame.
        this.canvas = new Graphics2DCanvas();
    }

    public void draw(State state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.scale(4, 4);
        state.render(canvas);
    }

    // Graphics2D-based implementation of the Retronix Canvas interface
    class Graphics2DCanvas implements Canvas {
        @Override
        public void drawSprite(int x, int y, int spriteId) {
            int sx = (spriteId % 32) * 4;
            int sy = (spriteId / 32) * 4;
            g2.drawImage(font, x, y, x+4, y+4, sx, sy, sx+4, sy+4, null);
        }

        @Override
        public void drawString(int x, int y, String text) {
            for (byte b : text.getBytes(StandardCharsets.ISO_8859_1)) {
                int index = b & 0xff;
                int sx = (index % 16) * 8;
                int sy = (index / 16) * 8;
                g2.drawImage(font, x, y, x+8, y+8, sx, sy, sx+8, sy+8, null);
                x+=8;
            }
        }

        @Override
        public void fillRect(int x, int y, int width, int height, Color color) {
            g2.setColor(systemColor(color));
            g2.fillRect(x, y, width, height);
        }

        private java.awt.Color systemColor(Color color) {
            switch (color) {
                case WHITE:     return java.awt.Color.WHITE;
                case BLACK:     return java.awt.Color.BLACK;
                case RED:       return java.awt.Color.RED;
                case GREEN:     return java.awt.Color.GREEN;
                case BLUE:      return java.awt.Color.BLUE;
                case YELLOW:    return java.awt.Color.YELLOW;
                case MAGENTA:   return java.awt.Color.MAGENTA;
                case CYAN:      return java.awt.Color.CYAN;
                default:        return null;
            }
        }
    }
}