package com.jackiez.questionhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.jackiez.questionhouse.R;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class ChoicesAdapter<String> extends BaseAdapter {

    private List<String> mData;
    private Context mContext;

    public ChoicesAdapter(Context context) {
        mContext = context;
    }

    public ChoicesAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    public List<String> getData() {
        return mData;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public String getItem(int position) {
        return getCount() != 0 ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_question_choice, parent, false);
            vh = new ViewHolder();
            vh.tvChoice = (CheckedTextView) convertView.findViewById(R.id.tv_choice);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String item = getItem(position);
        vh.tvChoice.setText((CharSequence) item);
        return convertView;
    }

    public void updateData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        CheckedTextView tvChoice;
    }
}
