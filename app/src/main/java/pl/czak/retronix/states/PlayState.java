package pl.czak.retronix.states;

import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.engine.GameState;
import pl.czak.retronix.models.*;

import static pl.czak.retronix.engine.Canvas.Color;

import java.util.Arrays;

/**
 * Created by czak on 15/04/16.
 */
public class PlayState extends GameState {
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
    public void handleEvent(GameEvent event) {
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
        final double FIELD_SIZE = Math.min((double) canvas.getWidth() / board.getWidth(),
                (double) canvas.getHeight() / board.getHeight());

        // Offset to center in window
        final double TX = ((double) canvas.getWidth() - FIELD_SIZE * board.getWidth()) / 2;
        final double TY = ((double) canvas.getHeight() - FIELD_SIZE * board.getHeight()) / 2;

        double x = TX, y = TY;

        // Draw the board
        for (Board.Field[] row : board.getFields()) {
            x = TX;
            for (Board.Field f : row) {
                canvas.fillRect(x, y, FIELD_SIZE, FIELD_SIZE, colorForField(f));
                x += FIELD_SIZE;
            }
            y += FIELD_SIZE;
        }

        // Draw the player
        Position pos = board.getPlayer().getPosition();
        canvas.fillRect(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE,
                Color.MAGENTA);

        // Draw the enemies
        for (Enemy enemy : board.getEnemies()) {
            pos = enemy.getPosition();
            canvas.fillRect(TX + pos.x * FIELD_SIZE, TY + pos.y * FIELD_SIZE, FIELD_SIZE, FIELD_SIZE,
                    Color.RED);
        }
    }

    private Color colorForField(Board.Field f) {
        switch (f) {
            case LAND:  return Color.GREEN;
            case SEA:   return Color.BLUE;
            case SAND:  return Color.YELLOW;
            default:    return null;
        }
    }
}
