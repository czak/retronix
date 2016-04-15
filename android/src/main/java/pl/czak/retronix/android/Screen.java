package pl.czak.retronix.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import pl.czak.retronix.GameRenderer;
import pl.czak.retronix.engine.Canvas;
import pl.czak.retronix.engine.GameState;


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
    public void render(GameState state) {
        if (!ready) return;

        final android.graphics.Canvas canvas = holder.lockCanvas();

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        state.render(new Canvas() {
            @Override
            public void fillRect(double x, double y, double width, double height, Color color) {
                RectF rect = new RectF((float) x, (float) y, (float) (x + width), (float) (y + height));
                paint.setColor(getSystemColor(color));
                canvas.drawRect(rect, paint);
            }

            @Override
            public int getWidth() {
                return Screen.this.getWidth();
            }

            @Override
            public int getHeight() {
                return Screen.this.getHeight();
            }
        });

        holder.unlockCanvasAndPost(canvas);
    }

    private int getSystemColor(Canvas.Color color) {
        switch (color) {
            case RED:       return Color.RED;
            case GREEN:     return Color.GREEN;
            case BLUE:      return Color.BLUE;
            case YELLOW:    return Color.YELLOW;
            case MAGENTA:   return Color.MAGENTA;
            default:        return 0;
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
