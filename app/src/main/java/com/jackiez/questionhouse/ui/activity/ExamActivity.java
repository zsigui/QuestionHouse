package com.jackiez.questionhouse.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.jackiez.questionhouse.R;
import com.jackiez.questionhouse.adapter.ExamPagerAdapter;
import com.jackiez.questionhouse.config.Global;
import com.jackiez.questionhouse.config.KeyConfig;
import com.jackiez.questionhouse.manager.ObserverManager;
import com.jackiez.questionhouse.model.Exam;
import com.jackiez.questionhouse.ui.activity.base.BaseAppCompatActivity;
import com.jackiez.questionhouse.ui.widget.transform.StackTransform;
import com.jackiez.questionhouse.utils.InputMethodUtil;
import com.jackiez.questionhouse.utils.ToastUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public class ExamActivity extends BaseAppCompatActivity implements ViewPager.OnPageChangeListener, OnItemClickListener {

    private ViewPager vpContent;
    private ExamPagerAdapter mAdapter;
    private DialogPlus mAnswerlistDialog;
    private TextView tvFinishedQuestionHint;
    private CheckedTextView tvAnswer;
    private CheckedTextView tvPractice;

    private EditText etIndex;
    private TextView btnJump;

    private int mCurrentIndex = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_exam);
        vpContent = getViewById(R.id.vp_container);
        tvFinishedQuestionHint = getViewById(R.id.tv_answer_finished_hint);
        tvAnswer = getViewById(R.id.tv_answer);
        tvPractice = getViewById(R.id.tv_practice);
        etIndex = getViewById(R.id.et_index);
        btnJump = getViewById(R.id.btn_jump);
    }

    @Override
    protected void processLogic() {
        handleIntent(getIntent());
        tvAnswer.setOnClickListener(this);
        tvPractice.setOnClickListener(this);
        btnJump.setOnClickListener(this);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            ToastUtil.showShort("intent为空，跳转失败");
            finish();
            return;
        }
        Exam exam = Global.getExam();
        if (exam == null) {
            ToastUtil.showShort("获取答题数据失败");
            finish();
            return;
        }
        mAdapter = new ExamPagerAdapter(getSupportFragmentManager(), exam.getQuestions());
        vpContent.addOnPageChangeListener(this);
        vpContent.setPageTransformer(true, new StackTransform());
        vpContent.setAdapter(mAdapter);
        vpContent.setCurrentItem(mCurrentIndex);
        etIndex.setHint("xx/" + exam.getQuestionCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        if (vpContent != null)
            vpContent.setCurrentItem(mCurrentIndex, true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_answer:
                setToolItemState(true);
                tvFinishedQuestionHint.setVisibility(View.VISIBLE);
                ObserverManager.getInstance()
                        .notifyOnStateChangeListener(KeyConfig.EXAM_STATE_KEY_MODE, KeyConfig.EXAM_MODE_ANSWER);
                break;
            case R.id.tv_practice:
                setToolItemState(false);
                tvFinishedQuestionHint.setVisibility(View.GONE);
                ObserverManager.getInstance()
                        .notifyOnStateChangeListener(KeyConfig.EXAM_STATE_KEY_MODE, KeyConfig.EXAM_MODE_PRACTICE);
                break;
            case R.id.btn_jump:
                try {
                    String s = etIndex.getText().toString();
                    vpContent.setCurrentItem(Integer.parseInt(s) - 1);
                    etIndex.setText("");
                    InputMethodUtil.hideSoftInput(this);
                } catch (Throwable t) {
                    ToastUtil.showShort("只能填入 <=最大题数 的数字");
                }
                break;
        }
    }

    private void setToolItemState(boolean isAnswer) {
        if (tvAnswer != null) {
            tvAnswer.setChecked(isAnswer);
        }
        if (tvPractice != null) {
            tvPractice.setChecked(!isAnswer);
        }
    }

    public void showDialogPlus() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setAdapter(null)
                .setContentHolder(new GridHolder(5))
                .setExpanded(true)
                .setOnItemClickListener(this)
                .create();
    }

    @Override
    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
        if (vpContent != null) {
            vpContent.setCurrentItem(position, true);
        }
    }
}
