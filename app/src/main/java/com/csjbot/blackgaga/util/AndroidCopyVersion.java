package com.csjbot.blackgaga.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 孙秀艳 on 2017/11/8.
 */

public class AndroidCopyVersion {
    private Context ctx;

    public AndroidCopyVersion(Context context) {
        this.ctx = context;
    }

    public String getVersion() {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getVersionCode() {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            int code = info.versionCode;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
