package pl.czak.retronix;

import java.util.Random;

/**
 * Created by czak on 24/02/16.
 */
public class Enemy extends GameCharacter {
    private static final Random random = new Random();

    private Board.Field type;

    public Enemy(Board board, Board.Field type) {
        super(board);
        this.type = type;

        // Place randomly & ensure I'm on correct land
        do {
            this.x = random.nextInt(board.getWidth());
            this.y = random.nextInt(board.getHeight());
        } while (board.getField(x, y) != type);

        // Randomly pick a direction appropriate for enemies
        // (i.e. diagonal only)
        this.direction = new Direction[] {
                Direction.NE,
                Direction.SE,
                Direction.NW,
                Direction.SW
        }[random.nextInt(4)];
    }

    /**
     * Bounce off walls and correct course.
     * Needs to be called before detectCollision
     */
    public void bounce() {
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
    }

    @Override
    public boolean move() {
        if (isValidLocation(x + direction.dx, y + direction.dy)) {
            x += direction.dx;
            y += direction.dy;
        }

        return false;
    }

    /**
     * Check if this enemy's movement is about to cause a collision.
     * Will throw Game.Collision if an upcoming collision is detected.
     * Before calling ensure the direction is correctly set by now.
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

    private boolean isValidLocation(int x, int y) {
        return board.getField(x, y) == type;
    }
}
