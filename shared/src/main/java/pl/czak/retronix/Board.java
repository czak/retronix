package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Board {
    private static final int DEFAULT_WIDTH = 70;
    private static final int DEFAULT_HEIGHT = 40;

    public enum Field {
        LAND, SEA, SAND;
    }

    private Field[][] fields;

    private int width;
    private int height;

    public Board() {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;

        fields = new Field[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isLand = x < 2 || y < 2 || x >= width - 2 || y >= height - 2;
                fields[y][x] = isLand ? Field.LAND : Field.SEA;
            }
        }
    }

    public Field[][] getFields() {
        return fields;
    }

    public Field getField(int x, int y) {
        return fields[y][x];
    }

    public void setField(int x, int y, Field field) {
        fields[y][x] = field;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height;
    }
}
