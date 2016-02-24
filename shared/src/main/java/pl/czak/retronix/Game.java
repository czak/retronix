package pl.czak.retronix;

public class Game {
    private Renderer handler;
    private Board board;

    public Game(Renderer handler) {
        this.handler = handler;
        this.board = new Board();
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    handler.render(board);
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
