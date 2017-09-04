package cn.smiles.andclock;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;


public class SmilesApplication extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
    }

    public static void showToast(String m) {
        if (!TextUtils.isEmpty(m)) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Toast.makeText(appContext, m, Toast.LENGTH_SHORT).show();
            } else {
                Looper.prepare();
                Toast.makeText(appContext, m, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }

}
