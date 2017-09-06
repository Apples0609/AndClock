package cn.smiles.andclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import cn.smiles.andclock.service.AndroidService;
import cn.smiles.andclock.service.RemoteService;

/**
 * 监听开机、点亮屏幕、网络改变广播
 *
 * @author kaifang
 * @date 2017/9/5 11:57
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        boolean isChecked = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isChecked", false);
        if (isChecked) {
            Intent intent2 = new Intent(context.getApplicationContext(), AndroidService.class);
            intent2.putExtra("isChecked", true);
            context.startService(intent2);
            context.startService(new Intent(context.getApplicationContext(), RemoteService.class));
        }
    }
}
