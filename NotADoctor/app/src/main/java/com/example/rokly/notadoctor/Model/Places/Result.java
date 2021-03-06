package com.example.rokly.notadoctor.Model.Places;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.rokly.notadoctor.Model.PlaceDetail.DetailResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result implements Parcelable {

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("detail_result")
    @Expose
    private DetailResult detailResult;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }

    }
            ;

    protected Result(Parcel in) {
        this.formattedAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.geometry = ((Geometry) in.readValue((Geometry.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.openingHours = ((OpeningHours) in.readValue((OpeningHours.class.getClassLoader())));
        in.readList(this.photos, (com.example.rokly.notadoctor.Model.Places.Photo.class.getClassLoader()));
        this.placeId = ((String) in.readValue((String.class.getClassLoader())));
        this.plusCode = ((PlusCode) in.readValue((PlusCode.class.getClassLoader())));
        this.rating = ((Double) in.readValue((Double.class.getClassLoader())));
        this.reference = ((String) in.readValue((String.class.getClassLoader())));
        this.detailResult = ((DetailResult) in.readValue((DetailResult.class.getClassLoader())));
        in.readList(this.types, (java.lang.String.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Result() {
    }

    /**
     *
     * @param photos
     * @param id
     * @param icon
     * @param placeId
     * @param openingHours
     * @param name
     * @param plusCode
     * @param formattedAddress
     * @param rating
     * @param types
     * @param reference
     * @param detailResult
     * @param geometry
     */
    public Result(String formattedAddress, Geometry geometry, String icon, String id, String name, OpeningHours openingHours, List<Photo> photos, String placeId, PlusCode plusCode, Double rating, String reference, DetailResult detailResult, List<String> types) {
        super();
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.placeId = placeId;
        this.plusCode = plusCode;
        this.rating = rating;
        this.reference = reference;
        this.detailResult = detailResult;
        this.types = types;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setDetailResult(DetailResult detailResult) {
        this.detailResult = detailResult;
    }

    public DetailResult getDetailResult() {
        return detailResult;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(formattedAddress);
        dest.writeValue(geometry);
        dest.writeValue(icon);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(openingHours);
        dest.writeList(photos);
        dest.writeValue(placeId);
        dest.writeValue(plusCode);
        dest.writeValue(rating);
        dest.writeValue(reference);
        dest.writeValue(detailResult);
        dest.writeList(types);
    }

    public int describeContents() {
        return 0;
    }

}