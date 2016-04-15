package pl.czak.retronix.engine;

import java.awt.*;

/**
 * Created by czak on 15/04/16.
 */
public abstract class GameState {
    public abstract void handleEvent(GameEvent event);
    public abstract void update();
    public abstract void render(Canvas canvas);
}
