package com.example.rokly.notadoctor.Model.Places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Southwest implements Parcelable {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    public final static Parcelable.Creator<Southwest> CREATOR = new Creator<Southwest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Southwest createFromParcel(Parcel in) {
            return new Southwest(in);
        }

        public Southwest[] newArray(int size) {
            return (new Southwest[size]);
        }

    }
            ;

    protected Southwest(Parcel in) {
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lng = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Southwest() {
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public Southwest(Double lat, Double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(lat);
        dest.writeValue(lng);
    }

    public int describeContents() {
        return 0;
    }

}