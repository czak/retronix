package pl.czak.retronix;

import java.util.Arrays;
import java.util.List;

public class Game {
    private GameRenderer renderer;
    private Board board;
    private Player player;
    private List<Enemy> enemies;

    public static class Collision extends Exception {}

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
                        board.clean();
                        resetCharacters();
                    }
                }
            }
        }).start();
    }

    public void setPlayerDirection(Direction direction) {
        player.setDirection(direction);
    }

    private void update() throws Collision {
        if (player.move()) {
            board.fill(enemies);
        }

        for (Enemy enemy : enemies) {
            enemy.detectCollision(board, player);
            enemy.move();
        }
    }

    private void resetCharacters() {
        this.player = new Player(board);
        this.enemies = Arrays.asList(
                new Enemy(board, Board.Field.LAND),
                new Enemy(board, Board.Field.SEA),
                new Enemy(board, Board.Field.SEA)
        );

    }
}
