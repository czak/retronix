package pl.czak.retronix.engine;

/**
 * Created by czak on 18/04/16.
 */
public class Event {

    public enum Type {
        UP, DOWN, LEFT, RIGHT,
        SELECT,
        BACK,
        CLICK
    }

    public static final Event UP = new Event(Type.UP);
    public static final Event DOWN = new Event(Type.DOWN);
    public static final Event LEFT = new Event(Type.LEFT);
    public static final Event RIGHT = new Event(Type.RIGHT);
    public static final Event SELECT = new Event(Type.SELECT);
    public static final Event BACK = new Event(Type.BACK);

    private final Type type;
    private final int x;
    private final int y;

    public Event(Type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Event(Type type) {
        this(type, 0, 0);
    }

    public Type getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWithinBounds(int x, int y, int width, int height) {
        return this.x >= x &&
                this.y >= y &&
                this.x <= x + width &&
                this.y <= y + height;
    }
}
