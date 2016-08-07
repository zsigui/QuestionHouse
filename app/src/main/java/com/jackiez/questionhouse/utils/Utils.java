package com.jackiez.questionhouse.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collection;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class Utils {

    public static boolean isEmpty(Object obj) {
        if (obj != null) {
            if (obj instanceof CharSequence)
                return ((CharSequence) obj).length() == 0;
            else
                return obj instanceof Collection && ((Collection) obj).isEmpty();
        }
        return true;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

    }

    /**
     * 将字节数组转换成十六进制字符串
     *
     * @param bs
     * @return
     */
    public static String bytesToHex(byte... bs) {
        if (isEmpty(bs)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bs) {
            int bt = b & 0xff;
            if (bt < 16) {
                builder.append(0);
            }
            builder.append(Integer.toHexString(bt));
        }
        return builder.toString();
    }
}
