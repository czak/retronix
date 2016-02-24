package pl.czak.retronix;

import java.util.Arrays;
import java.util.List;

public class Game {
    private GameRenderer renderer;
    private Board board;
    private Player player;
    private List<Enemy> enemies;

    public Game(GameRenderer renderer) {
        this.renderer = renderer;
        this.board = new Board();
        this.player = new Player(board);
        this.enemies = Arrays.asList(new Enemy(board, Board.Field.SEA));
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: Provide a way to exit the loop
                while (true) {
                    update();
                    renderer.render(board, player, enemies);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    private void update() {
        player.move();
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    public void setPlayerDirection(Direction direction) {
        player.setDirection(direction);
    }
}
