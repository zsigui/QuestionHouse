package com.jackiez.questionhouse.utils;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class QuestionUtil {

    /**
     * 获取从0开始的下标所代表的大写英文字母，即 0~25 对应 A~Z
     *
     * @param index 字母下标
     * @return 字母对应的字符
     */
    public static char getUpperAlphaByIndex(int index) {
        return (char) (index + 'A');
    }

    public static int getIndexByUpperAlpha(char s) {
        return (int) s - (int) 'A';
    }

    /**
     * 从一整块的选项字符串中提取出选项数组
     *
     * @param content 数据
     * @param spilt   定义分隔符
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

    public static String joinChoicesToString(List<String> choices) {
        StringBuilder result = new StringBuilder("");
        if (choices != null) {
            for (String choice : choices) {
                result.append(choice).append("\n");
            }
            if (result.length() > 0) {
                result.deleteCharAt(result.length() - 1);
            }
        }
        return result.toString();
    }

    public static List<String> extractChoicesFromString(String choices) {
        return Arrays.asList(choices.split("\\s"));
    }

    public static void removeBackNullChoice(List<String> choices) {
        if (choices != null) {
            int i = choices.size() - 1;
            for (; i > -1; i--) {
                if (choices.get(i) == null) {
                    choices.remove(i);
                }
            }
        }
    }
}
