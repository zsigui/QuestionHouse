package com.jackiez.questionhouse.utils;

import android.content.Context;
import android.content.Intent;

import com.jackiez.questionhouse.ui.activity.ExamActivity;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class IntentUtil {



    public static void jumpExam(Context context) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
