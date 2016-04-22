package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.State;

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

    // Sprites used for the logo
    private static final int SPRITE_FILL_1 = 0;
    private static final int SPRITE_FILL_2 = 1;
    private static final int SPRITE_FILL_3 = 2;

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
    public void render(Renderer renderer) {
        int x, y = 4;
        for (String line : LOGO.split("\\n")) {
            x = 0;
            for (char ch : line.toCharArray()) {
                x += 4;
                int spriteId;
                switch (ch) {
                    case ':': spriteId = SPRITE_FILL_1; break;
                    case '+': spriteId = SPRITE_FILL_2; break;
                    case '#': spriteId = SPRITE_FILL_3; break;
                    default: continue;
                }
                renderer.drawSprite(x, y, spriteId);
            }
            y += 4;
        }

        renderer.drawString(104, 100, "> START GAME <");

        renderer.drawString(68, 154, "Made by £ukasz Adamczak");
        renderer.drawString(8, 168, "Based on Xonix by Ilan Rav & Dani Katz");
    }

//    ANDROID VERSION
//    @Override
//    public void render(Renderer canvas) {
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
