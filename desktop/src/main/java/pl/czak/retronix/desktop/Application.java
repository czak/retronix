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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame implements Backend {
    private static Map<Integer, Event> EVENT_MAP = new HashMap<>();

    static {
        EVENT_MAP.put(KeyEvent.VK_UP, Event.KEY_UP);
        EVENT_MAP.put(KeyEvent.VK_DOWN, Event.KEY_DOWN);
        EVENT_MAP.put(KeyEvent.VK_RIGHT, Event.KEY_RIGHT);
        EVENT_MAP.put(KeyEvent.VK_LEFT, Event.KEY_LEFT);
        EVENT_MAP.put(KeyEvent.VK_ENTER, Event.KEY_SELECT);
        EVENT_MAP.put(KeyEvent.VK_ESCAPE, Event.KEY_BACK);
    }

    private Screen screen;

    public Application() {
        screen = new Screen();
        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(screen);
        pack();

        SoundEffect.init();

        Game game = new Game(this);
        game.pushState(new WelcomeState(game));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Event event = EVENT_MAP.get(e.getKeyCode());
                if (event != null) game.addEvent(event);
            }
        });

        new Thread(() -> {
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

            System.exit(0);
        }).start();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }
}
