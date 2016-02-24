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

        // Definitely stop if trying to move outside bounds
        if (!board.isWithinBounds(x + direction.dx, y + direction.dy)) {
            direction = null;
            return;
        }

        // If moving across sea, leave a bag of sand behind
        if (board.getField(x, y) == Board.Field.SEA)
            board.setField(x, y, Board.Field.SAND);

        x += direction.dx;
        y += direction.dy;
    }
}
