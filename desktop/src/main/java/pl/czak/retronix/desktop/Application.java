package pl.czak.retronix.desktop;

import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.models.Direction;
import pl.czak.retronix.GameEngine;
import pl.czak.retronix.states.PlayState;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame {
    private static Map<Integer, GameEvent> EVENT_MAP = new HashMap<>();

    static {
        EVENT_MAP.put(KeyEvent.VK_UP, GameEvent.KEY_UP);
        EVENT_MAP.put(KeyEvent.VK_DOWN, GameEvent.KEY_DOWN);
        EVENT_MAP.put(KeyEvent.VK_RIGHT, GameEvent.KEY_RIGHT);
        EVENT_MAP.put(KeyEvent.VK_LEFT, GameEvent.KEY_LEFT);
    }

    public Application() {
        Screen screen = new Screen();
        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(screen);
        pack();

        GameEngine game = new GameEngine(screen);
        game.setState(new PlayState());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameEvent event = EVENT_MAP.get(e.getKeyCode());
                if (event != null) game.setEvent(event);
            }
        });

        game.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }
}
