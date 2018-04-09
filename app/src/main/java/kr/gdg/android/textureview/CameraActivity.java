
package kr.gdg.android.textureview;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;

import cn.smiles.andclock.R;

public class CameraActivity extends AppCompatActivity {
    public static String TAG = "CameraActivity";

    private CameraSurfaceTextureListener mCameraSurfaceTextureListener;
    private OrientationEventListener mOrientationEventListener;
    private TextureView mTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textureview_demo_content);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mCameraSurfaceTextureListener = new CameraSurfaceTextureListener(
                this);

        mTextureView = (TextureView) findViewById(R.id.texture_view);
        mTextureView
                .setSurfaceTextureListener(mCameraSurfaceTextureListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.textureview_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.increase_alpha:
                mTextureView.setAlpha(mTextureView.getAlpha() + 0.1f);
                return true;
            case R.id.decrease_alpha:
                mTextureView.setAlpha(mTextureView.getAlpha() - 0.1f);
                return true;
            case R.id.rotate_left:
                mTextureView.setRotation(mTextureView.getRotation() - 5f);
                return true;
            case R.id.rotate_right:
                mTextureView.setRotation(mTextureView.getRotation() + 5f);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(this,
                    SensorManager.SENSOR_DELAY_NORMAL) {
                private int mOrientation;

                @Override
                public void onOrientationChanged(int orientation) {
                    System.out.println("orientation= " + orientation);
                    int lastOrientation = mOrientation;

                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != Surface.ROTATION_0) {
                            mOrientation = Surface.ROTATION_0;
                        }
                    } else if (orientation >= 45 && orientation < 135) {
                        if (mOrientation != Surface.ROTATION_90) {
                            mOrientation = Surface.ROTATION_90;
                        }
                    } else if (orientation >= 135 && orientation < 225) {
                        if (mOrientation != Surface.ROTATION_180) {
                            mOrientation = Surface.ROTATION_180;
                        }
                    } else if (mOrientation != Surface.ROTATION_270) {
                        mOrientation = Surface.ROTATION_270;
                    }

                    if (lastOrientation != mOrientation) {
                        Log.d("!!!!", "rotation!!! lastOrientation:"
                                + lastOrientation + " mOrientation:"
                                + mOrientation + " orientaion:"
                                + orientation);
                    }
                }
            };
        }

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationEventListener.disable();
    }
}

