package com.jackiez.questionhouse.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class QuestionCountGridAdapter extends BaseAdapter {

    private int mCount;

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return position + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
