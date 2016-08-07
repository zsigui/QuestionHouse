package com.jackiez.questionhouse.manager;

import android.app.Activity;
import android.app.Fragment;

import com.jackiez.questionhouse.utils.log.AppDebugConfig;

import java.util.ArrayList;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class ObserverManager {

    private ObserverManager(){}

    private static final class SingletonHolder{
        static final ObserverManager instance = new ObserverManager();
    }

    public static ObserverManager getInstance() {
        return SingletonHolder.instance;
    }

    private ArrayList<OnStateChangeListener> mOnStateChangeListeners = new ArrayList<>();

    public void addOnStateChangeListener(OnStateChangeListener listener) {
        if (listener != null) {
            mOnStateChangeListeners.add(listener);
        }
    }

    public void removeOnStateChangeListener(OnStateChangeListener listener) {
        if (listener != null) {
            int index;
            if ((index = mOnStateChangeListeners.indexOf(listener)) != -1) {
                mOnStateChangeListeners.remove(index);
            }
        }
    }

    public void notifyOnStateChangeListener(String eventKey, int currentState) {
        for (OnStateChangeListener observer : mOnStateChangeListeners) {
            try {
                if (observer != null) {
                    if (observer instanceof Fragment && ((Fragment) observer).isRemoving()) {
                        // 当为fragment且正被移除出界面，不更新，正确？
                        AppDebugConfig.v("removed removing fragment observer { " + observer + " }");
                        continue;
                    }
                    if (observer instanceof Activity && ((Activity) observer).isFinishing()) {
                        // 当Activity即将被销毁，无须更新
                        AppDebugConfig.v("removed activity finishing observer { " + observer + " }");
                        continue;
                    }
                    observer.onStateChange(eventKey, currentState);
                }
            } catch (Throwable e) {
                AppDebugConfig.e(e.getMessage());
            }
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(String eventKey, int currentState);
    }
}
