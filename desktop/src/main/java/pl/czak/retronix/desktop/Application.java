package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;

import javax.swing.*;

public class Application extends JFrame {
    public Application() {
        Screen screen = new Screen();

        setTitle("Retronix");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(screen);
        pack();

        new Game(screen).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application app = new Application();
                app.setVisible(true);
            }
        });
    }
}
