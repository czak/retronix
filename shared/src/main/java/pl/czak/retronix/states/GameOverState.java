package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.Event;

/**
 * Created by czak on 20/04/16.
 */
public class GameOverState extends State {
    private State previousState;
    private boolean isDone = false;

    public GameOverState(Game game, State previousState) {
        super(game);
        this.previousState = previousState;
    }

    @Override
    public void handleEvent(Event event) {
        if (event == Event.KEY_SELECT || event == Event.KEY_BACK) {
            isDone = true;
        }
    }

    @Override
    public void update() {
        if (!isDone) return;
        game.popState();
        game.popState();
    }

    @Override
    public void render(Renderer renderer) {
        previousState.render(renderer);
        renderer.drawString(88, 78, "[================]");
        renderer.drawString(88, 86, "|G A M E  O V E R|");
        renderer.drawString(88, 94, "{================}");
    }
}
