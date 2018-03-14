package cn.smiles.andclock.other;

import android.graphics.RectF;

public class MapWall {
    public RectF wall;
    public boolean iShow;

    public MapWall(RectF wall, boolean iShow) {
        this.wall = wall;
        this.iShow = iShow;
    }

    public MapWall(RectF wall) {
        this(wall, true);
    }

    public MapWall(float left, float top, float right, float bottom) {
        this(new RectF(left, top, right, bottom));
    }
}
