package pl.czak.retronix;

import java.util.List;

/**
 * Created by czak on 24/02/16.
 */
public interface GameRenderer {
    public void render(Board board, Player player, List<Enemy> enemies);
}
