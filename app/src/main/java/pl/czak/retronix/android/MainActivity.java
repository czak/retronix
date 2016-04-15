package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import pl.czak.retronix.GameEngine;
import pl.czak.retronix.states.PlayState;

public class MainActivity extends Activity {
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
        return game.setKeyEvent(event);
    }
}
