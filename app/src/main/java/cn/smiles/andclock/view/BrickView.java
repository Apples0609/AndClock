package cn.smiles.andclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import cn.smiles.andclock.other.MapGenerator;
import cn.smiles.andclock.other.MapWall;
import cn.smiles.andclock.other.Star;


public class BrickView extends SurfaceView implements Runnable {

    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    //the game thread
    private Thread gameThread = null;

    //These objects will be used for drawing
    private Paint paint;
    private SurfaceHolder surfaceHolder;

    //Adding an stars list
    private ArrayList<Star> stars = new ArrayList<>();
    private int screenX;
    private int screenY;
    private RectF rectBar;

    float cx, cy, radius;
    private float barWidth;
    private int xdir = -3;
    private int ydir = -4;
    private RectF rectBall;
    private MapGenerator map;
    private int score;
    private float downX, downY;

    public BrickView(Context context) {
        super(context);
        init();
    }

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setAntiAlias(true);

        screenX = getContext().getResources().getDisplayMetrics().widthPixels;
        screenY = getContext().getResources().getDisplayMetrics().heightPixels;
        //adding 100 stars you may increase the number
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }
        barWidth = screenX * 0.7f - screenX * 0.3f;
        rectBar = new RectF(screenX * 0.3f, screenY * 0.93f, screenX * 0.3f + barWidth, screenY * 0.93f + 12);
        cx = screenX * 0.4f;
        cy = screenY * 0.7f;
        radius = 46;
        rectBall = new RectF(cx, cy, cx + radius, cy + radius);
        map = new MapGenerator(screenX, screenY);
    }

    @Override
    public void run() {
        while (playing) {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }

    private void update() {
        //Updating the stars with player speed
        for (Star s : stars) {
            s.update();
        }

        //更新球位置
        if (rectBall.intersect(rectBar)) {
            ydir = -ydir;
        }
        if (cx <= 0) {
            xdir = -xdir;
        }
        if (cy <= 0) {
            ydir = -ydir;
        }
        if (cx >= screenX - radius) {
            xdir = -xdir;
        }
        if (map.totalWall <= 0 && cy > screenY - radius) {
            ydir = -ydir;
        }
        cx += xdir;
        cy += ydir;
        rectBall.left = cx;
        rectBall.top = cy;
        rectBall.right = cx + radius;
        rectBall.bottom = cy + radius;

        float left = map.firstWall().wall.left;
        float top = map.firstWall().wall.top;
        float right = map.lastWall().wall.right;
        float bottom = map.lastWall().wall.bottom;
        RectF bigWall = new RectF(left, top, right, bottom);
//        if (rectBall.intersect(bigWall)) {
            outer:
            for (int y = 0; y < map.map.length; y++) {
                for (int x = 0; x < map.map[0].length; x++) {
                    MapWall wall = map.map[y][x];
                    if (wall.iShow) {
                        if (rectBall.intersect(wall.wall)) {
                            if (rectBall.left <= wall.wall.left || rectBall.left >= wall.wall.right) {
                                xdir = -xdir;
                            } else {
                                ydir = -ydir;
                            }
                            map.setBrickValue(y, x);
                            map.totalWall--;
                            score += 5;
                            break outer;
                        }
                    }
                }
            }
//        }

    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);
            //drawing all stars
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            // drawing map
            map.draw(canvas);

            //画接球条
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(rectBar, 8, 8, paint);

            //画球
            paint.setColor(Color.YELLOW);
            canvas.drawRoundRect(rectBall, radius, radius, paint);

            // scores
            paint.setColor(Color.GREEN);
            paint.setTextSize(42);
            canvas.drawText(String.valueOf(score), screenX - 60, 60, paint);

            if (map.totalWall <= 0) {
                paint.setColor(Color.RED);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(80);
                canvas.drawText("你赢了！", screenX / 2, screenY / 2 - 60, paint);
            }
            if (cy > screenY) {
                paint.setColor(Color.RED);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(80);
                canvas.drawText("你输了！", screenX / 2, screenY / 2 - 60, paint);
                playing = false;
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //we will do something here

                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //do something here
                downX = motionEvent.getX();
                downY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = motionEvent.getX();
                float moveY = motionEvent.getY();
                RectF temp = new RectF(rectBar);
                float diffX = (moveX - downX) * 0.51f;
                temp.left = rectBar.left + diffX;
                temp.right = rectBar.right + diffX;
                if (temp.left < 0) {
                    temp.left = 0;
                    temp.right = barWidth;
                }
                if (temp.right > screenX) {
                    temp.left = screenX - barWidth;
                    temp.right = screenX;
                }
                rectBar.left = temp.left;
                rectBar.right = temp.right;
                downX = moveX;
                downY = moveY;
                break;
        }
        return true;
    }
}
