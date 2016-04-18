package pl.czak.retronix.desktop;

import pl.czak.retronix.State;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Created by czak on 24/02/16.
 */
public class Screen extends JPanel {
    private State state;

    public Screen() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(Color.BLACK);
    }

    public void draw(State state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(4, 4);
        state.render(g2);
    }
}