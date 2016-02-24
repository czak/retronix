package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;

import javax.swing.*;

public class Application {
    static class Window extends JFrame {
        public Window() {
            Screen screen = new Screen();

            setTitle("Retronix");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setContentPane(screen);
            pack();

            new Game(screen).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window().setVisible(true);
            }
        });
    }
}
