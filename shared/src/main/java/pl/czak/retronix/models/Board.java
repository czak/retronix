package pl.czak.retronix.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by czak on 24/02/16.
 */
public class Board {
    private static final Random RANDOM = new Random();
    private static final int LAND_BORDER_WIDTH = 2;

    public enum Field {
        LAND, SEA, SAND,
        // This is a marker field meaning the field
        // will remain a SEA after fill.
        DEEP_SEA;
    }

    private int width;
    private int height;

    private Field[][] fields;

    private double fillRatio = 0;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.fields = new Field[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isBorder = x < LAND_BORDER_WIDTH || y < LAND_BORDER_WIDTH ||
                        x >= width - LAND_BORDER_WIDTH || y >= height - LAND_BORDER_WIDTH;
                fields[y][x] = isBorder ? Field.LAND : Field.SEA;
            }
        }
    }

    /**
     * Fill walled areas with land
     * @param enemies
     */
    public int fill(List<Enemy> enemies) {
        // Mark all enemy-occupied areas as DEEP_SEA
        for (Enemy enemy : enemies) {
            Position pos = enemy.getPosition();
            floodFill(pos.x, pos.y);
        }

        // ...and set everything else to LAND
        int remainingSeaFields = 0, newLandFields = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fields[y][x] == Field.DEEP_SEA) {
                    fields[y][x] = Field.SEA;
                    remainingSeaFields++;
                }
                else if (fields[y][x] == Field.SEA || fields[y][x] == Field.SAND) {
                    fields[y][x] = Field.LAND;
                    newLandFields++;
                }
            }
        }

        // Calculate new fill percentage
        int totalSeaFields = (width - 2*LAND_BORDER_WIDTH) * (height - 2*LAND_BORDER_WIDTH);
        fillRatio = 1.0 - ((double) remainingSeaFields / totalSeaFields);

        // And return number of newly filled fields
        return newLandFields;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Field[][] getFields() {
        return fields;
    }

    public Field getField(Position position) {
        if (isWithinBounds(position))
            return fields[position.y][position.x];
        else
            return null;
    }

    public void setField(Position position, Field field) {
        fields[position.y][position.x] = field;
    }

    public double getFillRatio() {
        return fillRatio;
    }

//    /**
//     * Reset positions & directions of enemies on the board.
//     * Note only land enemies are updated.
//     */
//    public void resetEnemies() {
//        for (Enemy enemy : enemies) {
//            if (enemy instanceof LandEnemy) {
//                enemy.setPosition(randomPosition(Field.LAND));
//                enemy.setDirection(Direction.randomDiagonal());
//            }
//        }
//    }

    public boolean isWithinBounds(Position pos) {
        return pos.x >= 0 && pos.x < width &&
                pos.y >= 0 && pos.y < height;
    }

    /**
     * Return a random position within bounds of the board
     * @return a newly created Position
     */
    public Position randomPosition() {
        return new Position(RANDOM.nextInt(width), RANDOM.nextInt(height));
    }

    /**
     * Return a random position within bounds of the board,
     * matching the specified field
     * @param field the type of field requested
     * @return a newly created Position containing the given field type
     */
    public Position randomPosition(Field field) {
        // TODO: Optimize without instantiating unnecessary Positions
        Position position;
        do {
            position = randomPosition();
        } while (getField(position) != field);
        return position;
    }


    // TODO: Switch to a non-recursive implementation (or a smarter one even)
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