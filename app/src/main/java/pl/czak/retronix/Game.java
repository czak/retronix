package pl.czak.retronix;

import android.view.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Game {
    private static final Set<Integer> HANDLED_KEYCODES = new HashSet<>(Arrays.asList(
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT
    ));

    private State currentState;
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
            currentState.handleKeyEvent(lastKeyEvent);
            lastKeyEvent = null;
        }
    }

    public void update() {
        currentState.update();
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
