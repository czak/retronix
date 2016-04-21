package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Canvas;
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
        if (event == Event.KEY_SELECT) {
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
    public void render(Canvas canvas) {
        previousState.render(canvas);
        canvas.drawString(88, 78, "[================]");
        canvas.drawString(88, 86, "|G A M E  O V E R|");
        canvas.drawString(88, 94, "{================}");
    }
}