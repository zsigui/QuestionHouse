package com.jackiez.questionhouse.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class DateUtil {

    public static String formatTime(long timeInMillis, String format) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        return new SimpleDateFormat(format, Locale.CHINA).format(timeInMillis);
    }
}
