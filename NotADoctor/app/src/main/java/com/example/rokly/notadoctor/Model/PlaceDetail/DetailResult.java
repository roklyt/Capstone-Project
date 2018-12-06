package com.example.rokly.notadoctor.Model.PlaceDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailResult implements Parcelable {

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    public final static Parcelable.Creator<DetailResult> CREATOR = new Creator<DetailResult>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DetailResult createFromParcel(Parcel in) {
            return new DetailResult(in);
        }

        public DetailResult[] newArray(int size) {
            return (new DetailResult[size]);
        }

    }
            ;

    protected DetailResult(Parcel in) {
        this.formattedPhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public DetailResult() {
    }

    /**
     *
     * @param formattedPhoneNumber
     * @param name
     * @param rating
     */
    public DetailResult(String formattedPhoneNumber, String name, Integer rating) {
        super();
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.name = name;
        this.rating = rating;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(formattedPhoneNumber);
        dest.writeValue(name);
        dest.writeValue(rating);
    }

    public int describeContents() {
        return 0;
    }

}