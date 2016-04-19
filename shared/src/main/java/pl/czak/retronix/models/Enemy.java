package pl.czak.retronix.models;

/**
 * Created by czak on 24/02/16.
 */
public abstract class Enemy extends GameCharacter {
    public Enemy(Position position, Direction direction) {
        super(position, direction);
    }

    public Enemy(int x, int y, Direction direction) {
        super(x, y, direction);
    }

    public abstract Board.Field getNativeField();
}
