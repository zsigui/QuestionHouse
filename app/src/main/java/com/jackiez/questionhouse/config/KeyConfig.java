package com.jackiez.questionhouse.config;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public interface KeyConfig {

    String KEY_ARGS_1 = "data_args_1";
    String KEY_ARGS_2 = "data_args_2";
    String KEY_ARGS_3 = "data_args_3";

    String EXAM_STATE_KEY_MODE = "mode_in_exam";
    int EXAM_MODE_ANSWER = 1;
    int EXAM_MODE_PRACTICE = 2;

    String QUESTION_TYPE_JUDGE = "判断题";
    String QUESTION_TYPE_MULTIPLE = "多选题";
    String QUESTION_TYPE_SINGLE = "单选题";
    String JUDGE_OK = "正确";
    String JUDGE_NO = "错误";
}
