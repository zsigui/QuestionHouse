package com.jackiez.questionhouse.ui.fragment.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.questionhouse.utils.log.AppDebugConfig;

/**
 * @author micle
 * @email zsigui@foxmail.com
 * @date 2015/12/19
 */
public abstract class BaseFragmentLog extends Fragment{


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        AppDebugConfig.v();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AppDebugConfig.v();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDebugConfig.v();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppDebugConfig.v();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppDebugConfig.v();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDebugConfig.v();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        AppDebugConfig.v();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppDebugConfig.v();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AppDebugConfig.v();
    }

    @Override
    public void onPause() {
        super.onPause();
        AppDebugConfig.v();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppDebugConfig.v();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppDebugConfig.v();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDebugConfig.v();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppDebugConfig.v();
    }
}
