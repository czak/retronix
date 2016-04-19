package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
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
                GameEvent event = EVENT_MAP.get(e.getKeyCode());
                if (event != null) game.setGameEvent(event);
            }
        });

        game.run();
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

    @Override
    public void draw(State state) {
        screen.draw(state);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }
}
