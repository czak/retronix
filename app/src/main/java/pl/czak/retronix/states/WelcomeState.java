package pl.czak.retronix.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import pl.czak.retronix.State;

/**
 * Created by czak on 15/04/16.
 */
public class WelcomeState extends State {
    @Override
    public void render(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setAntiAlias(true);

        canvas.drawText("Welcome to Retronix", 0, 100, paint);
    }
}
