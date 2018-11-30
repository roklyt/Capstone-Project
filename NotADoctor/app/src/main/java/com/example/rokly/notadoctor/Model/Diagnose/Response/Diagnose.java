package com.example.rokly.notadoctor.Model.Diagnose.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Diagnose implements Parcelable {

    @SerializedName("question")
    @Expose
    private Question question;
    @SerializedName("conditions")
    @Expose
    private List<Condition> conditions = null;
    @SerializedName("should_stop")
    @Expose
    private Boolean shouldStop;
    public final static Parcelable.Creator<Diagnose> CREATOR = new Creator<Diagnose>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Diagnose createFromParcel(Parcel in) {
            return new Diagnose(in);
        }

        public Diagnose[] newArray(int size) {
            return (new Diagnose[size]);
        }

    }
            ;

    protected Diagnose(Parcel in) {
        this.question = ((Question) in.readValue((Question.class.getClassLoader())));
        this.conditions = new ArrayList<>();
        in.readList(this.conditions, (Condition.class.getClassLoader()));
        this.shouldStop = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Diagnose() {
    }

    /**
     *
     * @param conditions
     * @param shouldStop
     * @param question
     */
    public Diagnose(Question question, List<Condition> conditions, Boolean shouldStop) {
        super();
        this.question = question;
        this.conditions = conditions;
        this.shouldStop = shouldStop;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public Boolean getShouldStop() {
        return shouldStop;
    }

    public void setShouldStop(Boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(question);
        dest.writeList(conditions);
        dest.writeValue(shouldStop);
    }

    public int describeContents() {
        return 0;
    }

}