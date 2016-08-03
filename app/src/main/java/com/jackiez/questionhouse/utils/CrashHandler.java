package com.jackiez.questionhouse.utils;

import android.os.Process;

import com.jackiez.questionhouse.utils.log.AppDebugConfig;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sInstance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {}

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            if (!handleThrowable(ex) && mDefaultHandler != null) {
                AppDebugConfig.e(AppDebugConfig.TAG_DEBUG_INFO, "UncaughtException is handled by System DefaultHandler");
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                AppDebugConfig.e(AppDebugConfig.TAG_DEBUG_INFO, "UncaughtException is handled by Custom");
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        } catch (Throwable e) {
            AppDebugConfig.e(AppDebugConfig.TAG_DEBUG_INFO, e);
        }
    }

    private boolean handleThrowable(Throwable ex) {
        if (ex == null)
            return false;

        ex.printStackTrace();
        AppDebugConfig.w(AppDebugConfig.TAG_APP, ex);
        AppDebugConfig.file(AppDebugConfig.TAG_APP, "crash.log", ex);
        return true;
    }
}
