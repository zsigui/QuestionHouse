package com.jackiez.questionhouse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/8/5
 */
public class Exam extends RealmObject implements Parcelable{

    private RealmList<Question> questions;

    private String name;

    @PrimaryKey
    private String id;

    public Exam(){}

    public Exam(RealmList<Question> questions, String name, String id) {
        this.questions = questions;
        this.name = name;
        this.id = id;
    }

    public RealmList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getQuestionCount() {
        return getQuestions() == null ? 0 : getQuestions().size();
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "题库名称:%s，题数:%d", getName(), getQuestionCount());
    }


    protected Exam(Parcel in) {
//        questions = in.createTypedArrayList(Question.CREATOR);
        in.readTypedList(questions, Question.CREATOR);
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<Exam> CREATOR = new Creator<Exam>() {
        @Override
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        @Override
        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(questions);
        dest.writeString(name);
        dest.writeString(id);
    }

    public void copyFrom(Exam exam) {
        setQuestions(exam.getQuestions());
        setName(exam.getName());
        setId(getId());
    }
}
