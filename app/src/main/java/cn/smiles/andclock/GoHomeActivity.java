package cn.smiles.andclock;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import cn.smiles.andclock.service.AndroidService;
import cn.smiles.andclock.service.RemoteService;

public class GoHomeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private final String TAG = "===";
    private int statusHeight = 25;
    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_home);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
        switch1.setChecked(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("isChecked", false));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_app_setting:
                showInstalledAppDetails(getPackageName());
                break;
            case R.id.button4:
                File file = new File("/storage");
                File[] files = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.startsWith("sdcard");
                    }
                });
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (File f : files) {
                    try {
                        new File(f, "ss.doc").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(f.getAbsolutePath());
                }
                break;
        }
    }

    public void showInstalledAppDetails(String packageName) {
        final int apiLevel = Build.VERSION.SDK_INT;
        Intent intent = new Intent();

        if (apiLevel >= 9) {
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
        } else {
            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");

            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }

        // Start Activity
        startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent intent = new Intent(this.getApplicationContext(), AndroidService.class);
        intent.putExtra("isChecked", isChecked);
        startService(intent);
        startService(new Intent(getApplicationContext(), RemoteService.class));
    }

    private void getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        moveTaskToBack(true);
    }
}
