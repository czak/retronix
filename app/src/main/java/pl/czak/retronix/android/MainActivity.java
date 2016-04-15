package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import pl.czak.retronix.Game;
import pl.czak.retronix.states.PlayState;

public class MainActivity extends Activity {
    private Game game;
    private Screen screen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screen = new Screen(this);
        setContentView(screen);

        game = new Game();
        game.setCurrentState(new PlayState());

        // Main loop in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    game.handleEvents();
                    game.update();
                    screen.draw(game.getCurrentState());

                    // TODO: Improve timeout for consistent FPS/game rate
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return game.setKeyEvent(event);
    }
}
