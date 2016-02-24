package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public enum Direction {
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0),
    NE(1, -1), SE(1, 1), NW(-1, -1), SW(-1, 1);

    public int dx;
    public int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
