package cn.smiles.andclock;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

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
