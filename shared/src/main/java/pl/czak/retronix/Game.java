package pl.czak.retronix;

public class Game {
    private GameRenderer renderer;
    private Board board;

    public Game(GameRenderer renderer) {
        this.renderer = renderer;
        this.board = new Board();
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    renderer.render(board);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

                System.out.println("Game over");
            }
        }).start();
    }
}
