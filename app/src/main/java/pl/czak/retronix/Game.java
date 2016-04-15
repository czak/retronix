package pl.czak.retronix;

import android.view.KeyEvent;
import pl.czak.retronix.engine.GameState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Game {
    private static Set<Integer> HANDLED_KEYCODES = new HashSet<>(Arrays.asList(
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT
    ));

    private GameState state;
    private KeyEvent event;

    public boolean setKeyEvent(KeyEvent event) {
        if (HANDLED_KEYCODES.contains(event.getKeyCode())) {
            this.event = event;
            return true;
        }
        return false;
    }

    public void handleEvents() {
        if (event != null) {
            state.handleKeyEvent(event);
            event = null;
        }
    }

    public void update() {
        state.update();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
