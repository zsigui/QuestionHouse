package com.jackiez.questionhouse.iface;

import com.jackiez.questionhouse.config.KeyConfig;
import com.jackiez.questionhouse.model.Exam;
import com.jackiez.questionhouse.model.Question;
import com.jackiez.questionhouse.utils.FileUtil;
import com.jackiez.questionhouse.utils.QuestionUtil;
import com.jackiez.questionhouse.utils.Utils;
import com.jackiez.questionhouse.utils.chiper.MD5;
import com.jackiez.questionhouse.utils.log.AppDebugConfig;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/6
 */
public abstract class AbsWorker implements IWorker {

    public static String META_ID = "序号";
    public static String META_CONTENT = "题目";
    public static String META_DIFFICULTY = "难易度";
    public static String META_BUSINESS_TYPE = "业务类型";
    public static String META_TYPE = "题型";
    public static String META_CHOICES = "选择项";
    public static String META_ANSWER = "答案";

    @Override
    public Exam process(String path) {
        Exam result = null;
        List<List<Object>> data = extractTableToLists(path);
        if (data != null) {
            List<Object> metaRow = data.get(0);
            if (Utils.isEmpty(metaRow)) {
                AppDebugConfig.d("获取元组属性失败：" + metaRow);
                return null;
            }
            List<String> metaList = new ArrayList<>();
            for (Object obj : metaRow) {
                if (obj != null) {
                    metaList.add(formatString(obj));
                }
            }
            metaRow = null;
            AppDebugConfig.d("获取METAS值：" + metaList);
            RealmList<Question> questions = new RealmList<>();
            AppDebugConfig.d("总的行数：" + data.size());
            boolean allAnswerInOneCell = metaList.contains(META_ANSWER);
            for (int i = 1; i < data.size(); i++) {
                // 遍历所有行数据
                List<Object> row = data.get(i);
                if (!Utils.isEmpty(row)) {
                    List<String> choices = null;
                    if (!allAnswerInOneCell) {
                        choices = new ArrayList<>(24);
                    }
                    Question question = new Question();
                    for (int k = 0; k < metaList.size(); k++) {
                        // 遍历一行的所有列格子
                        String meta = metaList.get(k);
                        extractToSetQuestion(choices, question, meta, row.get(k));
                    }
                    if (!allAnswerInOneCell) {
                        QuestionUtil.removeBackNullChoice(choices);
                        question.setChoices(QuestionUtil.joinChoicesToString(choices));
                    }
                    normalizeQuestion(question);
                    questions.add(question);
                }
            }
            result = new Exam();
            result.setName(FileUtil.extractName(path));
            result.setQuestions(questions);
            result.setId(MD5.digestInHex(result.getName(), "UTF-8"));
        }
        return result;
    }

    private void normalizeQuestion(Question question) {
        if (!Utils.isEmpty(question.getType()) && !Utils.isEmpty(question.getAnswer())) {
            String type = question.getType();
            String answer = question.getAnswer().trim().replaceAll("\\s", "").toUpperCase();
            if (KeyConfig.QUESTION_TYPE_JUDGE.equalsIgnoreCase(type)) {
                if ("对".equals(answer) || "正确".equals(answer)
                        || "√".equalsIgnoreCase(answer)) {
                    answer = KeyConfig.JUDGE_OK;
                } else if ("错".equals(answer) || "错误".equals(answer)
                        || "×".equalsIgnoreCase(answer)) {
                    answer = KeyConfig.JUDGE_NO;
                }
            } else if (KeyConfig.QUESTION_TYPE_SINGLE.equalsIgnoreCase(type)) {
                answer = answer.substring(0, 1);
            }
            question.setAnswer(answer);
        }
    }



    protected abstract List<List<Object>> extractTableToLists(String path);

    private String formatString(Object val) {
        if (val == null) {
            return "";
        } else if (val instanceof String) {
            return ((String) val).trim();
        }
        return String.valueOf(val).trim();
    }

    /**
     * 从表格中提取值并设置到Question对象的对应属性
     */
    private void extractToSetQuestion(List<String> choices, Question question, String meta, Object val) {
        if (META_TYPE.equalsIgnoreCase(meta)) {
            question.setType(formatString(val));
        } else if (META_ID.equalsIgnoreCase(meta)) {
            question.setId(formatString(val));
        } else if (META_ANSWER.equalsIgnoreCase(meta)) {
            question.setAnswer(formatString(val));
        } else if (META_BUSINESS_TYPE.equalsIgnoreCase(meta)) {
            question.setBusinessType(formatString(val));
        } else if (META_CONTENT.equalsIgnoreCase(meta)) {
            question.setContent(formatString(val));
        } else if (META_DIFFICULTY.equalsIgnoreCase(meta)) {
            question.setDifficulty(formatString(val));
        } else if (META_CHOICES.equalsIgnoreCase(meta)) {
            question.setChoices(formatString(val));
        } else {
            if (choices != null) {
                // 选项或者未知列
                String tmp = formatString(val).trim();
                if (tmp.length() == 1) {
                    int iAnswer = QuestionUtil.getIndexByUpperAlpha(tmp.toUpperCase().charAt(0));
                    if (iAnswer >= 'A' && iAnswer <= 'Z') {
                        // 答案
                        choices.add(iAnswer - 'A', formatString(val));
                    }
                }
            }
        }
    }
}
