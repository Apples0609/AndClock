package cn.smiles.andclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;

import cn.smiles.andclock.tools.Injector;

public class HomeService extends Service {

    private final String TAG = "===";
    private WindowManager windowManager;
    private Button wmContentView;
    private SharedPreferences asp;
    private int wmParamsX;
    private int wmParamsY;
    private int swh = 100;


    public HomeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        asp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isChecked = intent.getBooleanExtra("isChecked", true);
        boolean b = wmContentView == null;
        if (isChecked == !b)
            return START_STICKY;
        if (b) {
            openAndroidWindow();
        } else {
            closeWindowView();
        }
        asp.edit().putBoolean("isChecked", b).apply();
        return START_STICKY;
    }

    /**
     * 开启窗口
     */
    private void openAndroidWindow() {
        wmParamsX = asp.getInt("wmParamsX", 0);
        wmParamsY = asp.getInt("wmParamsY", 0);
        wmContentView = new Button(getApplicationContext());
        wmContentView.setBackgroundResource(R.drawable.ic_android_black_normal);
        final WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(-2, -2);
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.width = swh;
        wmParams.height = swh;
        wmParams.x = wmParamsX;
        wmParams.y = wmParamsY;
        wmContentView.setOnTouchListener(new View.OnTouchListener() {
            int downX, downY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "ACTION_DOWN");
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        paramX = wmParams.x;
                        paramY = wmParams.y;
                        v.setBackgroundResource(R.drawable.ic_android_black_pressed);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "ACTION_MOVE");
                        int dx = (int) event.getRawX() - downX;
                        int dy = (int) event.getRawY() - downY;
                        if (Math.abs(dx) > 3 || Math.abs(dy) > 3) {
                            wmParams.x = paramX + dx;
                            wmParams.y = paramY + dy;
                            windowManager.updateViewLayout(v, wmParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        Log.i(TAG, "ACTION_UP");
                        v.setBackgroundResource(R.drawable.ic_android_black_normal);
                        int ux = (int) event.getRawX();
                        int uy = (int) event.getRawY();
                        if (Math.abs(ux - downX) < 3 && Math.abs(uy - downY) < 3) {
                            v.performClick();
                        }
                        asp.edit().putInt("wmParamsX", wmParams.x).apply();
                        asp.edit().putInt("wmParamsY", wmParams.y).apply();
                        break;
                }
                return true;
            }
        });
        wmContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContentWindow();
                closeWindowView();
            }
        });
        windowManager.addView(wmContentView, wmParams);
    }

    /**
     * 开启操作窗口
     */
    private void openContentWindow() {
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tools_view_layout, null);
        final WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(-1, -1);
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;
        view.findViewById(R.id.go_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                closeContentWindow(view);
            }
        });
        view.findViewById(R.id.go_home).setOnLongClickListener(longClickListener);
        view.findViewById(R.id.recent_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class<?> serviceManagerClass = Class.forName("android.os.ServiceManager");
                    Method getService = serviceManagerClass.getMethod("getService", String.class);
                    IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
                    Class<?> statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
                    Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[]{retbinder});
                    Method clearAll = statusBarClass.getMethod("toggleRecentApps");
                    clearAll.setAccessible(true);
                    clearAll.invoke(statusBarObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeContentWindow(view);
            }
        });
        view.findViewById(R.id.recent_app).setOnLongClickListener(longClickListener);
        view.findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Injector.pressBackButton();
                closeContentWindow(view);
            }
        });
        view.findViewById(R.id.go_back).setOnLongClickListener(longClickListener);
        view.findViewById(R.id.open_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Injector.showNotificationCenter();
                closeContentWindow(view);
            }
        });
        view.findViewById(R.id.open_notification).setOnLongClickListener(longClickListener);
        view.findViewById(R.id.power_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Injector.pressPowerButton();
                closeContentWindow(view);
            }
        });
        view.findViewById(R.id.power_off).setOnLongClickListener(longClickListener);
        view.findViewById(R.id.open_android_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
                closeContentWindow(view);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeContentWindow(view);
            }
        });
        windowManager.addView(view, wmParams);
    }

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(HomeService.this, v.getContentDescription(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    /**
     * 关闭操作窗口
     *
     * @param view
     */
    private void closeContentWindow(View view) {
        windowManager.removeView(view);
        openAndroidWindow();
    }

    /**
     * 关闭
     */
    private void closeWindowView() {
        if (wmContentView != null) {
            windowManager.removeView(wmContentView);
            wmContentView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
