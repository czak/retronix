package pl.czak.retronix.models;

/**
 * Created by czak on 24/02/16.
 */
public abstract class GameCharacter {
    protected Position position;
    protected Direction direction;

    /**
     * Create a new character at specified position
     * and moving in the specified direction
     * @param position
     * @param direction
     */
    public GameCharacter(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    /**
     * Create a new character at specified position
     * and not moving (direction = null)
     * @param position
     */
    public GameCharacter(Position position) {
        this(position, null);
    }

    /**
     * Create a new character at specified (x, y) location
     * and moving in the specified direction
     * @param x
     * @param y
     * @param direction
     */
    public GameCharacter(int x, int y, Direction direction) {
        this(new Position(x, y), direction);
    }

    /**
     * Create a new character at specified (x, y) location
     * and not moving (direction = null).
     * @param x
     * @param y
     */
    public GameCharacter(int x, int y) {
        this(x, y, null);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return The position ahead of the character in their current direction
     */
    public Position getNextPosition() {
        return position.movedTo(direction);
    }
}
