package cn.smiles.andclock.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.Collections;
import java.util.List;

/**
 * 工具类
 *
 * @author kaifang
 * @date 2018/4/10 16:57
 */
public class Ktools {
    /**
     * 检测是否安装指定的APP
     *
     * @param context
     * @param targetPackage
     * @return
     */
    public static boolean isPackageExists(Context context, String targetPackage) {
        List<ApplicationInfo> packages = getAllPackages(context, 0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有已安装APP包名
     *
     * @param context
     * @param sortFlag 排序标志  0：不做排序，1：用户安装app在前，2：系统自带app在前，3：可启动运行APP在前
     * @return
     */
    public static List<ApplicationInfo> getAllPackages(Context context, int sortFlag) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        switch (sortFlag) {
            case 1:
                Collections.sort(applications, (o1, o2) -> Integer.compare((o1.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)),
                        (o2.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM))));
                break;
            case 2:
                Collections.sort(applications, (o1, o2) -> Integer.compare((o2.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)),
                        (o1.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM))));
                break;
            case 3:
                Collections.sort(applications, (o1, o2) -> {
                    Intent launchIntentForPackage1 = pm.getLaunchIntentForPackage(o1.packageName);
                    Intent launchIntentForPackage2 = pm.getLaunchIntentForPackage(o2.packageName);
                    if (launchIntentForPackage2 == null) {
                        return (launchIntentForPackage1 == null) ? 0 : -1;
                    }
                    if (launchIntentForPackage1 == null) {
                        return 1;
                    }
                    return 2;
                });
                break;
        }
        return applications;
    }

    /**
     * 通过包名运行APP
     *
     * @param activity
     * @param spackage
     * @return
     */
    public static boolean runAPPByPackageName(Activity activity, String spackage) {
        if (TextUtils.isEmpty(spackage))
            return false;
        Intent LaunchIntent = activity.getPackageManager().getLaunchIntentForPackage(spackage);
        if (LaunchIntent != null) {
            activity.startActivity(LaunchIntent);
            return true;
        }
        return false;
    }
}
