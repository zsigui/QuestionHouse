package com.jackiez.questionhouse.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackiez.questionhouse.R;
import com.jackiez.questionhouse.config.Global;
import com.jackiez.questionhouse.iface.impl.WorkerFactory;
import com.jackiez.questionhouse.manager.DBManager;
import com.jackiez.questionhouse.manager.DialogManager;
import com.jackiez.questionhouse.model.Exam;
import com.jackiez.questionhouse.ui.activity.base.BaseAppCompatActivity;
import com.jackiez.questionhouse.utils.IntentUtil;
import com.jackiez.questionhouse.utils.ToastUtil;
import com.jackiez.questionhouse.utils.Utils;

import java.util.List;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerActivity;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/3
 */
public class MainActivity extends BaseAppCompatActivity {

    private TextView tvContent;
    private Button btnAdd;
    private Button btnChoose;
    private Button btnStart;

    private boolean mNeedOpenChooser = false;

    private Exam mCurrentExam = null;
    private String mLastStartDirectory = null;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        tvContent = getViewById(R.id.tv_content);
        btnAdd = getViewById(R.id.btn_add);
        btnChoose = getViewById(R.id.btn_choose);
        btnStart = getViewById(R.id.btn_start);
    }

    @Override
    protected void processLogic() {
        btnAdd.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        tvContent.setText(getString(R.string.st_main_hint_no_warehouse));
        DBManager.getInstance().init();
        List<Exam> exams = DBManager.getInstance().obtainExamList();
        if (!Utils.isEmpty(exams)) {
            updateData(exams.get(0));
        }
    }

    @Override
    public void release() {
        super.release();
        DBManager.getInstance().close();
    }

    private void judgeWriteESPermission() {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            mNeedOpenChooser = true;
        } else {
            openFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (permissions.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    if (Manifest.permission.READ_EXTERNAL_STORAGE.equalsIgnoreCase(permissions[i])
                            && mNeedOpenChooser) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            openFileChooser();
                        } else {
                            ToastUtil.showShort("打开外部文件夹失败，请确保具有读取外部存储权限!");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_add:
                judgeWriteESPermission();
                break;
            case R.id.btn_start:
                if (mCurrentExam == null) {
                    ToastUtil.showShort("请先选择答题题库！");
                    return;
                }
                Global.setExam(mCurrentExam);
                IntentUtil.jumpExam(this);
                break;
            case R.id.btn_choose:
                ToastUtil.showShort("暂未实现该功能");
                break;
        }
    }

    /* 处理外部文件夹 */
    private final int EX_FILE_PICKER_RESULT = 111;

    private void openFileChooser() {
        Intent intent = new Intent(getApplicationContext(), ExFilePickerActivity.class);
        intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
        intent.putExtra(ExFilePicker.SET_FILTER_LISTED, new String[]{"xls", "xlsx"});
        intent.putExtra(ExFilePicker.DISABLE_NEW_FOLDER_BUTTON, true);
        intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_FILES);
        if (!Utils.isEmpty(mLastStartDirectory))
            intent.putExtra(ExFilePicker.SET_START_DIRECTORY, mLastStartDirectory);
        startActivityForResult(intent, EX_FILE_PICKER_RESULT);
        mNeedOpenChooser = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EX_FILE_PICKER_RESULT) {
            if (data != null) {

                ExFilePickerParcelObject obj = (ExFilePickerParcelObject) data.getParcelableExtra
                        (ExFilePickerParcelObject.class.getCanonicalName());
                if (obj.count > 0) {
                    String filename = obj.path + obj.names.get(0);
                    mLastStartDirectory = obj.path;
                    new AsyncTask<String, Integer, Exam>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            DialogManager.getInstance().showLoading(MainActivity.this, "解析文件中...");
                        }

                        @Override
                        protected Exam doInBackground(String... params) {
                            Exam exam =  Utils.isEmpty(params) ? null : WorkerFactory.getInstance().process(params[0]);
                            return exam;
                        }

                        @Override
                        protected void onPostExecute(Exam exam) {
                            if(exam != null) {
                                // 写入数据库
                                DBManager.getInstance().store(exam);
                            }
                            DialogManager.getInstance().hideLoading();
                            if (exam != null && exam.getQuestions() != null) {
                               updateData(exam);
                            }
                        }
                    }.execute(filename);
                }
            }
        }
    }

    private void updateData(Exam exam) {
        mCurrentExam = exam;
        // 加载完成选中当前数据库
        tvContent.setText(String.format(getString(R.string.st_main_hint_current_warehouse),
                mCurrentExam.getName()));
    }
}
