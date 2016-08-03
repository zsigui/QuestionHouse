package com.jackiez.questionhouse.model;

/**
 * 存放每一题题型的实体数据
 *
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class Question {

    /**
     * 序号
     */
    private String mId;

    /**
     * 题目内容
     */
    private String mContent;

    /**
     * 难度系数
     */
    private String mDifficulty;

    /**
     * 业务类型
     */
    private String mBusinessType;

    /**
     * 题型
     */
    private String mType;

    /**
     * 选项
     */
    private String[] mChoices;

    /**
     * 答案
     */
    private int mAnswer;
}
