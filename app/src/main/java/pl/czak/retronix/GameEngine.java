package pl.czak.retronix;

import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.engine.GameState;

public class GameEngine {
    private GameRenderer renderer;
    private GameState state;
    private GameEvent event;

    public GameEngine(GameRenderer renderer) {
        this.renderer = renderer;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setEvent(GameEvent event) {
        this.event = event;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (event != null) {
                        state.handleEvent(event);
                        event = null;
                    }

                    state.update();

                    renderer.render(state);

                    // TODO: Improve timeout for consistent FPS/game rate
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }
}
