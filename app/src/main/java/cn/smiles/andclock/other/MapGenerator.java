package cn.smiles.andclock.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class MapGenerator {
    public RectF map[][];
    private Paint paint;
    public int totalWall;

    public MapGenerator(int screenX, int screenY) {
        init(screenX, screenY);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void init(int screenX, int screenY) {
        int row = 3;
        int col = 6;
        totalWall = row * col;
        int brickWidth = (int) (screenX * 0.8f) / col;
        int brickHeight = (int) (screenX * 0.26f) / row;
        float leftM = screenX * 0.2f / 2;
        float topM = screenY * 0.1f;
        map = new RectF[row][col];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                float left = x * brickWidth + leftM;
                float top = y * brickHeight + topM;
                float right = left + brickWidth;
                float bottom = top + brickHeight;
                map[y][x] = new RectF(left, top, right, bottom);
            }
        }
    }

    public void draw(Canvas canvas) {
        for (RectF[] aMap : map) {
            for (RectF rectF : aMap) {
                if (rectF != null) {
                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(3);
                    canvas.drawRect(rectF, paint);
                }
            }
        }
    }

    public void setBrickValue(int row, int col) {
        map[row][col] = null;
    }

}
