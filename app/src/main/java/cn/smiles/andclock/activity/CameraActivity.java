package cn.smiles.andclock.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback {


    private static final int CAMERA_FACING_FRONT = 1; //前置摄像头
    private static final int CAMERA_FACING_BACK = 0; //后置摄像头
    private static String TAG = "CAMERA_ACTIVITY";
    @BindView(R.id.tvPhotoNumber)
    TextView tvPhotoNumber;
    @BindView(R.id.btnSavePhoto)
    Button btnSavePhoto;
    @BindView(R.id.btnNoSavePhoto)
    Button btnNoSavePhoto;
    @BindView(R.id.btnStopPhoto)
    Button btnStopPhoto;
    @BindView(R.id.count_down)
    TextView countDown;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    Boolean isContinue = false;
    private boolean isSurfaceCreated = false;
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private Camera.Parameters cameraParameters;
    private Camera.Size bestPreviewSize;
    private int widthPixels;
    private int heightPixels;
    private Handler mHandler = new Handler();
    private int mCurrentTimer = 3;  //当前倒计时3秒
    private int numberOfCameras;
    private Camera.Size bestSavePreviewSize;
    private int count;


    /*循环拍照倒计时*/
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (camera == null)
                return;

            if (!isContinue)
                return;
            if (mCurrentTimer > 0) {   //当前时间大于0时
                countDown.setText(mCurrentTimer + "");  //设置拍照倒计时显示
                mCurrentTimer--;   //倒计时减一
                mHandler.postDelayed(this, 1000); //延时设置为1s
            } else {  //拍照倒计时小于0时
                countDown.setText("");  //设置拍照延时显示为null
                camera.takePicture(null, null, null, CameraActivity.this);  //进行拍照
                count++;
                playSound();  //播放拍照声音
                tvPhotoNumber.setText("" + count);
                mCurrentTimer = 3;  //拍照延时
                mHandler.post(this);//倒计时本就有3秒了
            }

        }
    };
    private Boolean isSavePicture = false;
    private PowerManager pm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*// 掩藏窗口标题  manifest中已执行
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        // 掩藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        //获取摄像头的个数
        numberOfCameras = camera.getNumberOfCameras();
        Log.d("CameraNumber", " numberOfCameras = " + numberOfCameras);


        //初始化事件
        initEvent();
        // 就一行代码,设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.d("CameraActivity", "onCreate========== =========");
    }


    @Override
    protected void onStart() {
        super.onStart();
//        StartPreview();
//        mHandler.post(timerRunnable);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CameraActivity", "onStop========== StopPreview =========");

    }


    /*息屏会执行onPause停止预览*/
    @Override
    protected void onPause() {
        super.onPause();

        try {
            isContinue = false;
            StopPreview();
            boolean isScreenOn = pm.isScreenOn();
            if (!isScreenOn) {//如果直接按电源键，退出拍照算了
                finish();
                return;
            }

            countDown.setText("");
            mCurrentTimer = 3;
            btnStopPhoto.setEnabled(true);
            btnNoSavePhoto.setEnabled(true);
            btnSavePhoto.setEnabled(true);
//        camera.release();
            Log.d("CameraActivity", "onPause========== StopPreview =========");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("zhikai", e.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        surfaceHolder.addCallback(this);
        StartPreview(); //开始预览
        isContinue = true;
//        mHandler.post(timerRunnable);
        Log.d("CameraActivity", "onResume========== StartPreview =========");
        //保持屏幕常亮
//        Settings.System.putInt(getContentResolver(),android.provider.Settings.System.SCREEN_OFF_TIMEOUT,-1);

    }

    /*初始化事件*/
    private void initEvent() {

        try {
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            btnSavePhoto.setOnClickListener(this);
            btnNoSavePhoto.setOnClickListener(this);
            btnStopPhoto.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(CameraActivity.this)
                    .setTitle("提示").setMessage("摄像头打开失败或没有摄像头")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        initUI();
    }

    /*UI初始化操作*/
    private void initUI() {
        btnSavePhoto.setEnabled(true);
        btnDelete.setEnabled(true);
        btnStopPhoto.setEnabled(true);
        btnNoSavePhoto.setEnabled(true);
        tvPhotoNumber.setText(" 10000");
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    /*SurfaceHolder.Callback实现方法*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceCreated = true;
        StartPreview();
        Log.d("CameraActivity", "surfaceCreated==========  surfaceCreated =========");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //camera.setDisplayOrientation(90);

//        StopPreview();
//        StartPreview();

        Log.d("CameraActivity", "surfaceChanged==========  surfaceChanged =========");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreated = false;
        Log.d("CameraActivity", "surfaceDestroyed==========  surfaceDestroyed =========");
    }

    /*View.OnClickListener实现方法*/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSavePhoto) {
            //存图拍照
            isContinue = true;
            isSavePicture = true;
            //设置按钮状态
            btnSavePhoto.setEnabled(false);
            btnStopPhoto.setEnabled(true);
            btnNoSavePhoto.setEnabled(false);
            btnDelete.setEnabled(false);
            count = 0;
            mHandler.post(timerRunnable);

        } else if (v.getId() == R.id.btnNoSavePhoto) {
            //不保存图片的拍照
            //设置一个标志
            isSavePicture = false;
            btnSavePhoto.setEnabled(false);
            btnDelete.setEnabled(false);
            btnNoSavePhoto.setEnabled(false);
            btnStopPhoto.setEnabled(true);
            isContinue = true;
            count = 0;
            mHandler.post(timerRunnable);

        } else if (v.getId() == R.id.btnStopPhoto) {
            //停止拍照
            countDown.setText("");
            isContinue = false;
           /* //mHandler 还有个 remove方法
            mHandler.removeCallbacks(timerRunnable);//用这个也可以停止，配合boolean标志 更保险*/
            mCurrentTimer = 3;
            btnNoSavePhoto.setEnabled(true);
            btnSavePhoto.setEnabled(true);
            btnDelete.setEnabled(true);
            btnStopPhoto.setEnabled(false);
        } else if (v.getId() == R.id.btnDelete) {
            //删除照片
            btnDelete.setEnabled(false);
            btnSavePhoto.setEnabled(true);
            btnStopPhoto.setEnabled(true);
            btnNoSavePhoto.setEnabled(true);
            tvPhotoNumber.setText("10000");


            File secondFolder = new File(Environment.getExternalStorageDirectory(), rootFolder);
            deletePictures(secondFolder);

        }

    }

    String rootFolder = "ac360";

    /*开始预览的方法*/
    private void StartPreview() {

        Log.d("CameraActivity", "StartPreview==========  StartPreview =========");
        if (camera != null || !isSurfaceCreated) {
            Log.d(TAG, "startPreview will return");
            return;
        }

        try {
            //打开后摄像头赋值给camera
            camera = Camera.open(CAMERA_FACING_BACK);
            Camera.Parameters parameters = setCameraDisplayOrientation(CameraActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            camera.setParameters(parameters);
            //获取camera 的参数
            cameraParameters = camera.getParameters();
            //获取长宽尺寸
            widthPixels = getResources().getDisplayMetrics().widthPixels;
            heightPixels = getResources().getDisplayMetrics().heightPixels;
            bestPreviewSize = getBestPreviewSize(widthPixels, heightPixels, cameraParameters);
            bestSavePreviewSize = getBestSavePreviewSize(widthPixels, heightPixels, cameraParameters);
            Log.e(TAG, "bestPreviewSize.width= " + bestPreviewSize.width + "，bestPreviewSize.height= " + bestPreviewSize.height);
            if (bestPreviewSize != null) {
                //设置预览分辨率//这里设置必须是预定义的，只能是那些
                cameraParameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
                //设置保存图片大小
                cameraParameters.setPictureSize(bestSavePreviewSize.width, bestSavePreviewSize.height);
            }
            //自动对焦
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            cameraParameters.setPreviewFrameRate(20);

            //设置相机预览方向，默认为水平
            camera.setDisplayOrientation(90);
            camera.setParameters(cameraParameters);


            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        camera.startPreview();

    }


    public Camera.Parameters setCameraDisplayOrientation(CameraActivity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Log.d("setDisplayOrientation", "1：" + cameraId);
        Camera.getCameraInfo(cameraId, info);
        Log.d("setDisplayOrientation", "1：" + cameraId);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        Log.d("setDisplayOrientation", "1：" + String.valueOf(degrees));
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
        Log.d("setDisplayOrientation", "2：" + String.valueOf(degrees));
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        Log.d("setDisplayOrientation", "orientation：" + String.valueOf(info.facing));
        Log.d("setDisplayOrientation", "facing：" + String.valueOf(info.facing));
        camera.setDisplayOrientation(result);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);  //开启闪光灯
        parameters.setRotation(result);

        //获取设备型号
        String model = android.os.Build.MODEL;
//            Toast.makeText(TakePictureActivity.this, "设备型号为：" + model, Toast.LENGTH_LONG).show();

        Log.d("setDisplayOrientation", "3：" + String.valueOf(result));
        return parameters;
    }

    /*Camera.PictureCallback 实现方法*/
    //拍照保存图片的的方法
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            if (isSavePicture) {
               /*创建文件名*/
                String pictureName = System.currentTimeMillis() + ".png";
                //创建文件对象，并指定文件存储路径
                File storageFolder = new File(Environment.getExternalStorageDirectory(), rootFolder);
                if (!storageFolder.exists()) {
                    storageFolder.mkdirs();
                }
                File curentFile = new File(storageFolder, pictureName);
                FileOutputStream fileOutputStream = new FileOutputStream(curentFile);
                //旋转角度，保证保存的图片方向是对的
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }

    /*结束预览的方法*/
    private void StopPreview() {
        Log.d("CameraActivity", "StopPreview==========  StopPreview =========");
        //释放Camera对象
        if (camera != null) {
            try {
                mHandler.removeCallbacks(timerRunnable);
                camera.setPreviewDisplay(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    /*获取最佳保存图片参数*/
    private Camera.Size getBestSavePreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            Log.e(TAG, "Savewidth= " + size.width + "，Saveheight= " + size.height);
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

    /*获取最佳预览参数*/
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            Log.e(TAG, "width= " + size.width + "，height= " + size.height);
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

    /*播放系统声音*/
    public void playSound() {
        MediaPlayer mediaPlayer = null;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

        if (volume != 0) {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(this,
                        Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        finish();
//        camera.release();
        Log.d("CameraActivity", "onDestroy==========  StopPreview =========");

    }


    /*删除照片的方法*/
    public void deletePictures(File file) {//删除有可能上千张图片，删除慢 最好在线程删 界面显示个等待
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] children = file.listFiles();//ffile.list 有个获取file数组的方法
                if (children == null) {
                    new AlertDialog.Builder(CameraActivity.this).setTitle("该路径下文件为空").setPositiveButton("确认", null)
                            .show();
                } else {
                  /*  new AlertDialog.Builder(CameraActivity.this).setTitle("删除照片").setMessage("正在删除照片，请稍后。。。")
                            .show();*/
                    for (File child : children) {
                        child.delete();
                    }
                }

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(CameraActivity.this).setTitle("提示：").setMessage("确定要退出摄像头测试？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isContinue = false;
//                    StopPreview();
                    //mHandler 还有个 remove方法
//                    mHandler.removeCallbacks(timerRunnable);//用这个也可以停止，配合boolean标志 更保险
//                    camera.release();
                    finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
