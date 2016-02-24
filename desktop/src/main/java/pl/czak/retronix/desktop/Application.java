package pl.czak.retronix.desktop;

import pl.czak.retronix.Direction;
import pl.czak.retronix.Game;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame {
    private static Map<Integer, Direction> DIRECTION_MAP = new HashMap<>();

    static {
        DIRECTION_MAP.put(KeyEvent.VK_UP, Direction.NORTH);
        DIRECTION_MAP.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        DIRECTION_MAP.put(KeyEvent.VK_RIGHT, Direction.EAST);
        DIRECTION_MAP.put(KeyEvent.VK_LEFT, Direction.WEST);
    }

    public Application() {
        Screen screen = new Screen();
        Game game = new Game(screen);

        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(screen);
        pack();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Direction dir = DIRECTION_MAP.get(e.getKeyCode());
                if (dir != null) game.setPlayerDirection(dir);
            }
        });

        game.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Application().setVisible(true));
    }
}
