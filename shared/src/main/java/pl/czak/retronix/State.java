package pl.czak.retronix;

import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameEvent;

import java.awt.Graphics2D;

/**
 * Created by czak on 15/04/16.
 */
public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public void handleGameEvent(GameEvent event) { }
    public void update() { }
    public abstract void render(Canvas canvas);
}
