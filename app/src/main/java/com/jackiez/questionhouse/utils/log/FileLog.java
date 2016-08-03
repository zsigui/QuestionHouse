package com.jackiez.questionhouse.utils.log;

import android.text.TextUtils;

import com.jackiez.questionhouse.QuestionApp;
import com.jackiez.questionhouse.config.AppConfig;
import com.jackiez.questionhouse.utils.DateUtil;
import com.jackiez.questionhouse.utils.FileUtil;

import java.io.File;

/**
 * Created by zsigui on 16-6-21.
 */
public class FileLog {

    private static String debugDirectory;
    private static String debugFileName;

    public static String getDebugDirectory() {
        initDebugFile();
        return debugDirectory;
    }


    public static String getDebugFileName() {
        initDebugFile();
        return debugFileName;
    }

    private static void initDebugFile() {
        if (debugDirectory == null || debugFileName == null) {
            File f = FileUtil.getOwnCacheDirectory(QuestionApp.getInstance(), AppConfig.LOGGING_CACHE_PATH, true);
            debugDirectory = f.getAbsolutePath();
            debugFileName = DateUtil.formatTime(System.currentTimeMillis(), "yyyyMMdd") + ".log";
        }
    }

    public static void printFile(String directory, String filename, String content) {
        printFile(directory, filename, content, null, true);
    }

    public static void printFile(String directory, String filename, String content, String charset, boolean isAppend) {
        if (TextUtils.isEmpty(directory)) {
            directory = getDebugDirectory();
        }
        if (TextUtils.isEmpty(filename)) {
            filename = getDebugFileName();
        }
        if (TextUtils.isEmpty(charset)) {
            charset = FileUtil.DEFAULT_CHASET;
        }
        FileUtil.writeString(new File(directory, filename), content, charset, isAppend);
    }
}
