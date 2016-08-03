package com.jackiez.questionhouse;

import android.app.Application;

import com.jackiez.questionhouse.asynctask.AsyncTask_Init;
import com.jackiez.questionhouse.utils.CrashHandler;
import com.jackiez.questionhouse.utils.log.GCLog;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class QuestionApp extends Application {

    private static QuestionApp mInstance;

    public static QuestionApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        GCLog.init(BuildConfig.DEBUG);
        CrashHandler.getInstance().init();
        new AsyncTask_Init(this).execute();
    }
}
