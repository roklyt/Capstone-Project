package com.example.rokly.notadoctor.Model.Diagnose.Request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Evidence implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<Evidence> CREATOR = new Parcelable.Creator<Evidence>() {
        @Override
        public Evidence createFromParcel(Parcel parcel) {
            return new Evidence(parcel);
        }

        @Override
        public Evidence[] newArray(int i) {
            return new Evidence[i];
        }

    };

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("choice_id")
    @Expose
    private String choiceId;

    public Evidence(String id, String choiceId) {
        this.id = id;
        this.choiceId = choiceId;
    }

    private Evidence(Parcel in) {
        id = in.readString();
        choiceId = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(choiceId);
    }

}