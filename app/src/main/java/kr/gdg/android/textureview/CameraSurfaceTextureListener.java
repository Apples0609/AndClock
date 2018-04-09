package kr.gdg.android.textureview;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

public class CameraSurfaceTextureListener implements
        TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private Activity mActivity;
    private CameraInfo mBackCameraInfo;

    public CameraSurfaceTextureListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                            int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureAvailable(
            SurfaceTexture surface,
            int width, int height) {
        Log.d("!!!!", "onSurfaceTextureAvailable!!!");
        Pair<CameraInfo, Integer> backCamera = getBackCamera();
        final int backCameraId = backCamera.second;
        mBackCameraInfo = backCamera.first;
        mCamera = Camera.open(backCameraId);
        cameraDisplayRotation();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    public void cameraDisplayRotation() {
        final int rotation = mActivity.getWindowManager()
                .getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        final int displayOrientation = (mBackCameraInfo.orientation
                - degrees + 360) % 360;
        mCamera.setDisplayOrientation(displayOrientation);
    }

    private Pair<CameraInfo, Integer> getBackCamera() {
        CameraInfo cameraInfo = new CameraInfo();
        final int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                return new Pair<CameraInfo, Integer>(cameraInfo,
                        Integer.valueOf(i));
            }
        }
        return null;
    }

    public boolean isCameraOpen() {
        return mCamera != null;
    }
}
