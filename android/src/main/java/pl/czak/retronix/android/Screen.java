package pl.czak.retronix.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pl.czak.retronix.engine.Renderer;
import pl.czak.retronix.engine.State;

/**
 * Created by czak on 14/04/16.
 */
public class Screen extends SurfaceView implements SurfaceHolder.Callback {
    // Prebuilt Paint objects to use for rendering
    private static final Map<Renderer.Color, Paint> PAINT_MAP = new HashMap<>();
    static {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        PAINT_MAP.put(Renderer.Color.WHITE, p);

        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xff00a8a8);
        PAINT_MAP.put(Renderer.Color.CYAN, p);

        p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xffa800a8);
        PAINT_MAP.put(Renderer.Color.MAGENTA, p);
    }

    private SurfaceHolder holder;
    private boolean ready;

    private Canvas canvas;
    private CanvasRenderer renderer;

    private Bitmap font;

    public Screen(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        holder.setFixedSize(960, 540);

        this.renderer = new CanvasRenderer();

        try {
            InputStream in = getContext().getAssets().open("images/font.png");
            font = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(State state) {
        if (ready && (canvas = holder.lockCanvas()) != null) {
            canvas.scale(3, 3);
            canvas.drawColor(Color.BLACK);
            state.render(renderer);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    // region ----- SurfaceHolder.Callback methods -----
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ready = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ready = false;
    }
    // endregion

    class CanvasRenderer implements Renderer {
        @Override
        public void drawSprite(int x, int y, int spriteId) {
            int sx = (spriteId % 32) * 4;
            int sy = (spriteId / 32) * 4;
            canvas.drawBitmap(font, new Rect(sx, sy, sx+4, sy+4), new Rect(x, y, x+4, y+4), null);
        }

        @Override
        public void drawString(int x, int y, String text) {
            byte[] bytes = new byte[0];
            try {
                bytes = text.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (byte b : bytes) {
                int index = b & 0xff;
                int sx = (index % 16) * 8;
                int sy = (index / 16) * 8;
                canvas.drawBitmap(font, new Rect(sx, sy, sx+8, sy+8), new Rect(x, y, x+8, y+8), null);
                x+=8;
            }
        }

        @Override
        public void fillRect(int x, int y, int width, int height, Color color) {
            canvas.drawRect(x, y, x+width, y+height, PAINT_MAP.get(color));
        }
    }
}
