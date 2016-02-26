package pl.czak.retronix;

import java.util.Random;

/**
 * Created by czak on 24/02/16.
 */
public abstract class Enemy extends GameCharacter {
    protected static final Random random = new Random();

    /**
     * Hold on to the player for collision detection
     */
    protected Player player;

    public Enemy(Board board, Player player) {
        super(board);

        this.player = player;

        // Place randomly & ensure I'm on correct turf
        do {
            this.x = random.nextInt(board.getWidth());
            this.y = random.nextInt(board.getHeight());
        } while (!isValidLocation(x, y));

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
     */
    protected void bounce() {
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

    protected abstract boolean isValidLocation(int x, int y);

    /**
     * Is (x, y) my turf and am I hitting the player?
     */
    protected boolean isPlayerHit(int x, int y) {
        return isValidLocation(x, y) &&
                player.x == x && player.y == y;
    }
}
