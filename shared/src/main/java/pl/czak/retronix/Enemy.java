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
    public void move() {
        // Wall in my horizontal direction?
        if (board.getField(x + direction.dx, y) != field) {
            direction = direction.flippedX();
        }

        // Wall in my vertical direction?
        if (board.getField(x, y + direction.dy) != field) {
            direction = direction.flippedY();
        }

        x += direction.dx;
        y += direction.dy;
    }
}
