package pl.czak.retronix;

/**
 * Created by czak on 25/02/16.
 */
public class LandEnemy extends Enemy {
    public LandEnemy(Board board, Player player) {
        super(board, player);
    }

    @Override
    public boolean move() throws Game.Collision {
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
        return board.getField(x, y) == Board.Field.LAND;
    }
}
