package com.example.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DemoView extends View {
    public static final int NB = 14;
    private Paint black, yellow, white;
    private ArrayList<Key> whites = new ArrayList<>();
    private ArrayList<Key> blacks = new ArrayList<>();
    private int keyWidth, height;
    //    private AudioSoundPlayer soundPlayer;
    private DemoSoundManager soundManager;


    public DemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        black = new Paint();
        black.setColor(Color.BLACK);
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        yellow = new Paint();
        yellow.setColor(Color.GRAY);
        yellow.setStyle(Paint.Style.FILL);

        soundManager = DemoSoundManager.getInstance();
        soundManager.init(context);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        keyWidth = w / NB;
        height = h;
        int count = 15;
        for (int i = 0; i < NB; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            if (i == NB - 1) {
                right = w;
            }

            RectF rect = new RectF(left, 0, right, h);
            whites.add(new Key(i+1, rect));

            if (i != 0  &&   i != 11  &&  i != 7  &&  i != 4) {
                rect = new RectF((float) (i - 1) * keyWidth + 0.5f * keyWidth + 0.25f * keyWidth, 0.33f * height,
                        (float) i * keyWidth + 0.25f * keyWidth, height);
                blacks.add(new Key(count, rect));
                count++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Key k : whites) {
            canvas.drawRect(k.rect, k.down ? yellow : white);
        }

        for (int i = 1; i < NB; i++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, height, black);
        }

        for (Key k : blacks) {
            canvas.drawRect(k.rect, k.down ? yellow : black);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            Key k = keyForCoords(x,y);

            if (k != null) {
                k.down = isDownAction;
            }
        }

        ArrayList<Key> tmp = new ArrayList<>(whites);
        tmp.addAll(blacks);


        for (Key k : tmp) {
            if (k.down) {
                switch (k.sound) {
                    case 1:
                        soundManager.playSound(R.raw.b3);
                        break;
                    case 2:
                        soundManager.playSound(R.raw.a3);
                        break;
                    case 3:
                        soundManager.playSound(R.raw.g3);
                        break;
                    case 4:
                        soundManager.playSound(R.raw.f3);
                        break;
                    case 5:
                        soundManager.playSound(R.raw.e3);
                        break;
                    case 6:
                        soundManager.playSound(R.raw.d3);
                        break;
                    case 7:
                        soundManager.playSound(R.raw.c3);
                        break;
                    case 8:
                        soundManager.playSound(R.raw.b2);
                        break;
                    case 9:
                        soundManager.playSound(R.raw.a2);
                        break;
                    case 10:
                        soundManager.playSound(R.raw.g2);
                        break;
                    case 11:
                        soundManager.playSound(R.raw.f2);
                        break;
                    case 12:
                        soundManager.playSound(R.raw.e2);
                        break;
                    case 13:
                        soundManager.playSound(R.raw.d2);
                        break;
                    case 14:
                        soundManager.playSound(R.raw.c2);
                        break;
                    case 15:
                        soundManager.playSound(R.raw.bb3);
                        break;
                    case 16:
                        soundManager.playSound(R.raw.ab3);
                        break;
                    case 17:
                        soundManager.playSound(R.raw.gb3);
                        break;
                    case 18:
                        soundManager.playSound(R.raw.eb3);
                        break;
                    case 19:
                        soundManager.playSound(R.raw.db3);
                        break;
                    case 20:
                        soundManager.playSound(R.raw.bb2);
                        break;
                    case 21:
                        soundManager.playSound(R.raw.ab2);
                        break;
                    case 22:
                        soundManager.playSound(R.raw.gb2);
                        break;
                    case 23:
                        soundManager.playSound(R.raw.eb2);
                        break;
                    case 24:
                        soundManager.playSound(R.raw.db2);
                        break;
                }
                invalidate();
            }
        }
        return true;
    }

    private Key keyForCoords(float x, float y) {
        for (Key k : blacks) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        for (Key k : whites) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        return null;
    }

    private void releaseKey(final Key k) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k.down = false;
                handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

}
