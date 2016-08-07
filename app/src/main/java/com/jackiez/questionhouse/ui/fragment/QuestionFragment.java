package com.jackiez.questionhouse.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jackiez.questionhouse.R;
import com.jackiez.questionhouse.adapter.ChoicesAdapter;
import com.jackiez.questionhouse.config.KeyConfig;
import com.jackiez.questionhouse.manager.ObserverManager;
import com.jackiez.questionhouse.model.Question;
import com.jackiez.questionhouse.ui.fragment.base.BaseFragment;
import com.jackiez.questionhouse.utils.QuestionUtil;
import com.jackiez.questionhouse.utils.ThreadUtil;
import com.jackiez.questionhouse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class QuestionFragment extends BaseFragment implements ObserverManager.OnStateChangeListener {

    private TextView tvContent;
    private ListView lvChoices;
    private Button btnConfirm;
    private TextView tvAnswer;
    private LinearLayout llAnswer;
    private ScrollView svContainer;

    private Question mData;
    private ChoicesAdapter<String> mAdapter;

    private static int mCurrentMode = KeyConfig.EXAM_MODE_ANSWER;

    public static QuestionFragment newInstance(Question q) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KeyConfig.KEY_ARGS_1, q);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initViewManger(R.layout.fragment_question);
        tvContent = getViewById(R.id.tv_content);
        lvChoices = getViewById(R.id.lv_choice);
        btnConfirm = getViewById(R.id.btn_ok);
        llAnswer = getViewById(R.id.ll_answer);
        tvAnswer = getViewById(R.id.tv_answer);
        svContainer = getViewById(R.id.sv_container);
    }

    @Override
    protected void setListener() {
        btnConfirm.setOnClickListener(this);
        ObserverManager.getInstance().addOnStateChangeListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        if (getArguments() == null) {
            ToastUtil.showShort("传输错误，请关闭重进");
            return;
        }
        Question q = getArguments().getParcelable(KeyConfig.KEY_ARGS_1);
        if (q == null) {
            ToastUtil.showShort("传输错误，请关闭重进: q = null");
            return;
        }
        mViewManager.showContent();
        mAdapter = new ChoicesAdapter<String>(getContext().getApplicationContext());
        lvChoices.setAdapter(mAdapter);
        update(q);
    }

    @Override
    protected void lazyLoad() {

    }

    public void update(Question q) {
        if (tvContent == null) {
            return;
        }
        mData = q;
        tvContent.setText(Html.fromHtml(String.format(Locale.CHINA,
                "<font color=\"#2FA7E9\">[%s]</font> <font color=\"#2FA7E9\">[%s]</font> %s",
                mData.getId(), mData.getType(), mData.getContent())));
        tvAnswer.setText(Html.fromHtml(String.format(Locale.CHINA, "正确答案：<font color=\"#2FA7E9\">%s</font>",
                mData.getAnswer())));
        List<String> s;
        boolean mIsMultiple = (KeyConfig.QUESTION_TYPE_MULTIPLE.equalsIgnoreCase(q.getType()));
        if (KeyConfig.QUESTION_TYPE_JUDGE.equalsIgnoreCase(q.getType())) {
            s = new ArrayList<>(2);
            s.add(KeyConfig.JUDGE_OK);
            s.add(KeyConfig.JUDGE_NO);
        } else {
            s = QuestionUtil.extractChoicesFromString(mData.getChoices());
        }
        if (mIsMultiple) {
            lvChoices.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        } else {
            lvChoices.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        }
        mAdapter.updateData(s);
        if (mCurrentMode == KeyConfig.EXAM_MODE_ANSWER) {
            llAnswer.setVisibility(View.GONE);
        } else {
            llAnswer.setVisibility(View.VISIBLE);
        }
        svContainer.smoothScrollTo(0, 0);
    }

    @Override
    public void release() {
        super.release();
        ObserverManager.getInstance().removeOnStateChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_ok:
                if (llAnswer.getVisibility() == View.VISIBLE) {
                    llAnswer.setVisibility(View.GONE);
                } else {
                    llAnswer.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onStateChange(String eventKey, final int currentState) {
        if (KeyConfig.EXAM_STATE_KEY_MODE.equalsIgnoreCase(eventKey)) {
            ThreadUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (currentState) {
                        case KeyConfig.EXAM_MODE_PRACTICE:
                            mCurrentMode = KeyConfig.EXAM_MODE_PRACTICE;
                            if (llAnswer != null) {
                                llAnswer.setVisibility(View.VISIBLE);
                            }
                            break;
                        case KeyConfig.EXAM_MODE_ANSWER:
                            mCurrentMode = KeyConfig.EXAM_MODE_ANSWER;
                            if (llAnswer != null) {
                                llAnswer.setVisibility(View.GONE);
                            }
                            break;
                    }
                }
            });
        }
    }
}
