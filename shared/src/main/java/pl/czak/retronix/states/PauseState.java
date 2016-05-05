package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.State;

/**
 * Created by czak on 27/04/16.
 */
public class PauseState extends State {
    private static final int OPTION_RESUME = 0;
    private static final int OPTION_EXIT = 1;

    private State previousState;
    private int selectedOption = OPTION_RESUME;

    public PauseState(Game game, State previousState) {
        super(game);
        this.previousState = previousState;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getType() == Event.Type.BACK)
            resume();

        if (game.isTouchEnabled()) {
            if (event.getType() == Event.Type.CLICK) {
                if (event.isWithinBounds(112, 70, 96, 24)) resume();
                else if (event.isWithinBounds(112, 94, 96, 24)) exit();
            }
        } else {
            switch (event.getType()) {
                case UP:
                case DOWN:
                    selectedOption = 1 - selectedOption;
                    break;
                case SELECT:
                    if (selectedOption == OPTION_RESUME) resume();
                    else if (selectedOption == OPTION_EXIT) exit();
                    break;
            }
        }
    }

    private void resume() {
        game.popState();
    }

    private void exit() {
        game.popState();
        game.popState();
    }

    @Override
    public void render(Renderer renderer) {
        previousState.render(renderer);

        renderer.drawString(112,  54, "   PAUSED   ");

        renderer.drawString(112,  70, "(----------)");
        renderer.drawString(112,  78, ";  RESUME  ;");
        renderer.drawString(112,  86, ",----------.");

        renderer.drawString(112,  94, "(----------)");
        renderer.drawString(112, 102, ";   EXIT   ;");
        renderer.drawString(112, 110, ",----------.");

        if (!game.isTouchEnabled()) {
            if (selectedOption == OPTION_RESUME) {
                renderer.drawString(112, 70, "[==========]");
                renderer.drawString(112, 78, "|          |");
                renderer.drawString(112, 86, "{==========}");
            } else if (selectedOption == OPTION_EXIT) {
                renderer.drawString(112, 94, "[==========]");
                renderer.drawString(112, 102, "|          |");
                renderer.drawString(112, 110, "{==========}");
            }
        }
    }
}
