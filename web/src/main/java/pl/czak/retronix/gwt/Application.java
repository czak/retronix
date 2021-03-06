package pl.czak.retronix.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import java.io.UnsupportedEncodingException;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.Sound;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.states.WelcomeState;

public class Application implements EntryPoint, Backend, KeyDownHandler {
    private Canvas canvas;
    private Game game;
    private Image font;
    private ImageElement fontElement;

    private Audio audio;

    @SuppressWarnings("JniMissingFunction")
    public static native void setupContext(Context2d ctx) /*-{
        ctx.mozImageSmoothingEnabled = false;
        ctx.webkitImageSmoothingEnabled = false;
        ctx.msImageSmoothingEnabled = false;
        ctx.imageSmoothingEnabled = false;
    }-*/;

    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            Window.alert("Canvas not supported");
            return;
        }
        RootPanel.get("screen").add(canvas);

        CanvasElement el = canvas.getCanvasElement();
        el.setWidth(960);
        el.setHeight(540);

        Context2d ctx = canvas.getContext2d();
        setupContext(ctx);
        ctx.scale(3, 3);

        RootPanel.get().addDomHandler(this, KeyDownEvent.getType());

        font = new Image("images/font.png");
        fontElement = ImageElement.as(font.getElement());

        audio = Audio.createIfSupported();

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
        state.render(new Renderer() {
            @Override
            public void drawSprite(int x, int y, int spriteId) {
                int sx = (spriteId % 32) * 4;
                int sy = (spriteId  / 32) * 4;
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
    public boolean isTouchEnabled() {
        return false;
    }

    @Override
    public void playSound(Sound sound) {
        switch (sound) {
            case LEVEL_COMPLETE: audio.setSrc("sounds/hurra.wav"); break;
            case DEATH: audio.setSrc("sounds/death.wav"); break;
            case GAME_OVER: audio.setSrc("sounds/gameover.wav"); break;
            case DANGER: audio.setSrc("sounds/danger.wav"); break;
        }
        audio.play();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_LEFT: game.addEvent(Event.LEFT); break;
            case KeyCodes.KEY_RIGHT: game.addEvent(Event.RIGHT); break;
            case KeyCodes.KEY_UP: game.addEvent(Event.UP); break;
            case KeyCodes.KEY_DOWN: game.addEvent(Event.DOWN); break;
            case KeyCodes.KEY_ENTER: game.addEvent(Event.SELECT); break;
            case KeyCodes.KEY_ESCAPE: game.addEvent(Event.BACK); break;
            default: return;
        }
    }
}
