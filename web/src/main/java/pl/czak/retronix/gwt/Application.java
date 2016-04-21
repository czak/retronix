package pl.czak.retronix.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Backend;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.states.WelcomeState;

public class Application implements EntryPoint, Backend, KeyDownHandler {
    private Canvas canvas;
    private Game game;

    public void onModuleLoad() {
        canvas = Canvas.createIfSupported();
        if (canvas == null) {
            Window.alert("Canvas not supported");
            return;
        }
        RootPanel.get().add(canvas);

        CanvasElement el = canvas.getCanvasElement();
        el.setWidth(320);
        el.setHeight(180);

        canvas.setFocus(true);
        canvas.addKeyDownHandler(this);

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
                ctx.setFillStyle("#ff0");
                ctx.fillRect(x, y, 4, 4);
            }

            @Override
            public void drawString(int x, int y, String text) {

            }

            @Override
            public void fillRect(int x, int y, int width, int height, Color color) {
                ctx.setFillStyle("#fff");
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
