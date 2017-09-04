package cn.smiles.andclock.tools;

/*
 * Copyright (C) 2016 Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RemovableStorageFinder {

    public static File getRemovabeStorageDir(Context context) {
        try {
            List<File> storages = getRemovabeStorages(context);
            if (!storages.isEmpty()) {
                return storages.get(0);
            }
        } catch (Exception ignored) {
        }
        final String SECONDARY_STORAGE = System.getenv("SECONDARY_STORAGE");
        if (SECONDARY_STORAGE != null) {
            return new File(SECONDARY_STORAGE.split(":")[0]);
        }
        return null;
    }

    public static List<File> getRemovabeStorages(Context context) throws Exception {
        List<File> storages = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storages.add(Environment.getExternalStorageDirectory());
        }
        Method getService = Class.forName("android.os.ServiceManager")
                .getDeclaredMethod("getService", String.class);
        if (!getService.isAccessible()) getService.setAccessible(true);
        IBinder service = (IBinder) getService.invoke(null, "mount");

        Method asInterface = Class.forName("android.os.storage.IMountService$Stub")
                .getDeclaredMethod("asInterface", IBinder.class);
        if (!asInterface.isAccessible()) asInterface.setAccessible(true);
        Object mountService = asInterface.invoke(null, service);

        Object[] storageVolumes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            int uid = context.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.uid;
            Method getVolumeList = mountService.getClass().getDeclaredMethod(
                    "getVolumeList", int.class, String.class, int.class);
            if (!getVolumeList.isAccessible()) getVolumeList.setAccessible(true);
            storageVolumes = (Object[]) getVolumeList.invoke(mountService, uid, packageName, 0);
        } else {
            Method getVolumeList = mountService.getClass().getDeclaredMethod("getVolumeList");
            if (!getVolumeList.isAccessible()) getVolumeList.setAccessible(true);
            storageVolumes = (Object[]) getVolumeList.invoke(mountService, (Object[]) null);
        }

        for (Object storageVolume : storageVolumes) {
            Class<?> cls = storageVolume.getClass();
            Method isRemovable = cls.getDeclaredMethod("isRemovable");
            if (!isRemovable.isAccessible()) isRemovable.setAccessible(true);
            if ((boolean) isRemovable.invoke(storageVolume, (Object[]) null)) {
                Method getState = cls.getDeclaredMethod("getState");
                if (!getState.isAccessible()) getState.setAccessible(true);
                String state = (String) getState.invoke(storageVolume, (Object[]) null);
                if (state.equals("mounted")) {
                    Method getPath = cls.getDeclaredMethod("getPath");
                    if (!getPath.isAccessible()) getPath.setAccessible(true);
                    String path = (String) getPath.invoke(storageVolume, (Object[]) null);
                    storages.add(new File(path));
                }
            }
        }
        return storages;
    }

}