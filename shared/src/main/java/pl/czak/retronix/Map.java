package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Map {
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 6;

    public enum Field {
        LAND, SEA;
    }

    private Field[][] fields;

    private int width;
    private int height;

    public Map() {
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
}
