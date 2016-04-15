package pl.czak.retronix.engine;

/**
 * Created by czak on 15/04/16.
 */
public interface Canvas {
    enum Color {
        RED, GREEN, BLUE, YELLOW, MAGENTA
    }

    void fillRect(double x, double y, double width, double height, Color color);

    int getWidth();
    int getHeight();
}
