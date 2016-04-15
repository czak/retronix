package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.models.Direction;
import pl.czak.retronix.GameEngine;
import pl.czak.retronix.states.PlayState;

public class MainActivity extends Activity {
    private static Map<Integer, GameEvent> EVENT_MAP = new HashMap<>();

    static {
        EVENT_MAP.put(KeyEvent.KEYCODE_DPAD_UP, GameEvent.KEY_UP);
        EVENT_MAP.put(KeyEvent.KEYCODE_DPAD_DOWN, GameEvent.KEY_DOWN);
        EVENT_MAP.put(KeyEvent.KEYCODE_DPAD_RIGHT, GameEvent.KEY_RIGHT);
        EVENT_MAP.put(KeyEvent.KEYCODE_DPAD_LEFT, GameEvent.KEY_LEFT);
    }

    private GameEngine game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Screen screen = new Screen(this);
        setContentView(screen);

        game = new GameEngine(screen);
        game.setState(new PlayState());
        game.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        GameEvent gameEvent = EVENT_MAP.get(keyCode);
        if (gameEvent != null) {
            game.setEvent(gameEvent);
            return true;
        }

        return false;
    }
}
