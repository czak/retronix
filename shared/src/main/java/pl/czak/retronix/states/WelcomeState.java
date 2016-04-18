package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.GameEvent;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Created by czak on 15/04/16.
 */
public class WelcomeState extends State {
    public WelcomeState(Game game) {
        super(game);
    }

    @Override
    public void handleGameEvent(GameEvent event) {
        if (event == GameEvent.KEY_SELECT) {
            game.pushState(new PlayState(game));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.drawString("Welcome to Retronix", 0, 100);
    }

//    ANDROID VERSION
//    @Override
//    public void render(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
//
//        paint.setColor(Color.WHITE);
//        paint.setTextSize(60);
//        paint.setAntiAlias(true);
//        canvas.drawText("Welcome to Retronix", 0, 100, paint);
//    }
}
