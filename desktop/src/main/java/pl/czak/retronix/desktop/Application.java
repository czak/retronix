package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.states.WelcomeState;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame implements Backend {
    private static Map<Integer, GameEvent> EVENT_MAP = new HashMap<>();

    static {
        EVENT_MAP.put(KeyEvent.VK_UP, GameEvent.KEY_UP);
        EVENT_MAP.put(KeyEvent.VK_DOWN, GameEvent.KEY_DOWN);
        EVENT_MAP.put(KeyEvent.VK_RIGHT, GameEvent.KEY_RIGHT);
        EVENT_MAP.put(KeyEvent.VK_LEFT, GameEvent.KEY_LEFT);
        EVENT_MAP.put(KeyEvent.VK_ENTER, GameEvent.KEY_SELECT);
    }

    public Application() {
        Screen screen = new Screen();
        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(screen);
        pack();

        Game game = new Game(this);
        game.pushState(new WelcomeState(game));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameEvent event = EVENT_MAP.get(e.getKeyCode());
                if (event != null) game.setGameEvent(event);
            }
        });

        SoundEffect.init();

        new Thread(() -> {
            while (true) {
                long start = System.currentTimeMillis();

                game.handleEvents();
                game.update();
                screen.draw(game.getCurrentState());

                long duration = System.currentTimeMillis() - start;

                try {
                    Thread.sleep(Math.max(0, 50 - duration));
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }

    @Override
    public void playSound(Game.Sound sound) {
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
        }
    }
}
