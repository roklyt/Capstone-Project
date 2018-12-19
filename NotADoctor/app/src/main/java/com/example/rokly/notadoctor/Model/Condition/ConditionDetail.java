package com.example.rokly.notadoctor.Model.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConditionDetail implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("common_name")
    @Expose
    private String commonName;
    @SerializedName("sex_filter")
    @Expose
    private String sexFilter;
    @SerializedName("categories")
    @Expose
    private List<String> categories;
    @SerializedName("prevalence")
    @Expose
    private String prevalence;
    @SerializedName("acuteness")
    @Expose
    private String acuteness;
    @SerializedName("severity")
    @Expose
    private String severity;
    @SerializedName("extras")
    @Expose
    private Extras extras;
    @SerializedName("triage_level")
    @Expose
    private String triageLevel;
    public final static Parcelable.Creator<ConditionDetail> CREATOR = new Creator<ConditionDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ConditionDetail createFromParcel(Parcel in) {
            return new ConditionDetail(in);
        }

        public ConditionDetail[] newArray(int size) {
            return (new ConditionDetail[size]);
        }

    }
            ;

    private ConditionDetail(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.commonName = ((String) in.readValue((String.class.getClassLoader())));
        this.sexFilter = ((String) in.readValue((String.class.getClassLoader())));
        this.categories = new ArrayList<>();
        in.readList(this.categories, (java.lang.String.class.getClassLoader()));
        this.prevalence = ((String) in.readValue((String.class.getClassLoader())));
        this.acuteness = ((String) in.readValue((String.class.getClassLoader())));
        this.severity = ((String) in.readValue((String.class.getClassLoader())));
        this.extras = ((Extras) in.readValue((Extras.class.getClassLoader())));
        this.triageLevel = ((String) in.readValue((String.class.getClassLoader())));
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

    public String getCommonName() {
        return commonName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getPrevalence() {
        return prevalence;
    }


    public String getAcuteness() {
        return acuteness;
    }


    public String getSeverity() {
        return severity;
    }


    public Extras getExtras() {
        return extras;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(commonName);
        dest.writeValue(sexFilter);
        dest.writeList(categories);
        dest.writeValue(prevalence);
        dest.writeValue(acuteness);
        dest.writeValue(severity);
        dest.writeValue(extras);
        dest.writeValue(triageLevel);
    }

    public int describeContents() {
        return 0;
    }

}