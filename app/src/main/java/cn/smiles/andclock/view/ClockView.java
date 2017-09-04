package cn.smiles.andclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class ClockView extends View {

    private Paint paint;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                postDelayed(this, 1000);
            }
        }, 1000 - second);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(500, 500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(250, 250);
        canvas.drawCircle(0, 0, 240, paint);
        paint.setStrokeWidth(10);
        canvas.drawPoint(0, 0, paint);

        paint.setStrokeCap(Paint.Cap.SQUARE);
        //分刻度
        paint.setStrokeWidth(4);
        canvas.save();
        for (int i = 0; i < 60; i++) {
            canvas.drawLine(0, -239, 0, -225, paint);
            canvas.rotate(6);
        }
        canvas.restore();
        //时刻度
        paint.setStrokeWidth(6);
        canvas.save();
        for (int i = 0; i < 12; i++) {
            canvas.drawLine(0, -239, 0, -210, paint);
            canvas.rotate(30);
        }
        canvas.restore();

        paint.setStrokeCap(Paint.Cap.ROUND);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
//        System.out.println("hour= " + hour + ",minute=" + minute + ",second=" + second);
        //时针
        paint.setStrokeWidth(8);
        float[] hourps = calculatePoint(hour * 30, 100);
        canvas.drawLines(hourps, paint);
        //分针
        paint.setStrokeWidth(6);
        float[] minuteps = calculatePoint(minute * 6, 130);
        canvas.drawLines(minuteps, paint);
        //秒针
        paint.setStrokeWidth(4);
        float[] secondps = calculatePoint(second * 6, 160);
        canvas.drawLines(secondps, paint);

        paint.setStrokeWidth(2);
        paint.setTextSize(24);
        paint.setTextAlign(Paint.Align.CENTER);
        for (int n = 1; n <= 12; n++) {
            float[] point = calculatePoint2(30 * n);
            float x = point[0];
            float y = point[1];
            canvas.drawText(String.valueOf(n), x, y, paint);
        }
    }

    private float[] calculatePoint2(float angle) {
        float[] points = new float[2];
        float length = 240 * 0.73f;
        if (angle <= 90f) {
            points[0] = (float) Math.sin(angle * Math.PI / 180) * length;
            points[1] = -(float) Math.cos(angle * Math.PI / 180) * length;
        } else if (angle <= 180f) {
            points[0] = (float) Math.cos((angle - 90) * Math.PI / 180) * length;
            points[1] = (float) Math.sin((angle - 90) * Math.PI / 180) * length;
        } else if (angle <= 270f) {
            points[0] = -(float) Math.sin((angle - 180) * Math.PI / 180) * length;
            points[1] = (float) Math.cos((angle - 180) * Math.PI / 180) * length;
        } else if (angle <= 360f) {
            points[0] = -(float) Math.cos((angle - 270) * Math.PI / 180) * length;
            points[1] = -(float) Math.sin((angle - 270) * Math.PI / 180) * length;
        }
        return points;
    }

    private final int DEFAULT_LEFT_LENGHT = 20;

    /**
     * 根据角度和长度计算线段的起点和终点的坐标
     *
     * @param angle
     * @param length
     * @return
     */
    private float[] calculatePoint(float angle, float length) {
        float[] points = new float[4];
        if (angle <= 90f) {
            points[0] = -(float) Math.sin(angle * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[1] = (float) Math.cos(angle * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[2] = (float) Math.sin(angle * Math.PI / 180) * length;
            points[3] = -(float) Math.cos(angle * Math.PI / 180) * length;
        } else if (angle <= 180f) {
            points[0] = -(float) Math.cos((angle - 90) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[1] = -(float) Math.sin((angle - 90) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[2] = (float) Math.cos((angle - 90) * Math.PI / 180) * length;
            points[3] = (float) Math.sin((angle - 90) * Math.PI / 180) * length;
        } else if (angle <= 270f) {
            points[0] = (float) Math.sin((angle - 180) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[1] = -(float) Math.cos((angle - 180) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[2] = -(float) Math.sin((angle - 180) * Math.PI / 180) * length;
            points[3] = (float) Math.cos((angle - 180) * Math.PI / 180) * length;
        } else if (angle <= 360f) {
            points[0] = (float) Math.cos((angle - 270) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[1] = (float) Math.sin((angle - 270) * Math.PI / 180) * DEFAULT_LEFT_LENGHT;
            points[2] = -(float) Math.cos((angle - 270) * Math.PI / 180) * length;
            points[3] = -(float) Math.sin((angle - 270) * Math.PI / 180) * length;
        }
        return points;
    }

}
