package pl.czak.retronix;

/**
 * Created by czak on 24/02/16.
 */
public class Enemy extends GameCharacter {
    public Enemy(Board board) {
        super(board);
        // TODO: Random location on appropriate surface
        this.x = 2;
        this.y = 2;
        this.direction = Direction.SE;
    }
}
