package com.jackiez.questionhouse.manager;

import android.content.Context;
import android.graphics.Color;

import com.jackiez.questionhouse.utils.ThreadUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public final class DialogManager {

    private static final class SingletonHolder {
        static final DialogManager instance = new DialogManager();
    }

    public static DialogManager getInstance() {
        return SingletonHolder.instance;
    }

    private DialogManager() {
    }

    private SweetAlertDialog mLoadingDialog;

    public void showLoading(final Context context, final String content) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    mLoadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    mLoadingDialog.setCancelable(false);
                    mLoadingDialog.setTitleText(content);
                    mLoadingDialog.show();
                }
            }
        });
    }

    public void hideLoading() {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                    mLoadingDialog.dismissWithAnimation();
                }
            }
        });
    }

    public void showSuccessDialog(Context context, String title, String content) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }
}
