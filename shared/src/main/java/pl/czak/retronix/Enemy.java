package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Enemy extends GameCharacter {
    private Board.Field field;

    public Enemy(Board board, Board.Field field) {
        super(board);
        this.field = field;
        // TODO: Random location & direction
        this.x = 2;
        this.y = 2;
        this.direction = Direction.SE;
    }

    @Override
    public boolean move() {
        // Wall in my horizontal direction?
        if (!isValidLocation(x + direction.dx, y)) {
            direction = direction.flippedX();
        }

        // Wall in my vertical direction?
        if (!isValidLocation(x, y + direction.dy)) {
            direction = direction.flippedY();
        }

        // Running straight into a corner?
        if (!isValidLocation(x + direction.dx, y + direction.dy)) {
            direction = direction.flippedX().flippedY();
        }

        if (isValidLocation(x + direction.dx, y + direction.dy)) {
            x += direction.dx;
            y += direction.dy;
        }
        return false;
    }

    private boolean isValidLocation(int x, int y) {
        return board.getField(x, y) == field;
    }
}
