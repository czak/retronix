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
        if (isWithinBounds(x, y)) return fields[y][x];
        return null;
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
     * @return part of sea covered with land as a double 0.0...1.0
     */
    public double fill(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            floodFill(enemy.x, enemy.y);
        }

        int seaBlocks = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fields[y][x] == Field.DEEP_SEA) {
                    fields[y][x] = Field.SEA;
                    seaBlocks++;
                }
                else
                    fields[y][x] = Field.LAND;
            }
        }

        int entireSea = (width - 4) * (height - 4);
        return 1.0 - (double) seaBlocks / entireSea;
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
