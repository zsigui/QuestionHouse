package com.jackiez.questionhouse.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.jackiez.questionhouse.QuestionApp;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class ToastUtil {

    public static void show(@StringRes int resId, final int duration) {
        Context context = QuestionApp.getInstance().getApplicationContext();
        Toast.makeText(context, context.getString(resId), duration).show();
    }

    public static void show(final CharSequence msg, final int duration) {
        Toast.makeText(QuestionApp.getInstance(), msg, duration).show();
    }

    public static void showLong(CharSequence msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public static void showShort(final CharSequence msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

}
