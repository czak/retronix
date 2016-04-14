package pl.czak.retronix.android;

import pl.czak.retronix.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by czak on 14/04/16.
 */
public class Screen extends SurfaceView implements GameRenderer, SurfaceHolder.Callback {
    private static final String TAG = "Screen";

    private SurfaceHolder holder;
    private boolean ready;

    public Screen(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void render(Board board) {
        if (!ready) return;

        // Single field size
        final float FIELD_SIZE = (float) Math.min((double) getWidth() / board.getWidth(),
                (double) getHeight() / board.getHeight());

        // Offset to center in window
        final float TX = ((float) getWidth() - FIELD_SIZE * board.getWidth()) / 2;
        final float TY = ((float) getHeight() - FIELD_SIZE * board.getHeight()) / 2;

        Canvas canvas = holder.lockCanvas();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        RectF rect = new RectF(TX, TY, TX + FIELD_SIZE, TY + FIELD_SIZE);

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

        holder.unlockCanvasAndPost(canvas);
    }

    private int colorForField(Board.Field f) {
        switch (f) {
            case LAND:  return Color.GREEN;
            case SEA:   return Color.BLUE;
            case SAND:  return Color.YELLOW;
            default:    return 0;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ready = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
