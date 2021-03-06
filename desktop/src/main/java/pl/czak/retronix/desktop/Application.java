package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Sound;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.states.WelcomeState;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame implements Backend {
    private static Map<Integer, Event> EVENT_MAP = new HashMap<>();

    static {
        EVENT_MAP.put(KeyEvent.VK_UP, Event.UP);
        EVENT_MAP.put(KeyEvent.VK_DOWN, Event.DOWN);
        EVENT_MAP.put(KeyEvent.VK_RIGHT, Event.RIGHT);
        EVENT_MAP.put(KeyEvent.VK_LEFT, Event.LEFT);
        EVENT_MAP.put(KeyEvent.VK_ENTER, Event.SELECT);
        EVENT_MAP.put(KeyEvent.VK_ESCAPE, Event.BACK);
    }

    private Screen screen;
    private Game game;
    private Thread gameThread;

    public Application() {
        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        screen = new Screen();
        add(screen);
        pack();

        // Enable double buffering
        screen.createBufferStrategy(2);

        SoundEffect.init();

        game = new Game(this);
        game.pushState(new WelcomeState(game));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Event event = EVENT_MAP.get(e.getKeyCode());
                if (event != null) game.addEvent(event);
            }
        });

        screen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.addEvent(new Event(Event.Type.CLICK, e.getX()/4, e.getY()/4));
            }
        });

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                game.resume();
                gameThread = new Thread(new MainLoop());
                gameThread.start();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                game.pause();
                try {
                    gameThread.join();
                } catch (InterruptedException ignored) { }
            }
        });
    }

    @Override
    public void playSound(Sound sound) {
        switch (sound) {
            case LEVEL_COMPLETE:
                SoundEffect.LEVEL_COMPLETE.play();
                break;
            case DEATH:
                SoundEffect.DEATH.play();
                break;
            case GAME_OVER:
                SoundEffect.GAME_OVER.play();
                break;
            case DANGER:
                SoundEffect.DANGER.play();
                break;
        }
    }

    @Override
    public void draw(State state) {
        screen.draw(state);
    }

    @Override
    public boolean isTouchEnabled() {
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }

    private class MainLoop implements Runnable {
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
                } catch (InterruptedException ignored) { }
            }

            if (game.isDone()) System.exit(0);
        }
    }
}
