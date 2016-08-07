package com.jackiez.questionhouse.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jackiez.questionhouse.model.Question;
import com.jackiez.questionhouse.ui.fragment.QuestionFragment;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class ExamPagerAdapter extends FragmentStatePagerAdapter{

    private List<Question> mQuestions;

    public ExamPagerAdapter(FragmentManager fm) {
        this(fm, null);
    }

    public ExamPagerAdapter(FragmentManager fm, List<Question> questions) {
        super(fm);
        mQuestions = questions;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    public void updateData(List<Question> questions) {
        this.mQuestions = questions;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        QuestionFragment f = (QuestionFragment) super.instantiateItem(container, position);
        if (f != null) {
            f.update(getQuestionItem(position));
        }
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }

    @Override
    public Fragment getItem(int position) {
        return QuestionFragment.newInstance(getQuestionItem(position));
    }

    public Question getQuestionItem(int position) {
        return getCount() == 0? null : mQuestions.get(position);
    }

    @Override
    public int getCount() {
        return mQuestions == null ? 0 : mQuestions.size();
    }
}
