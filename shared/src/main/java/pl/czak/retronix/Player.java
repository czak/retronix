package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Player extends GameCharacter {
    public Player(Board board) {
        super(board);
        this.x = board.getWidth() / 2;
        this.y = board.getHeight() - 2;
    }

    @Override
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
