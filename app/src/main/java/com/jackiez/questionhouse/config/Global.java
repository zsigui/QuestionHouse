package com.jackiez.questionhouse.config;

import com.jackiez.questionhouse.model.Exam;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class Global {

    public static Exam sExam = null;

    public static Exam getExam() {
        return sExam;
    }

    public static void setExam(Exam exam) {
        sExam = exam;
    }
}
