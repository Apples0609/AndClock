package com.dtr.zxing.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.decode.DecodeThread;

import cn.smiles.andclock.R;


public class ResultActivity extends Activity {

    private ImageView mResultImage;
    private TextView mResultText;
    private ClipboardManager clipboard;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Bundle extras = getIntent().getExtras();

        mResultImage = (ImageView) findViewById(R.id.result_image);
        mResultText = (TextView) findViewById(R.id.result_text);
        mResultText.setOnClickListener(v -> {
            if (URLUtil.isValidUrl(result)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, "内容已复制", Toast.LENGTH_SHORT).show();
            }
        });

        if (null != extras) {
            int width = extras.getInt("width");
            int height = extras.getInt("height");

            LayoutParams lps = new LayoutParams(width, height);
            lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

            mResultImage.setLayoutParams(lps);

            result = extras.getString("result");
            mResultText.setText(result);
            copyText(result);

            Bitmap barcode = null;
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                // Mutable copy:
                barcode = barcode.copy(Bitmap.Config.RGB_565, true);
            }

            mResultImage.setImageBitmap(barcode);
        }
    }

    private void copyText(String result) {
        ClipData clip = ClipData.newPlainText("qr_text", result);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
    }
}
