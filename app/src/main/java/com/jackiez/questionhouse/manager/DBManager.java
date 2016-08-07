package com.jackiez.questionhouse.manager;

import android.content.Context;

import com.jackiez.questionhouse.BuildConfig;
import com.jackiez.questionhouse.QuestionApp;
import com.jackiez.questionhouse.model.Exam;
import com.jackiez.questionhouse.utils.ToastUtil;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public final class DBManager {

    private static final class SingletonHolder {
        static final DBManager instance = new DBManager();
    }

    private Realm mRealm;
    private Context mContext;

    private DBManager() {
        mContext = QuestionApp.getInstance();
    }

    public static DBManager getInstance() {
        return SingletonHolder.instance;
    }

    private static final String DB_NAME = "questionhouse.realm";

    public void init() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(mContext)
                .schemaVersion(BuildConfig.VERSION_CODE)
                .name(DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        mRealm = Realm.getInstance(realmConfig);
    }

    /**
     * 将新题库写入数据库
     */
    public void store(final Exam exam) {
        if (exam == null || exam.getQuestionCount() == 0) {
            ToastUtil.showShort("写入数据库题库为空，执行失败!");
            return;
        }
        if (mRealm.where(Exam.class).equalTo("id", exam.getId()).count() == 0) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
//                for (Question q : exam.getQuestions()) {
//                    realm.copyToRealm(q);
//                }
                    realm.copyToRealm(exam);

                }
            });
        }
    }

    /**
     * 获取指定ID的题库数据
     *
     * @param id 题库ID
     * @return
     */
    public Exam restore(int id) {
        Exam result = mRealm.where(Exam.class).equalTo("id", id).findFirst();
        return result;
    }

    /**
     * 获取题库列表
     *
     * @return 返回题库列表
     */
    public List<Exam> obtainExamList() {
        List<Exam> result = mRealm.where(Exam.class).findAllSorted("id", Sort.DESCENDING);
        return result;
    }

    /**
     * 移除指定题库
     */
    public void remove(final Exam exam) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                exam.deleteFromRealm();
            }
        });
    }

    /**
     * 从列表中删除指定位置的题库
     */
    public void removeFromList(final RealmList<Exam> exams, final int index) {
        if (exams == null || index > exams.size()) {
            AppDebugConfig.w("删除的题库索引超过实际");
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                exams.deleteFromRealm(index);
            }
        });
    }

    public void close() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }
}
