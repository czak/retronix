package pl.czak.retronix.android;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import pl.czak.retronix.engine.State;


/**
 * Created by czak on 14/04/16.
 */
public class Screen extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private boolean ready;

    public Screen(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    public void draw(State state) {
        if (!ready) return;

        Canvas canvas = holder.lockCanvas();
        state.render(canvas);
        holder.unlockCanvasAndPost(canvas);
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
