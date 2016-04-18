package pl.czak.retronix;

import pl.czak.retronix.engine.GameEvent;

import java.util.Stack;

public class Game {
    private Stack<State> states = new Stack<>();
    private GameEvent lastGameEvent;

    public void setGameEvent(GameEvent gameEvent) {
        this.lastGameEvent = gameEvent;
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

    public void pushState(State state) {
        states.push(state);
    }

    public State popState() {
        return states.pop();
    }

    public State getCurrentState() {
        return states.peek();
    }
}
