package com.jackiez.questionhouse.asynctask;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class AsyncTask_Init extends AsyncTask<Object, Integer, Void> {

    private Context mContext;

    public AsyncTask_Init(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        return null;
    }
}
