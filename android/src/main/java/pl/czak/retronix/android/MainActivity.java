package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

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

    private GestureDetectorCompat detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = new Screen(this);
        setContentView(screen);

        detector = new GestureDetectorCompat(this, new GestureListener());

        soundBank = new SoundBank(this);

        game = new Game(this);
        game.pushState(new WelcomeState(game));

        // Main loop in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (game.isRunning()) {
                    long start = System.currentTimeMillis();

                    game.handleEvent();
                    game.update();
                    game.draw();

                    long duration = System.currentTimeMillis() - start;

                    try {
                        Thread.sleep(Math.max(0, 50 - duration));
                    } catch (InterruptedException ignored) {}
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundBank.release();
    }

    @Override
    public void playSound(Sound sound) {
        soundBank.play(sound);
    }

    @Override
    public void draw(State state) {
        screen.draw(state);
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
            case KeyEvent.KEYCODE_BACK:
                game.addEvent(Event.KEY_BACK);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            game.addEvent(Event.KEY_SELECT);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Stolen from http://stackoverflow.com/a/26387629/379822
            float x1 = e1.getX();
            float y1 = e1.getY();

            float x2 = e2.getX();
            float y2 = e2.getY();

            double angle = getAngle(x1, y1, x2, y2);

            if (angle >= 45 && angle < 135) {
                game.addEvent(Event.KEY_UP);
            } else if (angle >= 0 && angle < 45 || angle >= 315 && angle < 360) {
                game.addEvent(Event.KEY_RIGHT);
            } else if (angle >= 225 && angle < 315) {
                game.addEvent(Event.KEY_DOWN);
            } else {
                game.addEvent(Event.KEY_LEFT);
            }

            return true;
        }

        private double getAngle(float x1, float y1, float x2, float y2) {
            double rad = Math.atan2(y1-y2, x2-x1) + Math.PI;
            return (rad * 180/Math.PI + 180) % 360;
        }
    }
}