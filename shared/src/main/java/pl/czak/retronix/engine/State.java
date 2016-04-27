package pl.czak.retronix.engine;

import pl.czak.retronix.Game;

/**
 * Created by czak on 15/04/16.
 */
public abstract class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public void handleEvent(Event event) { }
    public void update() { }
    public abstract void render(Renderer renderer);
    public void pause() { }
}
