package pl.czak.retronix;

import java.util.Random;

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

    abstract Board.Field getNativeField();

    abstract void detectCollision(Board.Field field) throws Board.Collision;
}
