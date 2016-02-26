package pl.czak.retronix;

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
    Board.Field getNativeField() {
        return Board.Field.LAND;
    }

    @Override
    void detectCollision(Board.Field field) throws Board.Collision {
        // No-op here. LandEnemy does not collide with fields on the board.
    }
}
