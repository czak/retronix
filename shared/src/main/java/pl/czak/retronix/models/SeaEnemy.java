package pl.czak.retronix.models;

/**
 * Created by czak on 25/02/16.
 */
public class SeaEnemy extends Enemy {
    public SeaEnemy(Position position, Direction direction) {
        super(position, direction);
    }

    public SeaEnemy(int x, int y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public Board.Field getNativeField() {
        return Board.Field.SEA;
    }

    @Override
    public void detectCollision(Board.Field field) throws Board.Collision {
        if (field == Board.Field.SAND)
            throw new Board.Collision();
    }
}
