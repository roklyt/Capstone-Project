package com.example.rokly.notadoctor.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Mentions implements Parcelable {

    public static final String PARCELABLE_KEY = "parcelable_key";
    public final static Parcelable.Creator<Mentions> CREATOR = new Parcelable.Creator<Mentions>() {
        @Override
        public Mentions createFromParcel(Parcel parcel) {
            return new Mentions(parcel);
        }

        @Override
        public Mentions[] newArray(int i) {
            return new Mentions[i];
        }

    };


    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("choice_id")
    private String choiceId;
    @SerializedName("orth")
    private String orth;
    @SerializedName("common_name")
    private String commonName;


    public Mentions(String name, String id, String choiceId, String orth, String commonName, String type) {
        this.name = name;
        this.id = id;
        this.choiceId = choiceId;
        this.orth = orth;
        this.commonName = commonName;
    }

    private Mentions(Parcel in) {
        name = in.readString();
        id = in.readString();
        choiceId = in.readString();
        orth = in.readString();
        commonName = in.readString();
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

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getOrth() {
        return orth;
    }

    public void setOrth(String orth) {
        this.orth = orth;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(choiceId);
        parcel.writeString(orth);
        parcel.writeString(commonName);
    }
}
