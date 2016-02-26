package pl.czak.retronix;

/**
 * Created by czak on 26/02/16.
 */
public class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position other) {
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    /**
     * Returns a new Position moved in the specified direction
     * @param direction
     * @return
     */
    public Position movedTo(Direction direction) {
        if (direction == null) return this;
        return new Position(x + direction.dx, y + direction.dy);
    }

    /**
     * Returns a new Position moved in the X axis of the specified direction
     * @param direction
     * @return
     */
    public Position movedHorizontally(Direction direction) {
        if (direction == null) return this;
        return new Position(x + direction.dx, y);
    }

    /**
     * Returns a new Position moved in the Y axis of the specified direction
     * @param direction
     * @return
     */
    public Position movedVertically(Direction direction) {
        if (direction == null) return this;
        return new Position(x, y + direction.dy);
    }
}
