package com.jackiez.questionhouse.model;

import com.jackiez.questionhouse.utils.Utils;

/**
 * 该类用于维护问题的当前状态
 *
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/7
 */
public class QuestionState {

    public String id;

    public String mSelfAnswer;

    public String mRealAnswer;

    public boolean isCorrect() {
        return isFinished() && mSelfAnswer.equalsIgnoreCase(mRealAnswer);
    }

    public boolean isFinished() {
        return !Utils.isEmpty(mSelfAnswer);
    }
}
