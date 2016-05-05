package pl.czak.retronix;

import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Sound;
import pl.czak.retronix.engine.State;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private Backend backend;
    private Deque<State> states = new LinkedList<>();
    private Queue<Event> events = new LinkedList<>();

    private boolean running;
    private boolean done;
    private final boolean touchEnabled;

    public Game(Backend backend) {
        this.backend = backend;

        // Storing this so backend is not queried on every frame
        touchEnabled = backend.isTouchEnabled();
    }

    public void handleEvent() {
        if (events.peek() != null) {
            states.peek().handleEvent(events.poll());
        }
    }

    public void update() {
        states.peek().update();
    }

    public void draw() {
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

    public void pause() {
        states.peek().pause();
        running = false;
    }

    public void resume() {
        running = true;
    }

    public void stop() {
        running = false;
        done = true;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isTouchEnabled() {
        return touchEnabled;
    }
}
