package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import pl.czak.retronix.Direction;
import pl.czak.retronix.Game;

public class MainActivity extends Activity {
    private static Map<Integer, Direction> DIRECTION_MAP = new HashMap<>();

    static {
        DIRECTION_MAP.put(KeyEvent.KEYCODE_DPAD_UP, Direction.NORTH);
        DIRECTION_MAP.put(KeyEvent.KEYCODE_DPAD_DOWN, Direction.SOUTH);
        DIRECTION_MAP.put(KeyEvent.KEYCODE_DPAD_RIGHT, Direction.EAST);
        DIRECTION_MAP.put(KeyEvent.KEYCODE_DPAD_LEFT, Direction.WEST);
    }

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Screen screen = new Screen(this);
        setContentView(screen);

        game = new Game(screen);
        game.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Direction dir = DIRECTION_MAP.get(keyCode);
        if (dir != null) {
            game.setPlayerDirection(dir);
            return true;
        }

        return false;
    }
}
