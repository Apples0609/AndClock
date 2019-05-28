package cn.smiles.andclock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.smiles.andclock.R;


public class LedEffectActivity extends AppCompatActivity {

    private ImageView mImageView4;
    private TextView mTextView25;
    private float screenWidth;
    private HorizontalScrollView mHscroll;
    private int screenHeight;
    private EditText mEditText;
    private Button mBtnOk;
    private LinearLayout mLlayout;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_effect);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        System.out.println(screenHeight);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mImageView4 = findViewById(R.id.imageView4);
        mHscroll = findViewById(R.id.hscroll);
        mTextView25 = findViewById(R.id.textView25);
        mTextView25.setTextSize(screenHeight / 2.5f);

        getTextWidth();

        mLlayout = findViewById(R.id.llayout);
        mEditText = findViewById(R.id.editText);
        mBtnOk = findViewById(R.id.btn_ok);

        mImageView4.setOnClickListener(v -> {
            mEditText.setText(mTextView25.getText());
            mLlayout.setVisibility(View.VISIBLE);
            mEditText.requestFocus();
            imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
        });

        mBtnOk.setOnClickListener(view -> {
            if (mEditText.length() != 0) {
                mTextView25.setText(mEditText.getText());
                getTextWidth();
            }
            mLlayout.setVisibility(View.INVISIBLE);
            mEditText.clearFocus();
        });

    }

    private void getTextWidth() {
        mHscroll.post(() -> {
            int width = mTextView25.getWidth();
            screenWidth = screenWidth > width ? screenWidth : width;
            mTextView25.clearAnimation();
            setTranslation(mTextView25);

        });
    }


    private void setTranslation(TextView text) {
        TranslateAnimation tanim = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 1.0f * screenWidth,
                TranslateAnimation.ABSOLUTE, -1.0f * screenWidth,
                TranslateAnimation.ABSOLUTE, 0.0f,
                TranslateAnimation.ABSOLUTE, 0.0f);
        tanim.setDuration(text.getText().length() * 1650);
        tanim.setInterpolator(new LinearInterpolator());
        tanim.setRepeatCount(Animation.INFINITE);
        tanim.setRepeatMode(Animation.ABSOLUTE);

        text.startAnimation(tanim);
    }

}
