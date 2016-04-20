package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.Event;

import static pl.czak.retronix.engine.Canvas.Sprite.*;

/**
 * Created by czak on 15/04/16.
 */
public class WelcomeState extends State {
    private static final String LOGO =
            "::::::::  ::::::::: :::::::: ::::::::   :::::::  ::::   ::: :::::::: :::   :::\n" +
            ":+:   :+: :+:          ::    :+:   :+: :+:   :+: :+:+:  :+:    ::    :+:   :+:\n" +
            "+:+   +:+ +:+          ++    +:+   +:+ +:+   +:+ :+:+:: +:+    ++     +:+ +:+ \n" +
            "+#++++#:  +#++++#      ++    +#++++#:  +#+   +:+ +#+ +: +#+    ++      +#+:+  \n" +
            "+#+   +#+ +#+          ++    +#+   +#+ +#+   +#+ +#+  +##+#    ++     +#+ +#+ \n" +
            "#+#   #+# #+#          ##    #+#   #+# #+#   #+# #+#   ##+#    ##    #+#   #+#\n" +
            "###   ### #########    ##    ###   ###  #######  ###    ### ######## ###   ###";

    public WelcomeState(Game game) {
        super(game);
    }

    @Override
    public void handleEvent(Event event) {
        if (event == Event.KEY_SELECT) {
            game.pushState(new PlayState(game));
        }
    }

    @Override
    public void render(Canvas canvas) {
        int x, y = 4;
        for (String line : LOGO.split("\\n")) {
            x = 0;
            for (char ch : line.toCharArray()) {
                x += 4;
                Canvas.Sprite sprite;
                switch (ch) {
                    case ':': sprite = FILL_1; break;
                    case '+': sprite = FILL_2; break;
                    case '#': sprite = FILL_3; break;
                    default: continue;
                }
                canvas.drawSprite(x, y, sprite);
            }
            y += 4;
        }

        canvas.drawString(104, 100, "> START GAME <");

        canvas.drawString(68, 154, "Made by £ukasz Adamczak");
        canvas.drawString(8, 168, "Based on Xonix by Ilan Rav & Dani Katz");
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
