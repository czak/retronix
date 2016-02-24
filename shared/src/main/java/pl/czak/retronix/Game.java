package pl.czak.retronix;

public class Game {
    private GameRenderer renderer;
    private Board board;
    private Player player;

    public Game(GameRenderer renderer) {
        this.renderer = renderer;
        this.board = new Board();
        this.player = new Player(board);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: Provide a way to exit the loop
                while (true) {
                    update();
                    renderer.render(board, player);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();
    }

    private void update() {
        player.move();
    }

    public void setPlayerDirection(Direction direction) {
        player.setDirection(direction);
    }
}
