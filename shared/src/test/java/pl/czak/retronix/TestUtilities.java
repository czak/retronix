package pl.czak.retronix;

import java.util.List;

/**
 * Created by czak on 26/02/16.
 */
public class TestUtilities {
    public static void debugRender(Board board) {
        Player player = board.getPlayer();
        List<Enemy> enemies = board.getEnemies();

        for (int y = 0; y < board.getHeight(); y++) {
            row: for (int x = 0; x < board.getWidth(); x++) {
                Position position = new Position(x, y);

                if (position.equals(player.getPosition())) {
                    System.out.print("P");
                    continue;
                }

                for (Enemy enemy : enemies) {
                    if (position.equals(enemy.getPosition())) {
                        System.out.print("E");
                        continue row;
                    }
                }

                Board.Field field = board.getField(position);
                switch (field) {
                    case LAND:  System.out.print("#"); break;
                    case SEA:   System.out.print(" "); break;
                    case SAND:  System.out.print("%"); break;
                    default:    System.out.print("?"); break;
                }
            }
            System.out.println();
        }
    }

}
