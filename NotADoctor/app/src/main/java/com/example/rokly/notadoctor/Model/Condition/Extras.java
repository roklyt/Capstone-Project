package com.example.rokly.notadoctor.Model.Condition;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extras implements Parcelable {

    @SerializedName("hint")
    @Expose
    private String hint;
    @SerializedName("icd10_code")
    @Expose
    private String icd10Code;
    public final static Parcelable.Creator<Extras> CREATOR = new Creator<Extras>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Extras createFromParcel(Parcel in) {
            return new Extras(in);
        }

        public Extras[] newArray(int size) {
            return (new Extras[size]);
        }

    }
            ;

    protected Extras(Parcel in) {
        this.hint = ((String) in.readValue((String.class.getClassLoader())));
        this.icd10Code = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Extras() {
    }

    /**
     *
     * @param hint
     * @param icd10Code
     */
    public Extras(String hint, String icd10Code) {
        super();
        this.hint = hint;
        this.icd10Code = icd10Code;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(hint);
        dest.writeValue(icd10Code);
    }

    public int describeContents() {
        return 0;
    }

}