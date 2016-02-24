package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Player extends GameCharacter {
    public Player(Board board) {
        super(board);
        this.x = board.getWidth() / 2;
        this.y = board.getHeight() - 2;
    }
}
