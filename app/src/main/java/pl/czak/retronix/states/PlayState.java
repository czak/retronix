package pl.czak.retronix.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.KeyEvent;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.models.*;

import java.util.Arrays;

/**
 * Created by czak on 15/04/16.
 */
public class PlayState extends State {
    private static final int BOARD_WIDTH = 70;
    private static final int BOARD_HEIGHT = 40;

    private Board board;

    public PlayState() {
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
        board.setEnemies(Arrays.<Enemy>asList(
                new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
        ));
    }

    @Override
    public void handleKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
                board.getPlayer().setDirection(Direction.NORTH);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                board.getPlayer().setDirection(Direction.SOUTH);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                board.getPlayer().setDirection(Direction.WEST);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                board.getPlayer().setDirection(Direction.EAST);
                break;
            default:
                return;
        }
    }

    @Override
    public void update() {
        try {
            board.update();
        } catch (Board.Collision e) {
            System.out.println("You're dead");

            board.clean();
            board.setPlayer(new Player(BOARD_WIDTH / 2, 0));
            board.resetEnemies();
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
        // Single field size
        final float FIELD_SIZE = Math.min((float) canvas.getWidth() / board.getWidth(),
                (float) canvas.getHeight() / board.getHeight());

        // Offset to center in window
        final float TX = ((float) canvas.getWidth() - FIELD_SIZE * board.getWidth()) / 2;
        final float TY = ((float) canvas.getHeight() - FIELD_SIZE * board.getHeight()) / 2;

        RectF rect = new RectF(TX, TY, TX + FIELD_SIZE, TY + FIELD_SIZE);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // Draw the board
        for (Board.Field[] row : board.getFields()) {
            rect.left = TX;
            rect.right = TX + FIELD_SIZE;
            for (Board.Field f : row) {
                paint.setColor(colorForField(f));
                canvas.drawRect(rect, paint);
                rect.offset(FIELD_SIZE, 0);
            }
            rect.offset(0, FIELD_SIZE);
        }

        // Draw the player
        Position pos = board.getPlayer().getPosition();
        rect.offsetTo(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE);
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(rect, paint);

        // Draw the enemies
        for (Enemy enemy : board.getEnemies()) {
            pos = enemy.getPosition();
            rect.offsetTo(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE);
            paint.setColor(Color.RED);
            canvas.drawRect(rect, paint);
        }
    }

    private int colorForField(Board.Field f) {
        switch (f) {
            case LAND:  return Color.GREEN;
            case SEA:   return Color.BLUE;
            case SAND:  return Color.YELLOW;
            default:    return 0;
        }
    }
}
