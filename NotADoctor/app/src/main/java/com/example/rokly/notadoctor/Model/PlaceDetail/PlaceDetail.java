package com.example.rokly.notadoctor.Model.PlaceDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetail implements Parcelable {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private DetailResult result;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Parcelable.Creator<PlaceDetail> CREATOR = new Creator<PlaceDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PlaceDetail createFromParcel(Parcel in) {
            return new PlaceDetail(in);
        }

        public PlaceDetail[] newArray(int size) {
            return (new PlaceDetail[size]);
        }

    }
            ;

    protected PlaceDetail(Parcel in) {
        in.readList(this.htmlAttributions, (java.lang.Object.class.getClassLoader()));
        this.result = ((DetailResult) in.readValue((DetailResult.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PlaceDetail() {
    }

    /**
     *
     * @param result
     * @param status
     * @param htmlAttributions
     */
    public PlaceDetail(List<Object> htmlAttributions, DetailResult result, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public DetailResult getResult() {
        return result;
    }

    public void setResult(DetailResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(htmlAttributions);
        dest.writeValue(result);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}