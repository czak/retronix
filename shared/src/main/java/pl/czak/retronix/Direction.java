package pl.czak.retronix;

import java.util.Random;

/**
 * Created by czak on 24/02/16.
 */
public enum Direction {
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0),
    NE(1, -1), SE(1, 1), NW(-1, -1), SW(-1, 1);

    private static final Direction[] DIAGONALS = { NE, SE, NW, SW };

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // TODO: Consider making Direction mutable
    //       This will allow just updating dx/dy
    //       Enum values will be replaced with static instances
    public Direction flippedX() {
        switch (this) {
            // These two are unchanged
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            // These flip X direction
            case EAST:  return WEST;
            case WEST:  return EAST;
            case NE:    return NW;
            case SE:    return SW;
            case NW:    return NE;
            case SW:    return SE;
            default:    return null;
        }
    }

    public Direction flippedY() {
        switch (this) {
            // These two are unchanged
            case EAST:  return EAST;
            case WEST:  return WEST;
            // These flip Y direction
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case NE:    return SE;
            case SE:    return NE;
            case NW:    return SW;
            case SW:    return NW;
            default:    return null;
        }
    }

    /**
     * Return one of the diagonal directions at random.
     * @return one of NE, SE, NW, or SW
     */
    public static Direction randomDiagonal() {
        return DIAGONALS[new Random().nextInt(4)];
    }
}
