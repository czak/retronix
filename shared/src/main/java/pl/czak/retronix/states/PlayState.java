package pl.czak.retronix.states;

import pl.czak.retronix.Game;
import pl.czak.retronix.engine.Event;
import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.State;
import pl.czak.retronix.models.*;

import java.util.ArrayList;
import java.util.List;

import static pl.czak.retronix.engine.Sound.*;

/**
 * Created by czak on 15/04/16.
 */
public class PlayState extends State {
    private static final int BOARD_WIDTH = 80;
    private static final int BOARD_HEIGHT = 43;
    private static final double BOARD_FILL_THRESHOLD = 0.8;
    private static final int LAND_ENEMY_TIMEOUT = 1820;

    // Sprites used for this state
    private static final int SPRITE_PLAYER_LAND = 4;
    private static final int SPRITE_PLAYER_SEA = 5;
    private static final int SPRITE_SEA_ENEMY = 6;
    private static final int SPRITE_LAND_ENEMY = 7;

    private Board board;
    private Player player;
    private List<LandEnemy> landEnemies;
    private List<SeaEnemy> seaEnemies;

    private int score = 0;
    private int level = 1;
    private int lives = 3;

    private boolean died;
    private boolean levelCompleted;

    private int delay;
    private int timeout;

    public PlayState(Game game) {
        super(game);
        initialize();
    }

    private void initialize() {
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        player = new Player(BOARD_WIDTH / 2, 0);

        // n+2 sea enemies on nth level
        seaEnemies = new ArrayList<>();
        for (int i = 0; i < level + 2; i++) {
            seaEnemies.add(new SeaEnemy(board.randomPosition(Board.Field.SEA), Direction.randomDiagonal()));
        }

        resetLandEnemies();
    }

    private void resetLandEnemies() {
        // Start with 1 land enemy on every level
        landEnemies = new ArrayList<>();
        landEnemies.add(new LandEnemy(BOARD_WIDTH / 2, BOARD_HEIGHT - 2, Direction.randomDiagonal()));

        // When a new LandEnemy will be spawned
        timeout = LAND_ENEMY_TIMEOUT;
    }

    @Override
    public void handleEvent(Event event) {
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
            case KEY_BACK:
                pause();
            default:
                break;
        }
    }

    @Override
    public void update() {
        if (--delay > 0)
            return;

        // Cleanup after dying or completing a level
        if (died) {
            board.clean();
            player = new Player(BOARD_WIDTH / 2, 0);
            resetLandEnemies();
            died = false;
        } else if (levelCompleted) {
            initialize();
            levelCompleted = false;
        }

        try {
            movePlayer();
            moveEnemies();

            // Have we completed the level?
            if (board.getFillRatio() >= BOARD_FILL_THRESHOLD) {
                game.playSound(LEVEL_COMPLETE);
                level++;

                levelCompleted = true;
                delay = 50;
            }
        } catch (Collision e) {
            if (--lives == 0) {
                game.playSound(GAME_OVER);
                game.pushState(new GameOverState(game, this));
            }
            else {
                game.playSound(DEATH);
            }

            died = true;
            delay = 50;
        }

        // Update time counter
        if (--timeout <= 0) {
            game.playSound(DANGER);
            landEnemies.add(new LandEnemy(BOARD_WIDTH / 2, BOARD_HEIGHT - 2, Direction.randomDiagonal()));
            timeout = LAND_ENEMY_TIMEOUT;
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
                score += board.fill(seaEnemies);
            }
        }

        player.setPosition(nextPosition);
    }

    private void moveEnemies() throws Collision {
        for (Enemy enemy : landEnemies) {
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

        for (Enemy enemy : seaEnemies) {
            // Sea enemies collide with in-progress SAND walls
            if (board.getField(enemy.getNextPosition()) == Board.Field.SAND)
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
    public void render(Renderer renderer) {
        // Single field size is 4px
        final int FIELD_SIZE = 4;

        // Draw the board
        int y = 0;
        for (Board.Field[] row : board.getFields()) {
            int x = 0;
            for (Board.Field f : row) {
                if (f != Board.Field.SEA)
                    renderer.fillRect(x, y, FIELD_SIZE, FIELD_SIZE, colorForField(f));
                x += FIELD_SIZE;
            }
            y += FIELD_SIZE;
        }

        // Draw the player
        Position pos = player.getPosition();
        int spriteId = board.getField(pos) == Board.Field.LAND ? SPRITE_PLAYER_LAND : SPRITE_PLAYER_SEA;
        renderer.drawSprite(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, spriteId);

        // Draw the enemies
        for (Enemy enemy : landEnemies) {
            pos = enemy.getPosition();
            renderer.drawSprite(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, SPRITE_LAND_ENEMY);
        }

        for (Enemy enemy : seaEnemies) {
            pos = enemy.getPosition();
            renderer.drawSprite(pos.x * FIELD_SIZE, pos.y * FIELD_SIZE, SPRITE_SEA_ENEMY);
        }

        // Bottom info
        renderer.drawString(0, 172, "Score: " + score);
        renderer.drawString(112, 172, "Xn: " + lives);
        renderer.drawString(170, 172, "Full: " + ((int) (board.getFillRatio() * 100)) + "%");
        renderer.drawString(250, 172, "Time: " + Math.max(0, timeout / 20));
    }

    private Renderer.Color colorForField(Board.Field f) {
        switch (f) {
            case LAND:  return Renderer.Color.CYAN;
            case SAND:  return Renderer.Color.MAGENTA;
            default:    return null;
        }
    }

    @Override
    public void pause() {
        game.pushState(new PauseState(game, this));
    }

    private class Collision extends Exception {}
}
