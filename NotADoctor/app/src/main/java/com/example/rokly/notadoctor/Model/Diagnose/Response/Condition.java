package com.example.rokly.notadoctor.Model.Diagnose.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Condition implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("probability")
    @Expose
    private Double probability;
    public final static Parcelable.Creator<Condition> CREATOR = new Creator<Condition>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Condition createFromParcel(Parcel in) {
            return new Condition(in);
        }

        public Condition[] newArray(int size) {
            return (new Condition[size]);
        }

    }
            ;

    protected Condition(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.probability = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Condition() {
    }

    /**
     *
     * @param id
     * @param name
     * @param probability
     */
    public Condition(String id, String name, Double probability) {
        super();
        this.id = id;
        this.name = name;
        this.probability = probability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(probability);
    }

    public int describeContents() {
        return 0;
    }

}