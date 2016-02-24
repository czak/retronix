package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Player {
    private Board board;

    private int x;
    private int y;
    private Direction direction;

    public Player(Board board) {
        // FIXME: Not sure I like the Player->Board dependency
        this.board = board;
        this.x = board.getWidth() / 2;
        this.y = board.getHeight() - 2;
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
