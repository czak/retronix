package pl.czak.retronix;

import java.util.Arrays;

public class Game {
    private static final int BOARD_WIDTH = 70;
    private static final int BOARD_HEIGHT = 40;

    private GameRenderer renderer;
    private Board board;

    // TODO: Game keeps score, lives, current level

    public Game(GameRenderer renderer) {
        this.renderer = renderer;

        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
        board.setEnemies(Arrays.<Enemy>asList(
                new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
        ));
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: Provide a way to exit the loop

                boolean dead = false;

                while (true) {
                    try {
                        board.update();
                    } catch (Board.Collision e) {
                        e.printStackTrace();
                        dead = true;
                    }

                    renderer.render(board);

                    // TODO: Improve timeout for consistent FPS/game rate
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}

                    // Handle death
                    if (dead) {
                        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

                        board.clean();
                        board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
                        board.resetEnemies();
                        dead = false;
                    }
                }
            }
        }).start();
    }

    public void setPlayerDirection(Direction direction) {
        board.getPlayer().setDirection(direction);
    }
}
