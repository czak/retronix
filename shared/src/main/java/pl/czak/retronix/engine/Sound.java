package pl.czak.retronix.engine;

/**
 * Created by czak on 23/04/16.
 */
public enum Sound {
    LEVEL_COMPLETE("sounds/hurra.wav"),
    DEATH("sounds/death.wav"),
    GAME_OVER("sounds/gameover.wav"),
    DANGER("sounds/danger.wav");

    private String filename;

    Sound(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
