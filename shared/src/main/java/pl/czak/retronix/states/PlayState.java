package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.models.*;

import java.util.Arrays;

/**
 * Created by czak on 15/04/16.
 */
public class PlayState extends State {
    private static final int BOARD_WIDTH = 80;
    private static final int BOARD_HEIGHT = 45;

    private Board board;

    public PlayState(Game game) {
        super(game);

        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
        board.setEnemies(Arrays.<Enemy>asList(
                new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
        ));
    }

    @Override
    public void handleGameEvent(GameEvent event) {
        switch (event) {
            case KEY_UP:
                board.getPlayer().setDirection(Direction.NORTH);
                break;
            case KEY_DOWN:
                board.getPlayer().setDirection(Direction.SOUTH);
                break;
            case KEY_LEFT:
                board.getPlayer().setDirection(Direction.WEST);
                break;
            case KEY_RIGHT:
                board.getPlayer().setDirection(Direction.EAST);
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        try {
            board.update();
        } catch (Board.Collision e) {
            System.out.println("You're dead");
            game.popState();
        } catch (Board.LevelComplete e) {
            System.out.println("Level complete");

            board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
            board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
            board.setEnemies(Arrays.<Enemy>asList(
                    new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                    new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()),
                    new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
            ));
        }
    }

    @Override
    public void render(Canvas canvas) {
        // Single field size is 4px
        final int FIELD_SIZE = 4;

        int x = 0, y = 0;

        // Draw the board
        for (Board.Field[] row : board.getFields()) {
            x = 0;
            for (Board.Field f : row) {
                if (f != Board.Field.SEA)
                    canvas.fillRect(x, y, FIELD_SIZE, FIELD_SIZE, colorForField(f));
                x += FIELD_SIZE;
            }
            y += FIELD_SIZE;
        }

        // Draw the player
        Position pos = board.getPlayer().getPosition();
        canvas.fillRect(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE, Canvas.Color.WHITE);

        // Draw the enemies
        for (Enemy enemy : board.getEnemies()) {
            pos = enemy.getPosition();
            canvas.fillRect(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE, Canvas.Color.WHITE);
        }
    }

//    THIS IS ANDROID SPECIFIC VERSION OF THE ABOVE
//    @Override
//    public void render(Canvas canvas) {
//        // Single field size
//        final float FIELD_SIZE = Math.min((float) canvas.getWidth() / board.getWidth(),
//                (float) canvas.getHeight() / board.getHeight());
//
//        // Offset to center in window
//        final float TX = ((float) canvas.getWidth() - FIELD_SIZE * board.getWidth()) / 2;
//        final float TY = ((float) canvas.getHeight() - FIELD_SIZE * board.getHeight()) / 2;
//
//        RectF rect = new RectF(TX, TY, TX + FIELD_SIZE, TY + FIELD_SIZE);
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//
//        // Draw the board
//        for (Board.Field[] row : board.getFields()) {
//            rect.left = TX;
//            rect.right = TX + FIELD_SIZE;
//            for (Board.Field f : row) {
//                paint.setColor(colorForField(f));
//                canvas.drawRect(rect, paint);
//                rect.offset(FIELD_SIZE, 0);
//            }
//            rect.offset(0, FIELD_SIZE);
//        }
//
//        // Draw the player
//        Position pos = board.getPlayer().getPosition();
//        rect.offsetTo(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE);
//        paint.setColor(Color.MAGENTA);
//        canvas.drawRect(rect, paint);
//
//        // Draw the enemies
//        for (Enemy enemy : board.getEnemies()) {
//            pos = enemy.getPosition();
//            rect.offsetTo(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE);
//            paint.setColor(Color.RED);
//            canvas.drawRect(rect, paint);
//        }
//    }

    private Canvas.Color colorForField(Board.Field f) {
        switch (f) {
            case LAND:  return Canvas.Color.CYAN;
            case SAND:  return Canvas.Color.MAGENTA;
            default:    return null;
        }
    }
}
