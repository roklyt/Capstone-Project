package com.example.rokly.notadoctor.Model.Diagnose.Request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiagnoseReq implements Parcelable{

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<DiagnoseReq> CREATOR = new Parcelable.Creator<DiagnoseReq>() {
        @Override
        public DiagnoseReq createFromParcel(Parcel parcel) {
            return new DiagnoseReq(parcel);
        }

        @Override
        public DiagnoseReq[] newArray(int i) {
            return new DiagnoseReq[i];
        }

    };

    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("evidence")
    @Expose
    private List<Evidence> evidence;
    @SerializedName("extras")
    @Expose
    private Extras extras;

    public DiagnoseReq(String sex, int age, List<Evidence> evidence, Extras extras){
        this.sex = sex;
        this.age = age;
        this.evidence = evidence;
        this.extras = extras;
    }

    private DiagnoseReq(Parcel in) {
        sex = in.readString();
        age = in.readInt();
        evidence = new ArrayList<>();
        in.readList(evidence, Evidence.class.getClassLoader());
        extras = ((Extras) in.readValue((Extras.class.getClassLoader())));
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Evidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<Evidence> evidence) {
        this.evidence = evidence;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sex);
        parcel.writeInt(age);
        parcel.writeList(evidence);
        parcel.writeValue(extras);
    }
}