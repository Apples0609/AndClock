package cn.smiles.andclock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

import cn.smiles.andclock.R;

public class MGridView extends GridView {
    private boolean expanded;

    public MGridView(Context context) {
        this(context, null);
    }

    public MGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MGridView,
                defStyleAttr, 0);
        try {
            expanded = a.getBoolean(R.styleable.MGridView_inList, false);
        } finally {
            a.recycle();
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (expanded) {
            // Calculate entire height by providing a very large height hint.
            // MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
