package pl.czak.retronix;

/**
 * Created by czak on 25/02/16.
 */
public class SeaEnemy extends Enemy {
    public SeaEnemy(Position position, Direction direction) {
        super(position, direction);
    }

    @Override
    Board.Field getNativeField() {
        return Board.Field.SEA;
    }

    @Override
    void detectCollision(Board.Field field) throws Board.Collision {
        if (field == Board.Field.SAND)
            throw new Board.Collision();
    }
}
