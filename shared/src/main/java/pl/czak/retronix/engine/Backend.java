package pl.czak.retronix.engine;

import pl.czak.retronix.Game;

/**
 * Created by czak on 19/04/16.
 */
public interface Backend {
    void playSound(Game.Sound sound);
    void draw(State state);
}
