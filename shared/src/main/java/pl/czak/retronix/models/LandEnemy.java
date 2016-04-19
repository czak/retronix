package pl.czak.retronix.models;

/**
 * Created by czak on 25/02/16.
 */
public class LandEnemy extends Enemy {
    public LandEnemy(Position position, Direction direction) {
        super(position, direction);
    }

    public LandEnemy(int x, int y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public Board.Field getNativeField() {
        return Board.Field.LAND;
    }
}
