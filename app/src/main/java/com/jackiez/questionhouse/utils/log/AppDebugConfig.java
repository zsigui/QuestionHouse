package com.jackiez.questionhouse.utils.log;

import com.jackiez.questionhouse.BuildConfig;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class AppDebugConfig {
    /**
     * debug模式，发布打包需要置为false，可以通过混淆让调试的log文本从代码文件中消除，避免被反编译时漏泄相关信息。
     */
    public static boolean IS_DEBUG = BuildConfig.LOG_DEBUG;
    public static boolean IS_FILE_DEBUG = BuildConfig.FILE_DEBUG;

    public static final String TAG_APP = "qh_app";

    public static final String TAG_FRAG = "qh_fragment";

    public static final String TAG_UTIL = "qh_util";

    public static final String TAG_DEBUG_INFO = "qh_info";
    /**
     * 所有注解该TAG的LOG需要进行删除
     */
    public static final String TAG_WARN = "qh_warning";

    public static final int STACKTRACE_INDEX = 6;

    public static void w(String tag, Object... object) {
        w(STACKTRACE_INDEX + 1, tag, object);
    }

    public static void w(int stacktraceIndex, String tag, Object... object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.w(stacktraceIndex, tag, object);
        }
    }

    public static void v() {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.v(STACKTRACE_INDEX, AppDebugConfig.TAG_DEBUG_INFO);
        }
    }

    public static void v(String tag, Object... object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.v(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void d(Object object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.d(STACKTRACE_INDEX, AppDebugConfig.TAG_DEBUG_INFO, object);
        }
    }

    public static void d(String tag, Object... object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.d(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void e(Object object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.e(STACKTRACE_INDEX, AppDebugConfig.TAG_DEBUG_INFO, object);
        }
    }

    public static void e(String tag, Object... object) {
        if (AppDebugConfig.IS_DEBUG) {
            GCLog.e(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void file(Object object) {
        if (AppDebugConfig.IS_DEBUG && AppDebugConfig.IS_FILE_DEBUG) {
            GCLog.file(STACKTRACE_INDEX, AppDebugConfig.TAG_DEBUG_INFO, null, null, object);
        }
    }

    public static void file(String tag, String filename, Object... object) {
        if (AppDebugConfig.IS_DEBUG && AppDebugConfig.IS_FILE_DEBUG) {
            GCLog.file(STACKTRACE_INDEX, tag, null, filename, object);
        }
    }
}
