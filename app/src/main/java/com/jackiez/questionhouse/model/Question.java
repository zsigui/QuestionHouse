package com.jackiez.questionhouse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 存放每一题题型的实体数据
 *
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/2
 */
public class Question extends RealmObject implements Parcelable{

    /**
     * 序号
     */
    @PrimaryKey
    private String id;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 难度系数
     */
    private String difficulty;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 题型
     */
    private String type;

    /**
     * 选项，针对“判断题类型”，该项为空，忽略
     */
    private String choices;

    /**
     * 答案，对于“多选题”，该项为多值；对于“判断题”，该项为“对/错” 或者 “√/×”
     */
    private String answer;

    public Question(){}

    public Question(String id, String content, String difficulty, String businessType, String type, String choices, String
            answer) {
        this.id = id;
        this.content = content;
        this.difficulty = difficulty;
        this.businessType = businessType;
        this.type = type;
        this.choices = choices;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "序号:%s\n题目类型：%s\n题目:%s\n选项:%s\n答案:%s\n",
                getId(),
                getType(),
                getContent(),
                getChoices(),
                getAnswer());
    }


    protected Question(Parcel in) {
        id = in.readString();
        content = in.readString();
        difficulty = in.readString();
        businessType = in.readString();
        type = in.readString();
        choices = in.readString();
        answer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(difficulty);
        dest.writeString(businessType);
        dest.writeString(type);
        dest.writeString(choices);
        dest.writeString(answer);
    }

    public void copyFrom(Question q) {
        setId(q.getId());
        setContent(q.getContent());
        setDifficulty(q.getDifficulty());
        setBusinessType(q.getBusinessType());
        setType(q.getType());
        setChoices(q.getChoices());
        setAnswer(q.getAnswer());
    }
}
