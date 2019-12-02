package com.hxh.commonthirdparty.bugly;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * bugly 工具类
 */
public class BuglyUtil {

    public static void init(Context appContext, String appid)
    {
        boolean isDebug = BuildConfig.DEBUG;
        String versionName = getVersionName(appContext);

        // 获取当前包名
        String packageName = appContext.getPackageName();
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(appContext);
        strategy.setAppChannel("bugly");  //设置渠道
        strategy.setAppVersion(versionName);      //App的版本
        strategy.setAppPackageName(packageName);  //App的包名

        CrashReport.initCrashReport(appContext, appid, isDebug, strategy);
    }

    private static String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否是主进程
     * @param appContext
     * @return
     */
    public static boolean isMainProcess(Context appContext)
    {
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 获取当前包名
        String packageName = appContext.getPackageName();
        return packageName.equals(processName);
    }

    /**
     * 获取进程号对应的进程名
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
