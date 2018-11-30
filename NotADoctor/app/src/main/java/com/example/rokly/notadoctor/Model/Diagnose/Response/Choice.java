package com.example.rokly.notadoctor.Model.Diagnose.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Choice implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("label")
    @Expose
    private String label;
    public final static Parcelable.Creator<Choice> CREATOR = new Creator<Choice>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }

        public Choice[] newArray(int size) {
            return (new Choice[size]);
        }

    }
            ;

    protected Choice(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.label = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Choice() {
    }

    /**
     *
     * @param id
     * @param label
     */
    public Choice(String id, String label) {
        super();
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(label);
    }

    public int describeContents() {
        return 0;
    }

}