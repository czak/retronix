package pl.czak.retronix;

import org.junit.Test;
import pl.czak.retronix.models.*;

import java.util.Arrays;

/**
 * Created by czak on 26/02/16.
 */
public class CollisionsTest {
    @Test
    public void testSeaEnemyCannotCollideWithPlayerOnLand() throws Board.Collision, Board.LevelComplete {
        // ######
        // ######
        // ##S ##
        // ##  ## S - SeaEnemy moving SE
        // ####P# P - Player not moving
        // ######
        Board board = new Board(6, 6);
        board.setPlayer(new Player(4, 4));
        board.setEnemies(Arrays.<Enemy>asList(
                new SeaEnemy(2, 2, Direction.SE)
        ));

        // Should complete cleanly
        board.update();
    }
}
