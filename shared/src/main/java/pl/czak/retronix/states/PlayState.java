package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.State;
import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameEvent;
import pl.czak.retronix.models.*;

import java.util.Arrays;
import java.util.List;

import static pl.czak.retronix.engine.Canvas.Sprite.*;

/**
 * Created by czak on 15/04/16.
 */
public class PlayState extends State {
    private static final int BOARD_WIDTH = 80;
    private static final int BOARD_HEIGHT = 43;
    private static final double BOARD_FILL_THRESHOLD = 0.8;

    private Board board;
    private Player player;
    private List<Enemy> enemies;

    private int score;

    public PlayState(Game game) {
        super(game);

        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        player = new Player(BOARD_WIDTH / 2, 0);
        enemies = Arrays.asList(
                new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()),
                new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
        );
    }

    @Override
    public void handleGameEvent(GameEvent event) {
        switch (event) {
            case KEY_UP:
                player.setDirection(Direction.NORTH);
                break;
            case KEY_DOWN:
                player.setDirection(Direction.SOUTH);
                break;
            case KEY_LEFT:
                player.setDirection(Direction.WEST);
                break;
            case KEY_RIGHT:
                player.setDirection(Direction.EAST);
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        try {
            movePlayer();
            moveEnemies();

            // Have we completed the level?
            if (board.getFillRatio() >= BOARD_FILL_THRESHOLD) {
                System.out.println("Level complete");

                board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
                player = new Player(BOARD_WIDTH / 2, 0);
                enemies = Arrays.asList(
                        new LandEnemy(board.randomPosition(Board.Field.LAND), Direction.randomDiagonal()),
                        new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()),
                        new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal())
                );
            }
        } catch (Collision e) {
            System.out.println("You're dead");
            game.popState();
        }
    }

    private void movePlayer() throws Collision {
        if (player.getDirection() == null) return;

        Position position = player.getPosition();
        Position nextPosition = player.getNextPosition();

        // Definitely stop if trying to move outside bounds
        if (!board.isWithinBounds(nextPosition)) {
            player.stop();
            return;
        }

        // Going back into the sand is a collision
        if (board.getField(nextPosition) == Board.Field.SAND)
            throw new Collision();

        // If moving across sea, leave a bag of sand behind
        if (board.getField(position) == Board.Field.SEA) {
            board.setField(position, Board.Field.SAND);

            // If re-entering dry land, stop immediately
            if (board.getField(nextPosition) == Board.Field.LAND) {
                player.stop();

                // And fill the board
                score += board.fill(enemies);
            }
        }

        player.setPosition(nextPosition);
    }

    private void moveEnemies() throws Collision {
        for (Enemy enemy : enemies) {
            // Sea enemies collide with in-progress SAND walls
            if (enemy instanceof SeaEnemy && board.getField(enemy.getNextPosition()) == Board.Field.SAND)
                throw new Collision();

            bounceEnemy(enemy);

            // Detect collision with player
            collideEnemyWithPlayer(enemy);

            // There's still a possibility the enemy can't move (i.e. gets stuck)
            // so we check one more time before actually moving.
            Position nextPosition = enemy.getNextPosition();
            if (canMoveEnemy(enemy, nextPosition)) {
                enemy.setPosition(nextPosition);
            }

            // Need to recheck collisions, otherwise player might
            // pass an enemy untouched.
            // TODO: Clean this up. Should be doable in a single pass.
            collideEnemyWithPlayer(enemy);
        }
    }

    private void bounceEnemy(Enemy enemy) {
        Position position = enemy.getPosition();
        Direction direction = enemy.getDirection();

        // Wall in my horizontal direction?
        if (!canMoveEnemy(enemy, position.movedHorizontally(direction))) {
            direction = direction.flippedX();
        }

        // Wall in my vertical direction?
        if (!canMoveEnemy(enemy, position.movedVertically(direction))) {
            direction = direction.flippedY();
        }

        // Running straight into a corner?
        if (!canMoveEnemy(enemy, position.movedTo(direction))) {
            direction = direction.flippedX().flippedY();
        }

        // Update the direction after bouncing
        enemy.setDirection(direction);
    }

    private boolean canMoveEnemy(Enemy enemy, Position position) {
        return board.getField(position) == enemy.getNativeField();
    }

    /**
     * Detect a collision between the given enemy and the player.
     * @param enemy
     * @throws Collision
     */
    private void collideEnemyWithPlayer(Enemy enemy) throws Collision {
        Position position = enemy.getPosition();
        Direction direction = enemy.getDirection();

        // Is it a direct hit?
        Position nextPosition = position.movedTo(direction);
        if (board.getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();

        // Is it a hit from the side?
        nextPosition = position.movedHorizontally(direction);
        if (board.getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();

        // Is it a hit from top or bottom?
        nextPosition = position.movedVertically(direction);
        if (board.getField(nextPosition) == enemy.getNativeField() &&
                nextPosition.equals(player.getPosition()))
            throw new Collision();
    }
    @Override
    public void render(Canvas canvas) {
        // Single field size is 4px
        final int FIELD_SIZE = 4;

        // Draw the board
        int y = 0;
        for (Board.Field[] row : board.getFields()) {
            int x = 0;
            for (Board.Field f : row) {
                if (f != Board.Field.SEA)
                    canvas.fillRect(x, y, FIELD_SIZE, FIELD_SIZE, colorForField(f));
                x += FIELD_SIZE;
            }
            y += FIELD_SIZE;
        }

        // Draw the player
        Position pos = player.getPosition();
        Canvas.Sprite sprite = board.getField(pos) == Board.Field.LAND ? PLAYER_LAND : PLAYER_SEA;
        canvas.drawSprite(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, sprite);

        // Draw the enemies
        for (Enemy enemy : enemies) {
            pos = enemy.getPosition();
            sprite = enemy instanceof SeaEnemy ? SEA_ENEMY : LAND_ENEMY;
            canvas.drawSprite(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, sprite);
        }

        // Bottom info
        canvas.drawString(0, 172, "Score: " + score);
        canvas.drawString(120, 172, String.format("Full: %d%%", (int) (board.getFillRatio() * 100)));
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

    private class Collision extends Exception {}
}
