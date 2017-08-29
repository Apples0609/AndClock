package cn.smiles.andclock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

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

        paint.setStrokeWidth(8);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //时针
        canvas.drawLine(0, 0, 0, -120, paint);
        //分针
        canvas.drawLine(0, 0, 140, 0, paint);
        //秒针
        canvas.drawLine(0, 0, 0, 160, paint);
    }
}
