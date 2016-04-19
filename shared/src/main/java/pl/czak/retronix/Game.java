package pl.czak.retronix;

import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;

import java.util.Stack;

public class Game {
    private Backend backend;
    private Stack<State> states = new Stack<>();
    private Event event;

    public enum Sound {
        LEVEL_COMPLETE, DEATH, GAME_OVER
    }

    public Game(Backend backend) {
        this.backend = backend;
    }

    /**
     * Enter the game's main loop
     */
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();

                    handleEvent();
                    update();
                    draw();

                    long duration = System.currentTimeMillis() - start;

                    try {
                        Thread.sleep(Math.max(0, 50 - duration));
                    } catch (InterruptedException ignored) { }
                }
            }
        }).start();
    }

    private void handleEvent() {
        if (event != null) {
            getCurrentState().handleEvent(event);
            event = null;
        }
    }

    private void update() {
        getCurrentState().update();
    }

    private void draw() {
        backend.draw(getCurrentState());
    }

    // region State management
    // -----------------------

    public void pushState(State state) {
        states.push(state);
    }

    public State popState() {
        return states.pop();
    }

    public State getCurrentState() {
        return states.peek();
    }

    // ---------
    // endregion

    public void playSound(Sound sound) {
        backend.playSound(sound);
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
