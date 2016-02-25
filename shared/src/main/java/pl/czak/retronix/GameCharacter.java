package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public abstract class GameCharacter {
    protected Board board;
    protected int x;
    protected int y;
    protected Direction direction;

    public GameCharacter(Board board) {
        this.board = board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public abstract boolean move() throws Game.Collision;
}
