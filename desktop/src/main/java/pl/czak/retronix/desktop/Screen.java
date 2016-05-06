package pl.czak.retronix.desktop;

import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by czak on 24/02/16.
 */
public class Screen extends Canvas {
    private static final BufferedImage FONT;
    private static final Map<Renderer.Color, Color> COLOR_MAP = new HashMap<>();

    static {
        BufferedImage img = null;
        try {
            img = ImageIO.read(ClassLoader.getSystemResource("images/font.png"));
        } catch (IOException ex) {
            System.out.println("Font not loaded");
        }
        FONT = img;

        COLOR_MAP.put(Renderer.Color.WHITE, Color.WHITE);
        COLOR_MAP.put(Renderer.Color.CYAN, new Color(0, 168, 168));
        COLOR_MAP.put(Renderer.Color.MAGENTA, new Color(168, 0, 168));
    }

    public Screen() {
        setPreferredSize(new Dimension(1280, 720));
        setIgnoreRepaint(true);
    }

    public void draw(State state) {
        BufferStrategy buffer = getBufferStrategy();
        Graphics2D g2 = (Graphics2D) buffer.getDrawGraphics();
        if (g2 == null) return;
        try {
            g2.scale(4, 4);
            g2.setBackground(Color.BLACK);
            g2.clearRect(0, 0, 320, 180);
            state.render(new Graphics2DRenderer(g2));
            buffer.show();
        } finally {
            g2.dispose();
        }
    }

    // Graphics2D-based implementation of the Retronix Renderer interface
    private class Graphics2DRenderer implements Renderer {
        private Graphics2D g2;

        public Graphics2DRenderer(Graphics2D g2) {
            this.g2 = g2;
        }

        @Override
        public void drawSprite(int x, int y, int spriteId) {
            int sx = (spriteId % 32) * 4;
            int sy = (spriteId / 32) * 4;
            g2.drawImage(FONT, x, y, x+4, y+4, sx, sy, sx+4, sy+4, null);
        }

        @Override
        public void drawString(int x, int y, String text) {
            for (byte b : text.getBytes(StandardCharsets.ISO_8859_1)) {
                int index = b & 0xff;
                int sx = (index % 16) * 8;
                int sy = (index / 16) * 8;
                g2.drawImage(FONT, x, y, x+8, y+8, sx, sy, sx+8, sy+8, null);
                x+=8;
            }
        }

        @Override
        public void fillRect(int x, int y, int width, int height, Color color) {
            g2.setColor(COLOR_MAP.get(color));
            g2.fillRect(x, y, width, height);
        }
    }
}