package pl.czak.retronix.engine;

/**
 * Created by czak on 18/04/16.
 */
public class Event {
    public enum Type {
        KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT,
        KEY_SELECT,
        KEY_BACK
    }

    public static final Event KEY_UP = new Event(Type.KEY_UP);
    public static final Event KEY_DOWN = new Event(Type.KEY_DOWN);
    public static final Event KEY_LEFT = new Event(Type.KEY_LEFT);
    public static final Event KEY_RIGHT = new Event(Type.KEY_RIGHT);
    public static final Event KEY_SELECT = new Event(Type.KEY_SELECT);
    public static final Event KEY_BACK = new Event(Type.KEY_BACK);

    public final Type type;

    public Event(Type type) {
        this.type = type;
    }
}
