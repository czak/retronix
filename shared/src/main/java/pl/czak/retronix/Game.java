package pl.czak.retronix;

import java.util.Arrays;
import java.util.List;

public class Game {
    private GameRenderer renderer;
    private Board board;
    private Player player;
    private List<Enemy> enemies;

    public static class Collision extends Exception {}
    public static class LevelComplete extends Exception {}

    public Game(GameRenderer renderer) {
        this.renderer = renderer;
        this.board = new Board();
        resetCharacters();
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: Provide a way to exit the loop
                while (true) {
                    try {
                        update();
                        renderer.render(board, player, enemies);

                        // TODO: Improve timeout for consistent FPS/game rate
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException ignored) {}
                    } catch (Collision collision) {
                        System.out.println("You're dead!");
                        // TODO: Decrement lives
                        // TODO: If last life, game over
                        board.clean();
                        resetCharacters();
                    } catch (LevelComplete e) {
                        System.out.println("Level complete!");
                        // TODO: Increment level
                        board = new Board();
                        resetCharacters();
                    }
                }
            }
        }).start();
    }

    public void setPlayerDirection(Direction direction) {
        player.setDirection(direction);
    }

    private void update() throws Collision, LevelComplete {
        if (player.move()) {
            double area = board.fill(enemies);
            System.out.println("Covered: " + area);
            if (area >= 0.8) throw new LevelComplete();
        }

        for (Enemy enemy : enemies) {
            enemy.bounce();
            enemy.detectCollision(board, player);
            enemy.move();
        }
    }

    private void resetCharacters() {
        this.player = new Player(board);
        // TODO: Make this dependent on the current level
        this.enemies = Arrays.asList(
                new Enemy(board, Board.Field.LAND),
                new Enemy(board, Board.Field.SEA),
                new Enemy(board, Board.Field.SEA)
        );

    }
}
