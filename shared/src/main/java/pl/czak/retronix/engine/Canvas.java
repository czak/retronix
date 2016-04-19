package pl.czak.retronix.engine;

/**
 * Created by czak on 18/04/16.
 */
public interface Canvas {
    enum Color {
        WHITE, MAGENTA, CYAN
    }

    enum Sprite {
        FILL_1(0),
        FILL_2(1),
        FILL_3(2),
        FILL_4(3),
        PLAYER_LAND(4),
        PLAYER_SEA(5),
        SEA_ENEMY(6),
        LAND_ENEMY(7);

        private int index;

        Sprite(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    void drawSprite(int x, int y, Sprite sprite);
    void drawString(int x, int y, String text);
    void fillRect(int x, int y, int width, int height, Color color);
}
