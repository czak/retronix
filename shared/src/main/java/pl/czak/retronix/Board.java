package pl.czak.retronix;

import java.util.List;

/**
 * Created by czak on 24/02/16.
 */
public class Board {
    private static final int DEFAULT_WIDTH = 70;
    private static final int DEFAULT_HEIGHT = 40;

    public enum Field {
        LAND, SEA, SAND,
        // This is a marker field meaning the field
        // will remain a SEA after fill.
        DEEP_SEA;
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
                boolean isBorder = x < 2 || y < 2 || x >= width - 2 || y >= height - 2;
                fields[y][x] = isBorder ? Field.LAND : Field.SEA;
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

    /**
     * Fill walled areas with land
     * @param enemies
     */
    public void fill(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            floodFill(enemy.x, enemy.y);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fields[y][x] == Field.DEEP_SEA)
                    fields[y][x] = Field.SEA;
                else
                    fields[y][x] = Field.LAND;
            }
        }
    }

    /**
     * Remove any remaining sand walls
     */
    public void clean() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fields[y][x] == Field.SAND)
                    fields[y][x] = Field.SEA;
            }
        }
    }

    private void floodFill(int x, int y) {
        if (fields[y][x] == Field.SEA) {
            fields[y][x] = Field.DEEP_SEA;
            floodFill(x - 1, y);
            floodFill(x + 1, y);
            floodFill(x, y - 1);
            floodFill(x, y + 1);
        }
    }
}
