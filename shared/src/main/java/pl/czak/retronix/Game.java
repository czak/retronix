package pl.czak.retronix;

import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class Game {
    private Backend backend;
    private Deque<State> states = new ArrayDeque<>();
    private Queue<Event> events = new ArrayDeque<>();

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
        if (events.peek() != null) {
            states.peek().handleEvent(events.poll());
        }
    }

    private void update() {
        states.peek().update();
    }

    private void draw() {
        backend.draw(states.peek());
    }

    // region State management
    // -----------------------

    public void pushState(State state) {
        states.push(state);
    }

    public State popState() {
        return states.pop();
    }

    // ---------
    // endregion

    public void playSound(Sound sound) {
        backend.playSound(sound);
    }

    public void addEvent(Event event) {
        events.offer(event);
    }
}
