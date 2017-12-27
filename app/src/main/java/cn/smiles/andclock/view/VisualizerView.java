package cn.smiles.andclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerView extends View {

    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(1f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.RED);
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null)
            return;
        if (mPoints == null || mPoints.length < mBytes.length * 4)
            mPoints = new float[mBytes.length * 4];
        mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * 4 + 1] = mRect.height() / 2
                    + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * 4 + 3] = mRect.height() / 2
                    + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
        }
        canvas.drawLines(mPoints, mForePaint);

        //绘制宽柱状图
        /*int width = mRect.width();
        int rwidth = width / (20 + 3);
        int step = mBytes.length / 30;
        for (int i = 0; i < mBytes.length - 2; i += step) {
            float left = width * i / (mBytes.length - 1);
            float top = mRect.height() - ((byte) (mBytes[i + 1] + 128)) * mRect.height() / 64 - 20;
            float right = left + 12;
            float bottom = mRect.height();
            System.out.println("step= " + step + ",left= " + left + ",top= " + top + ",right= " + right + ",bottom= " + bottom);
            canvas.drawRect(left, top, right, bottom, mForePaint);
        }*/


    }

}
