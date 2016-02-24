package pl.czak.retronix;

public class Game {
    private GameHandler handler;
    private Map map;

    public Game(GameHandler handler) {
        this.handler = handler;
        this.map = new Map();
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
                    handler.render(map);
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
