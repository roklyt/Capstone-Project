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
    private List<String> categories = null;
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

    protected ConditionDetail(Parcel in) {
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

    /**
     * No args constructor for use in serialization
     *
     */
    public ConditionDetail() {
    }

    /**
     *
     * @param id
     * @param acuteness
     * @param commonName
     * @param triageLevel
     * @param prevalence
     * @param name
     * @param severity
     * @param categories
     * @param sexFilter
     * @param extras
     */
    public ConditionDetail(String id, String name, String commonName, String sexFilter, List<String> categories, String prevalence, String acuteness, String severity, Extras extras, String triageLevel) {
        super();
        this.id = id;
        this.name = name;
        this.commonName = commonName;
        this.sexFilter = sexFilter;
        this.categories = categories;
        this.prevalence = prevalence;
        this.acuteness = acuteness;
        this.severity = severity;
        this.extras = extras;
        this.triageLevel = triageLevel;
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

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSexFilter() {
        return sexFilter;
    }

    public void setSexFilter(String sexFilter) {
        this.sexFilter = sexFilter;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getPrevalence() {
        return prevalence;
    }

    public void setPrevalence(String prevalence) {
        this.prevalence = prevalence;
    }

    public String getAcuteness() {
        return acuteness;
    }

    public void setAcuteness(String acuteness) {
        this.acuteness = acuteness;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    public String getTriageLevel() {
        return triageLevel;
    }

    public void setTriageLevel(String triageLevel) {
        this.triageLevel = triageLevel;
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