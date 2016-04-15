package pl.czak.retronix;

import android.view.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Game {
    private static final Set<Integer> HANDLED_KEYCODES = new HashSet<>(Arrays.asList(
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER
    ));

    private Stack<State> states = new Stack<>();
    private KeyEvent lastKeyEvent;

    public boolean setKeyEvent(KeyEvent event) {
        if (HANDLED_KEYCODES.contains(event.getKeyCode())) {
            this.lastKeyEvent = event;
            return true;
        }
        return false;
    }

    public void handleEvents() {
        if (lastKeyEvent != null) {
            getCurrentState().handleKeyEvent(lastKeyEvent);
            lastKeyEvent = null;
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
