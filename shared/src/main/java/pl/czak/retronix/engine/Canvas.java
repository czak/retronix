package pl.czak.retronix.engine;

/**
 * Created by czak on 18/04/16.
 */
public interface Canvas {
    enum Color {
        WHITE, MAGENTA, CYAN
    }

    void drawSprite(int x, int y, int spriteId);
    void drawString(int x, int y, String text);
    void fillRect(int x, int y, int width, int height, Color color);
}
