package com.example.rokly.notadoctor.Model.Diagnose.Request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Extras implements Parcelable
{

    @SerializedName("disable_groups")
    @Expose
    private Boolean disableGroups;
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
        this.disableGroups = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Extras() {
    }

    /**
     *
     * @param disableGroups
     */
    public Extras(Boolean disableGroups) {
        super();
        this.disableGroups = disableGroups;
    }

    public Boolean getDisableGroups() {
        return disableGroups;
    }

    public void setDisableGroups(Boolean disableGroups) {
        this.disableGroups = disableGroups;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(disableGroups);
    }

    public int describeContents() {
        return 0;
    }

}