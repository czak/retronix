package pl.czak.retronix.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import pl.czak.retronix.Game;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Screen screen = new Screen(this);
        setContentView(screen);

        Game game = new Game(screen);
        game.start();
    }
}
