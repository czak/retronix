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
    private static final double FILL_THRESHOLD = 0.8;

    public enum Field {
        LAND, SEA, SAND,
        // This is a marker field meaning the field
        // will remain a SEA after fill.
        DEEP_SEA;
    }

    private int width;
    private int height;

    private Field[][] fields;

    private Player player;
    private List<Enemy> enemies = new ArrayList<>();

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
     * Perform one turn of the movements
     */
    public void update() throws Collision, LevelComplete {
        movePlayer();
        moveEnemies();
    }

    private void movePlayer() throws Collision, LevelComplete {
        if (player.getDirection() == null) return;

        Position position = player.getPosition();
        Position nextPosition = player.getNextPosition();

        // Definitely stop if trying to move outside bounds
        if (!isWithinBounds(nextPosition)) {
            player.stop();
            return;
        }

        // Going back into the sand is a collision
        if (getField(nextPosition) == Field.SAND)
            throw new Collision();

        // If moving across sea, leave a bag of sand behind
        if (getField(position) == Field.SEA) {
            setField(position, Field.SAND);

            // If re-entering dry land, stop immediately
            if (getField(nextPosition) == Field.LAND) {
                player.stop();

                // And fill the board
                fill();
            }
        }

        player.setPosition(nextPosition);
    }

    private void moveEnemies() throws Collision {
        for (Enemy enemy : enemies) {
            // Sea enemies collide with in-progress SAND walls
            enemy.detectCollision(getField(enemy.getNextPosition()));

            bounceEnemy(enemy);

            // Detect collision with player
            collideEnemyWithPlayer(enemy);

            // There's still a possibility the enemy can't move (i.e. gets stuck)
            // so we check one more time before actually moving.
            Position nextPosition = enemy.getNextPosition();
            if (canMoveEnemy(enemy, nextPosition)) {
                enemy.setPosition(nextPosition);
            }

            // Need to recheck collisions, otherwise player might
            // pass an enemy untouched.
            // TODO: Clean this up. Should be doable in a single pass.
            collideEnemyWithPlayer(enemy);
        }
    }

    private void bounceEnemy(Enemy enemy) {
        Position position = enemy.getPosition();
        Direction direction = enemy.getDirection();

        // Wall in my horizontal direction?
        if (!canMoveEnemy(enemy, position.movedHorizontally(direction))) {
            direction = direction.flippedX();
        }

        // Wall in my vertical direction?
        if (!canMoveEnemy(enemy, position.movedVertically(direction))) {
            direction = direction.flippedY();
        }

        // Running straight into a corner?
        if (!canMoveEnemy(enemy, position.movedTo(direction))) {
            direction = direction.flippedX().flippedY();
        }

        // Update the direction after bouncing
        enemy.setDirection(direction);
    }

    private boolean canMoveEnemy(Enemy enemy, Position position) {
        return getField(position) == enemy.getNativeField();
    }

    /**
     * Detect a collision between the given enemy and the player.
     * @param enemy
     * @throws Collision
     */
    private void collideEnemyWithPlayer(Enemy enemy) throws Collision {
        Position position = enemy.getPosition();
        Direction direction = enemy.getDirection();

        // Is it a direct hit?
        Position nextPosition = position.movedTo(direction);
        if (getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();

        // Is it a hit from the side?
        nextPosition = position.movedHorizontally(direction);
        if (getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();

        // Is it a hit from top or bottom?
        nextPosition = position.movedVertically(direction);
        if (getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();
    }

    /**
     * Fill walled areas with land
     */
    public void fill() throws LevelComplete {
        // Mark all enemy-occupied areas as DEEP_SEA
        for (Enemy enemy : enemies) {
            Position pos = enemy.getPosition();
            floodFill(pos.x, pos.y);
        }

        // ...and set everything else to LAND
        int remainingSeaFields = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fields[y][x] == Field.DEEP_SEA) {
                    fields[y][x] = Field.SEA;
                    remainingSeaFields++;
                }
                else
                    fields[y][x] = Field.LAND;
            }
        }

        // Break out if we've passed FILL_THRESHOLD
        int totalSeaFields = (width - 2*LAND_BORDER_WIDTH) * (height - 2*LAND_BORDER_WIDTH);
        double ratio = 1.0 - ((double) remainingSeaFields / totalSeaFields);
        if (ratio >= FILL_THRESHOLD)
            throw new LevelComplete();
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

    private void setField(Position position, Field field) {
        fields[position.y][position.x] = field;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = new ArrayList<>(enemies);
    }

    /**
     * Reset positions & directions of enemies on the board.
     * Note only land enemies are updated.
     */
    public void resetEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy instanceof LandEnemy) {
                enemy.setPosition(randomPosition(Field.LAND));
                enemy.setDirection(Direction.randomDiagonal());
            }
        }
    }

    private boolean isWithinBounds(Position pos) {
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

    public static class Collision extends Exception {}
    public static class LevelComplete extends Exception {}
}