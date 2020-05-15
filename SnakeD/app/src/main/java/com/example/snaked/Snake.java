package com.example.snaked;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import static com.example.snaked.SnakeView.EAST;
import static com.example.snaked.SnakeView.LOSE;
import static com.example.snaked.SnakeView.NORTH;
import static com.example.snaked.SnakeView.PAUSE;
import static com.example.snaked.SnakeView.READY;
import static com.example.snaked.SnakeView.RUNNING;
import static com.example.snaked.SnakeView.SOUTH;
import static com.example.snaked.SnakeView.WEST;

public class Snake extends Activity {

    private SnakeView mSnakeView;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private static String ICICLE_KEY = "snake-view";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snake);
        up = (Button) findViewById(R.id.button);

        left = (Button) findViewById(R.id.button2);


        right = (Button) findViewById(R.id.button3);


        down = (Button) findViewById(R.id.button4);

        mSnakeView = (SnakeView) findViewById(R.id.snake);
        mSnakeView.setTextView((TextView) findViewById(R.id.text));

        if (savedInstanceState == null) {
            mSnakeView.setMode(READY);
        } else {
            Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
            if (map != null) {
                mSnakeView.restoreState(map);
            } else {
                mSnakeView.setMode(PAUSE);
            }
        }
    }

    public void buttonClicked(View view) {
        if (view.getId() == R.id.button) {
            if (mSnakeView.mMode == READY | mSnakeView.mMode == LOSE) {
                mSnakeView.initNewGame();
                mSnakeView.setMode(RUNNING);
                mSnakeView.update();
            }
            if (mSnakeView.mMode == PAUSE) {
                mSnakeView.setMode(RUNNING);
                mSnakeView.update();
            }
            if (mSnakeView.mDirection != SOUTH) {
                mSnakeView.mNextDirection = NORTH;
            }
        }
        else if (view.getId() == R.id.button2) {
            if (mSnakeView.mDirection != EAST) {
                mSnakeView.mNextDirection = WEST;
            }
        }
        else if (view.getId() == R.id.button3) {
            if (mSnakeView.mDirection != WEST) {
                mSnakeView.mNextDirection = EAST;
            }
        }
        else if (view.getId() == R.id.button4) {
            if (mSnakeView.mDirection != NORTH) {
                mSnakeView.mNextDirection = SOUTH;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSnakeView.setMode(PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
    }

}


class TileView extends View {

    protected static int mTileSize;

    protected static int mXTileCount;
    protected static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;

    private Bitmap[] mTileArray;

    private int[][] mTileGrid;

    private final Paint mPaint = new Paint();

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TileView);

        mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);

        a.recycle();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TileView);

        mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);

        a.recycle();
    }


    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXTileCount = (int) Math.floor(w / mTileSize);
        mYTileCount = (int) Math.floor(h / mTileSize);

        mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
        mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

        mTileGrid = new int[mXTileCount][mYTileCount];
        clearTiles();
    }


    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }


    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }


    public void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x
                            * mTileSize, mYOffset + y * mTileSize, mPaint);
                }
            }
        }

    }

}

