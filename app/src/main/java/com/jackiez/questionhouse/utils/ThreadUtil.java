package com.jackiez.questionhouse.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class ThreadUtil {
    private static Handler sHandler;
    private static ExecutorService sExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread t = new Thread(runnable);
            t.setPriority(Thread.NORM_PRIORITY - 1);
            return t;
        }
    });

    private ThreadUtil() {
    }

    private static Handler getHandler() {
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        return sHandler;
    }

    /**
     * 在子线程执行任务
     *
     * @param task
     */
    public static void runInThread(Runnable task) {
        sExecutor.execute(task);
    }


    /**
     * 在UI线程执行任务
     *
     * @param task
     */
    public static void runOnUiThread(Runnable task) {
        getHandler().post(task);
    }

    public static void remove(Runnable task) {
        if (sHandler != null) {
            sHandler.removeCallbacks(task);
        }
    }

    /**
     * 在UI线程延时执行任务
     *
     * @param task
     * @param delayMillis 延时时间，单位毫秒
     */
    public static void runOnUiThread(Runnable task, long delayMillis) {
        getHandler().postDelayed(task, delayMillis);
    }

    public static void destroy() {
        if (sHandler != null) {
            sHandler.removeCallbacksAndMessages(null);
            sHandler = null;
        }
        if (sExecutor != null) {
            sExecutor.shutdown();
        }
    }
}