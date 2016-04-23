package pl.czak.retronix.engine;

/**
 * Created by czak on 19/04/16.
 */
public interface Backend {
    void playSound(Sound sound);
    void draw(State state);
}
