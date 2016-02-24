package pl.czak.retronix.desktop;

import pl.czak.retronix.Game;
import pl.czak.retronix.GameHandler;
import pl.czak.retronix.Map;

public class Application implements GameHandler {
    // TODO: Optimize: render only the changed blocks
    public void render(Map map) {
        for (Map.Field[] row : map.getFields()) {
            for (Map.Field f : row) {
                System.out.print(f == Map.Field.LAND ? "#" : " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Application app = new Application();

        Game game = new Game(app);
        game.start();
    }
}
