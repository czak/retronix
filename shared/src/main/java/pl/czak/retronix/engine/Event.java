package pl.czak.retronix.engine;

/**
 * Created by czak on 18/04/16.
 */
public class Event {
    public enum Type {
        UP, DOWN, LEFT, RIGHT,
        SELECT,
        BACK
    }

    public static final Event UP = new Event(Type.UP);
    public static final Event DOWN = new Event(Type.DOWN);
    public static final Event LEFT = new Event(Type.LEFT);
    public static final Event RIGHT = new Event(Type.RIGHT);
    public static final Event SELECT = new Event(Type.SELECT);
    public static final Event BACK = new Event(Type.BACK);

    public final Type type;

    public Event(Type type) {
        this.type = type;
    }
}
