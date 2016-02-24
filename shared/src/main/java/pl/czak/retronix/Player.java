package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Player extends GameCharacter {
    public Player(Board board) {
        super(board);
        this.x = board.getWidth() / 2;
        this.y = 0;
    }

    @Override
    public boolean move() {
        if (direction == null) return false;

        int nx = x + direction.dx;
        int ny = y + direction.dy;

        // TODO: Driving into the sand is a collision and should kill me

        boolean res = false;

        // Definitely stop if trying to move outside bounds
        if (!board.isWithinBounds(nx, ny)) {
            direction = null;
            return false;
        }

        // If moving across sea, leave a bag of sand behind
        if (board.getField(x, y) == Board.Field.SEA) {
            board.setField(x, y, Board.Field.SAND);

            // If re-entering dry land, stop immediately
            if (board.getField(nx, ny) == Board.Field.LAND) {
                direction = null;
                res = true; // true will trigger a map fill
            }
        }

        x = nx;
        y = ny;

        return res;
    }
}
