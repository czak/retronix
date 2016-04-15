package pl.czak.retronix;

import android.view.KeyEvent;
import pl.czak.retronix.android.Screen;
import pl.czak.retronix.engine.GameState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameEngine {
    private static Set<Integer> HANDLED_KEYCODES = new HashSet<>(Arrays.asList(
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT
    ));

    private Screen screen;
    private GameState state;
    private KeyEvent event;

    public GameEngine(Screen screen) {
        this.screen = screen;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean setKeyEvent(KeyEvent event) {
        if (HANDLED_KEYCODES.contains(event.getKeyCode())) {
            this.event = event;
            return true;
        }
        return false;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (event != null) {
                        state.handleKeyEvent(event);
                        event = null;
                    }

                    state.update();

                    screen.render(state);

                    // TODO: Improve timeout for consistent FPS/game rate
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }
}
