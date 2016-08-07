package com.jackiez.questionhouse;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jackiez.questionhouse.asynctask.AsyncTask_Init;
import com.jackiez.questionhouse.ui.widget.LoadAndRetryViewManager;
import com.jackiez.questionhouse.utils.log.GCLog;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class QuestionApp extends MultiDexApplication {

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
//        CrashHandler.getInstance().init();
        initLoadAndRetryManager();
        new AsyncTask_Init(this).execute();
    }

    private void initLoadAndRetryManager() {
        LoadAndRetryViewManager.setDefaultEmptyViewId(R.layout.activity_empty);
        LoadAndRetryViewManager.setDefaultErrorRetryViewId(R.layout.activity_empty);
        LoadAndRetryViewManager.setDefaultLoadViewId(R.layout.activity_empty);
    }

    @Override
    protected void attachBaseContext(Context base) {
        mInstance = this;
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
