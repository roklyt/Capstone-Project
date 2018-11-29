package com.example.rokly.notadoctor.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Diagnose implements Parcelable{

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<Diagnose> CREATOR = new Parcelable.Creator<Diagnose>() {
        @Override
        public Diagnose createFromParcel(Parcel parcel) {
            return new Diagnose(parcel);
        }

        @Override
        public Diagnose[] newArray(int i) {
            return new Diagnose[i];
        }

    };

    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("evidence")
    @Expose
    private List<Evidence> evidence = null;

    private Diagnose(Parcel in) {
        sex = in.readString();
        age = in.readInt();
        evidence = new ArrayList<Evidence>();
        in.readTypedList(evidence, null);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Evidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<Evidence> evidence) {
        this.evidence = evidence;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sex);
        parcel.writeInt(age);
        parcel.writeTypedList(evidence);
    }
}