package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Sound;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.states.WelcomeState;

public class MainActivity extends Activity implements Backend {
    private Game game;
    private Screen screen;
    private SoundBank soundBank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screen = new Screen(this);
        setContentView(screen);

        soundBank = new SoundBank(this);

        game = new Game(this);
        game.pushState(new WelcomeState(game));

        // Main loop in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();

                    game.handleEvent();
                    game.update();
                    game.draw();

                    long duration = System.currentTimeMillis() - start;

                    try {
                        Thread.sleep(Math.max(0, 50 - duration));
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundBank.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                game.addEvent(Event.KEY_UP);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                game.addEvent(Event.KEY_DOWN);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                game.addEvent(Event.KEY_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                game.addEvent(Event.KEY_RIGHT);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                game.addEvent(Event.KEY_SELECT);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void playSound(Sound sound) {
        soundBank.play(sound);
    }

    @Override
    public void draw(State state) {
        screen.draw(state);
    }
}
