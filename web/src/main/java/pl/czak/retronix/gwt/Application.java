package pl.czak.retronix.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.states.WelcomeState;

public class Application implements EntryPoint, Backend, KeyDownHandler {
    private Canvas canvas;
    private Game game;
    private Image font;
    private ImageElement fontElement;

    @SuppressWarnings("JniMissingFunction")
    public static native void setupContext(Context2d ctx) /*-{
        ctx.imageSmoothingEnabled = false;
    }-*/;

    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            Window.alert("Canvas not supported");
            return;
        }
        RootPanel.get().add(canvas);

        CanvasElement el = canvas.getCanvasElement();
        el.setWidth(640);
        el.setHeight(360);

        Context2d ctx = canvas.getContext2d();
        setupContext(ctx);
        ctx.scale(2, 2);

        canvas.setFocus(true);
        canvas.addKeyDownHandler(this);

        font = new Image("images/font.png");
        fontElement = ImageElement.as(font.getElement());

        game = new Game(this);
        game.pushState(new WelcomeState(game));

        new Timer() {
            @Override
            public void run() {
                game.handleEvent();
                game.update();
                game.draw();
            }
        }.scheduleRepeating(50);
    }

    @Override
    public void draw(State state) {
        final Context2d ctx = canvas.getContext2d();
        ctx.setFillStyle("#000");
        ctx.fillRect(0, 0, 320, 180);
        state.render(new pl.czak.retronix.engine.Canvas() {
            @Override
            public void drawSprite(int x, int y, Sprite sprite) {
                int index = sprite.getIndex();
                int sx = (index % 32) * 4;
                int sy = (index / 32) * 4;
                ctx.drawImage(fontElement, sx, sy, 4, 4, x, y, 4, 4);
            }

            @Override
            public void drawString(int x, int y, String text) {
                byte[] bytes = new byte[0];
                try {
                    // TODO: I can safely drop the encoding.
                    //       I'm barely using 50 characters.
                    bytes = text.getBytes("ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for (byte b : bytes) {
                    int index = b & 0xff;
                    int sx = (index % 16) * 8;
                    int sy = (index / 16) * 8;
                    ctx.drawImage(fontElement, sx, sy, 8, 8, x, y, 8, 8);
                    x+=8;
                }
            }

            @Override
            public void fillRect(int x, int y, int width, int height, Color color) {
                String fillColor = "#000";
                switch (color) {
                    case WHITE: fillColor = "#fff"; break;
                    case CYAN: fillColor = "#00a8a8"; break;
                    case MAGENTA: fillColor = "#a800a8"; break;
                }
                ctx.setFillStyle(fillColor);
                ctx.fillRect(x, y, width, height);
            }
        });
    }

    @Override
    public void playSound(Game.Sound sound) {
        GWT.log("Playing sound: " + sound.toString());
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_LEFT: game.addEvent(Event.KEY_LEFT); break;
            case KeyCodes.KEY_RIGHT: game.addEvent(Event.KEY_RIGHT); break;
            case KeyCodes.KEY_UP: game.addEvent(Event.KEY_UP); break;
            case KeyCodes.KEY_DOWN: game.addEvent(Event.KEY_DOWN); break;
            case KeyCodes.KEY_ENTER: game.addEvent(Event.KEY_SELECT); break;
            default: return;
        }
    }
}
