package cn.smiles.andclock.other;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class MapGenerator {
    public MapWall map[][];
    private Paint paint;
    public int totalWall;
    private int row = 3;
    private int col = 6;

    public MapGenerator(int screenX, int screenY) {
        init(screenX, screenY);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public MapWall firstWall() {
        return map[0][0];
    }

    public MapWall lastWall() {
        return map[row - 1][col - 1];
    }

    private void init(int screenX, int screenY) {
        totalWall = row * col;
        int brickWidth = (int) (screenX * 0.8f) / col;
        int brickHeight = (int) (screenX * 0.26f) / row;
        float leftM = screenX * 0.2f / 2;
        float topM = screenY * 0.1f;
        map = new MapWall[row][col];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                float left = x * brickWidth + leftM;
                float top = y * brickHeight + topM;
                float right = left + brickWidth;
                float bottom = top + brickHeight;
                map[y][x] = new MapWall(left, top, right, bottom);
            }
        }
    }

    public void draw(Canvas canvas) {
        for (MapWall[] aMap : map) {
            for (MapWall wall : aMap) {
                if (wall.iShow) {
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2);
                    canvas.drawRect(wall.wall, paint);

                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    RectF temp = new RectF(wall.wall);
                    temp.left += 2;
                    temp.top += 2;
                    temp.right -= 2;
                    temp.bottom -= 2;
                    canvas.drawRect(temp, paint);
                }
            }
        }
    }

    public void setBrickValue(int row, int col) {
        map[row][col].iShow = false;
    }

}
