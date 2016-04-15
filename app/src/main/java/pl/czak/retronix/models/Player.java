package pl.czak.retronix.models;

/**
 * Created by czak on 24/02/16.
 */
public class Player extends GameCharacter {
    public Player(Position position) {
        super(position);
    }

    public Player(int x, int y) {
        super(x, y);
    }

    /**
     * Stop movement of the player. Effectively sets direction to null;
     */
    public void stop() {
        direction = null;
    }
}
