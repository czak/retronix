package pl.czak.retronix;

import android.graphics.Canvas;
import android.view.KeyEvent;

/**
 * Created by czak on 15/04/16.
 */
public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public void handleKeyEvent(KeyEvent event) { }
    public void update() { }
    public abstract void render(Canvas canvas);
}
