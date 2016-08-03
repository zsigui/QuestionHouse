package com.jackiez.questionhouse.utils;

import android.text.TextUtils;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class QuestionUtil {

    /**
     * 获取从0开始的下标所代表的大写英文字母，即 0~25 对应 A~Z
     * @param index 字母下标
     * @return 字母对应的字符
     */
    public static char getUpperAlphaByIndex(int index) {
        return (char)(index + 'A');
    }

    /**
     * 从一整块的选项字符串中提取出选项数组
     *
     * @param content 数据
     * @param spilt 定义分隔符
     */
    public static String[] extractChoicesFromString(String content, String spilt) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        String[] cs = content.split(spilt);
        for (int i = 0; i < cs.length; i++) {
            cs[i] = cs[i].trim();
        }
        return cs;
    }
}
