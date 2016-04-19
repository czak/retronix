package pl.czak.retronix;

import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.GameEvent;

import java.util.Stack;

public class Game {
    private Backend backend;
    private Stack<State> states = new Stack<>();
    private GameEvent lastGameEvent;

    public enum Sound {
        LEVEL_COMPLETE, DEATH, GAME_OVER
    }

    public Game(Backend backend) {
        this.backend = backend;
    }

    public void handleEvents() {
        if (lastGameEvent != null) {
            getCurrentState().handleGameEvent(lastGameEvent);
            lastGameEvent = null;
        }
    }

    public void update() {
        getCurrentState().update();
    }

    public void playSound(Sound sound) {
        backend.playSound(sound);
    }

    public void pushState(State state) {
        states.push(state);
    }

    public State popState() {
        return states.pop();
    }

    public State getCurrentState() {
        return states.peek();
    }

    public void setGameEvent(GameEvent gameEvent) {
        this.lastGameEvent = gameEvent;
    }
}
