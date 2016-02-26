package pl.czak.retronix;

/**
 * Created by czak on 25/02/16.
 */
public class SeaEnemy extends Enemy {
    public SeaEnemy(Board board, Player player) {
        super(board, player);
    }

    @Override
    public boolean move() throws Game.Collision {
        // Am I swimming into an in-progress SAND wall?
        if (board.getField(x + direction.dx, y + direction.dy) == Board.Field.SAND)
            throw new Game.Collision();

        bounce();

        // Am I hitting the player on my turf
        if (isPlayerHit(x + direction.dx, y + direction.dy) ||
                isPlayerHit(x,                y + direction.dy) ||
                isPlayerHit(x + direction.dx, y               ))
            throw new Game.Collision();

        if (isValidLocation(x + direction.dx, y + direction.dy)) {
            x += direction.dx;
            y += direction.dy;
        }

        return false;
    }

    protected boolean isValidLocation(int x, int y) {
        return board.getField(x, y) == Board.Field.SEA;
    }
}
