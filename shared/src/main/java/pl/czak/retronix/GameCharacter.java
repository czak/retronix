package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class GameCharacter {
    protected Board board;
    protected int x;
    protected int y;
    protected Direction direction;

    public GameCharacter(Board board) {
        this.board = board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move() {
        if (direction == null) return;

        // Check if move is within board bounds
        if (board.isWithinBounds(x + direction.dx, y + direction.dy)) {
            x += direction.dx;
            y += direction.dy;
        } else {
            // Stop if not
            direction = null;
        }
    }
}
