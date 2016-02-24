package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Enemy extends GameCharacter {
    private Board.Field type;

    public Enemy(Board board, Board.Field type) {
        super(board);
        this.type = type;
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
        return board.getField(x, y) == type;
    }

    /**
     * Check if this enemy's movement is about to cause a collision.
     * Will throw Game.Collision if an upcoming collision is detected.
     * @param board
     * @param player
     * @throws Game.Collision
     */
    public void detectCollision(Board board, Player player) throws Game.Collision {
        final int nx = x + direction.dx;
        final int ny = y + direction.dy;

        // Am I about to collide with player on my turf?
        if (type == board.getField(nx, ny) &&
                nx == player.x && ny == player.y)
            throw new Game.Collision();

        // Am I a sea creature about to collide
        // with an incomplete sand wall?
        if (type == Board.Field.SEA &&
                board.getField(nx, ny) == Board.Field.SAND)
            throw new Game.Collision();
    }
}
